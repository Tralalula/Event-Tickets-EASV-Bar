package event.tickets.easv.bar.gui.util;

import event.tickets.easv.bar.util.FailureType;
import event.tickets.easv.bar.util.Result;
import event.tickets.easv.bar.util.Result.Failure;
import event.tickets.easv.bar.util.Result.Success;
import javafx.concurrent.Task;

import java.util.concurrent.Callable;
import java.util.function.Consumer;

/**
 * Helper class to provide methods for executing background tasks.
 */
public class BackgroundExecutor {

    /**
     * Executes a given task in the background, handles the result asynchronously,
     * and runs a post-task GUI action on the JavaFX thread.
     *
     * @param task the task to be executed.
     * @param postTaskGuiAction a Runnable that executes on the JavaFX Application Thread after the task completes and before onResult is called.
     * @param onResult what to do when the task completes.
     * @param <T> the type of the result produced by the task.
     */
    public static <T> void performBackgroundTask(Callable<Result<T>> task, Runnable postTaskGuiAction, Consumer<Result<T>> onResult) {
        Task<Result<T>> backgroundTask = new Task<>() {
            @Override
            protected Result<T> call() {
                try {
                    return task.call();
                } catch (Exception e) {
                    return new Failure<>(FailureType.BACKGROUND_TASK_FAILURE, "An error occurred during a background task", e);
                }
            }
        };

        backgroundTask.setOnSucceeded(event -> {
            if (postTaskGuiAction != null) {
                postTaskGuiAction.run();
            }
            onResult.accept(backgroundTask.getValue());
        });

        new Thread(backgroundTask).start();
    }

    /**
     * Executes a given task in the background and handles the result asynchronously.
     *
     * @param task the task to be executed.
     * @param onResult what to do when the task completes.
     * @param <T> the type of the result produced by the task.
     */
    public static <T> void performBackgroundTask(Callable<Result<T>> task, Consumer<Result<T>> onResult) {
        performBackgroundTask(task,null, onResult);
    }

    /**
     * Executes a given task in the background, handles the result asynchronously by executing
     * separate actions for success and failure, and runs a post-task GUI action on the JavaFX thread.
     *
     * @param task the task to be executed.
     * @param postTaskGuiAction a Runnable that executes on the JavaFX Application Thread after the task completes.
     * @param onSuccess what to do if the task is a success
     * @param onFailure what to do if the task is a failure
     * @param <T> the type of the result produced by the task.
     */
    public static <T> void performBackgroundTask(Callable<Result<T>> task, Runnable postTaskGuiAction, Consumer<Success<T>> onSuccess, Consumer<Failure<T>> onFailure) {
        Task<Result<T>> backgroundTask = new Task<>() {
            @Override
            protected Result<T> call() {
                try {
                    return task.call();
                } catch (Exception e) {
                    return new Failure<>(FailureType.BACKGROUND_TASK_FAILURE, "An error occurred during a background task", e);
                }
            }
        };

        backgroundTask.setOnSucceeded(event -> {
            if (postTaskGuiAction != null) {
                postTaskGuiAction.run();
            }
            Result<T> result = backgroundTask.getValue();

            if (result.isSuccess()) {
                onSuccess.accept((Success<T>) result);
            } else {
                onFailure.accept((Failure<T>) result);
            }
        });

        new Thread(backgroundTask).start();
    }
}
