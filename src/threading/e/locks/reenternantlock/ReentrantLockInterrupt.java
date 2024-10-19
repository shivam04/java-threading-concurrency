package threading.e.locks.reenternantlock;

import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockInterrupt {

    static ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {
        Thread thread1 = new Thread(new InterruptCheck());
        Thread thread2 = new Thread(new InterruptCheck());
        thread1.start();
        thread2.start();
    }

    private static class InterruptCheck implements Runnable {
        @Override
        public void run() {
            try {
                lock.lockInterruptibly();
                System.out.println("Start....." + Thread.currentThread().getName());
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                System.out.println("Exception: " + Thread.currentThread().getName());
                ex.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }
}
