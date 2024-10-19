package threading.i.virtualthreads;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Runnable runnable = () -> System.out.println("Inside thread: " + Thread.currentThread());

        Thread platformThread = new Thread(runnable);
        platformThread.start();
        platformThread.join();
        System.out.println("Inside thread: " + Thread.currentThread());
    }
}
