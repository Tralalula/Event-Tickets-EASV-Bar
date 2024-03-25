package event.tickets.easv.bar.gui.util;

import javafx.concurrent.Task;

import java.util.concurrent.Callable;
import java.util.function.Consumer;

public class BackgroundExecutor {
    public static <T> void performBackgroundTask(Callable<T> task, Consumer<T> onSuccess, Consumer<Exception> onError) {
        Task<T> backgroundTask = new Task<>() {
            @Override
            protected T call() throws Exception {
                return task.call();
            }
        };

        backgroundTask.setOnSucceeded(event -> {
            T result = backgroundTask.getValue();
            onSuccess.accept(result);
        });

        backgroundTask.setOnFailed(event -> {
            Throwable error = backgroundTask.getException();
            onError.accept((Exception) error);
        });

        new Thread(backgroundTask).start();
    }
}
