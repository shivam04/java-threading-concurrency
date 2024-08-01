package threading.virtualthreads;

import java.util.ArrayList;
import java.util.List;

public class PlatformThreadBlockingExample {
    private static final int NUMBER_OF_PLATFORM_THREADS = 1000;
    public static void main(String[] args) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        List<Thread> platformThreads = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_PLATFORM_THREADS; i++) {
            Thread platformThread = new Thread(new BlockingTask());
            platformThreads.add(platformThread);
        }

        for (Thread platformThread: platformThreads) {
            platformThread.start();
        }

        for (Thread platformThread: platformThreads) {
            platformThread.join();
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
