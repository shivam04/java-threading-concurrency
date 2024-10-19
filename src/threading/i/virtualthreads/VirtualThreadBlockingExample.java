package threading.i.virtualthreads;

import java.util.ArrayList;
import java.util.List;

public class VirtualThreadBlockingExample {
    private static final int NUMBER_OF_VIRTUAL_THREADS = 1000;
    public static void main(String[] args) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        List<Thread> virtualThreads = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_VIRTUAL_THREADS; i++) {
            Thread virtualThread = Thread.ofVirtual().unstarted(new BlockingTask());
            virtualThreads.add(virtualThread);
        }

        for (Thread virtualThread: virtualThreads) {
            virtualThread.start();
        }

        for (Thread virtualThread: virtualThreads) {
            virtualThread.join();
        }

        System.out.println("Inside thread: " + Thread.currentThread() + "call finish time= " + (System.currentTimeMillis() - startTime));
    }

    private static class BlockingTask implements Runnable {
        @Override
        public void run() {
            long startTime = System.currentTimeMillis();
            System.out.println("Inside thread: " + Thread.currentThread() + " before blocking call");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Inside thread: " + Thread.currentThread() + " after blocking call finish time= " + (System.currentTimeMillis() - startTime));
        }
    }
}
