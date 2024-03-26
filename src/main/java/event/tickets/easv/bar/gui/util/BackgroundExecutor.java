package event.tickets.easv.bar.gui.util;

import event.tickets.easv.bar.util.FailureType;
import event.tickets.easv.bar.util.Result;
import event.tickets.easv.bar.util.Result.Failure;
import javafx.concurrent.Task;

import java.util.concurrent.Callable;
import java.util.function.Consumer;

/**
 * Helper class to provide methods class for executing background tasks.
 */
public class BackgroundExecutor {

    /**
     * Executes a given task in the background and handles the result asynchronously.
     *
     * @param task the task to be executed.
     * @param onResult what to do when the task completes.
     * @param <T> the type of the result produced by the task.
     */
    public static <T> void performBackgroundTask(Callable<Result<T>> task, Consumer<Result<T>> onResult) {
        Task<Result<T>> backgroundTask = new Task<>() {
            @Override
            protected Result<T> call() {
                try {
                    return task.call();
                } catch (Exception e) {
                    return new Failure<>(FailureType.BACKGROUND_TASK_FAILURE, "An error occurred in a background task", e);
                }
            }
        };

        backgroundTask.setOnSucceeded(event -> onResult.accept(backgroundTask.getValue()));

        new Thread(backgroundTask).start();
    }
}
