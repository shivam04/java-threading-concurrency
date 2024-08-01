package threading.virtualthreads;

public class Main3 {
    public static void main(String[] args) throws InterruptedException {
        Runnable runnable = () -> System.out.println("Inside thread: " + Thread.currentThread());

        Thread virtualThread = Thread.ofVirtual().unstarted(runnable);
        virtualThread.start();
        virtualThread.join();
        System.out.println("Inside thread: " + Thread.currentThread());
    }
}
