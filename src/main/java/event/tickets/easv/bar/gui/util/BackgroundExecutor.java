package event.tickets.easv.bar.gui.util;

import event.tickets.easv.bar.util.Result;
import event.tickets.easv.bar.util.Result.Success;
import event.tickets.easv.bar.util.Result.Failure;
import javafx.concurrent.Task;

import java.util.concurrent.Callable;
import java.util.function.Consumer;

public class BackgroundExecutor {
    public static <T> void performBackgroundTask(Callable<Result<T>> task, Consumer<Result<T>> onResult) {
        Task<Result<T>> backgroundTask = new Task<>() {
            @Override
            protected Result<T> call() {
                try {
                    return task.call();
                } catch (Exception e) {
                    return new Failure<>(e);
                }
            }
        };

        backgroundTask.setOnSucceeded(event -> onResult.accept(backgroundTask.getValue()));

        new Thread(backgroundTask).start();
    }
}
