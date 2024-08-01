package threading.inter.communication.waitnotify.quiz;

import java.util.ArrayList;
import java.util.List;

public class SimpleCountDownLatch {
    private int count;

    public SimpleCountDownLatch(int count) {
        this.count = count;
        if (count < 0) {
            throw new IllegalArgumentException("count cannot be negative");
        }
    }

    /**
     * Causes the current thread to wait until the latch has counted down to zero.
     * If the current count is already zero then this method returns immediately.
     */
    public synchronized void await() throws InterruptedException {
        while (count > 0) {
            System.out.println(Thread.currentThread().getName() + " is waiting");
            wait();
            System.out.println(Thread.currentThread().getName() + " wait over");
        }
    }

    /**
     *  Decrements the count of the latch, releasing all waiting threads when the count reaches zero.
     *  If the current count already equals zero then nothing happens.
     */
    public synchronized void countDown() throws InterruptedException {
        if (count == 0) {
            return;
        }
        count--;
        if (count == 0) {
            notifyAll();
        }
    }

    /**
     * Returns the current count.
     */
    public int getCount() {
        return count;
    }

    public static void main(String[] args) throws InterruptedException {
        SimpleCountDownLatch simpleCountDownLatch = new SimpleCountDownLatch(5);
        DownThread downThread = new DownThread(simpleCountDownLatch);
        downThread.setDaemon(true);

        List<Thread> threadList = new ArrayList<>();
        for (int i=0;i<10;i++) {
            WaitAndGetThread thread = new WaitAndGetThread(simpleCountDownLatch);
            thread.setDaemon(true);
            thread.setName("Reader"+i);
            threadList.add(thread);
        }

        for(Thread thread: threadList) {
            thread.start();
        }
        downThread.start();

        for(Thread thread: threadList) {
            thread.join();
        }
    }

    public static class DownThread extends Thread {
        private SimpleCountDownLatch simpleCountDownLatch;

        public DownThread(SimpleCountDownLatch simpleCountDownLatch) {
            this.simpleCountDownLatch = simpleCountDownLatch;
        }

        @Override
        public void run() {
            while(true) {
                try {
                    simpleCountDownLatch.countDown();
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public static class WaitAndGetThread extends Thread {
        private SimpleCountDownLatch simpleCountDownLatch;

        public WaitAndGetThread(SimpleCountDownLatch simpleCountDownLatch) {
            this.simpleCountDownLatch = simpleCountDownLatch;
        }

        @Override
        public void run() {
            try {
                simpleCountDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " result: " + simpleCountDownLatch.getCount());
        }
    }
}

