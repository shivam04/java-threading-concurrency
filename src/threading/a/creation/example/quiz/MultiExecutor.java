package threading.a.creation.example.quiz;

import java.util.ArrayList;
import java.util.List;

public class MultiExecutor {
    List<Thread> threads = new ArrayList<>();

    /*
     * @param tasks to executed concurrently
     */
    public MultiExecutor(List<Runnable> tasks) {
        for(Runnable r: tasks) {
            threads.add(new Thread(r));
        }
    }

    /**
     * Starts and executes all the tasks concurrently
     */
    public void executeAll() {
        for(Thread thread: threads) {
            thread.start();
        }
    }
}
