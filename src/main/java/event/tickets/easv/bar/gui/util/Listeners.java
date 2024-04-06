package event.tickets.easv.bar.gui.util;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class Listeners {
    /**
     * Adds a one-time change listener to an ObservableValue. The listener is automatically removed after the first change.
     *
     * @param observable the ObservableValue to listen to.
     * @param action the action to perform when a change occurs.
     * @param <T> the type of the observable value.
     */
    public static <T> void addOnceChangeListener(ObservableValue<T> observable, Runnable action) {
        ChangeListener<T> listener = new ChangeListener<>() {
            @Override
            public void changed(ObservableValue<? extends T> obs, T ov, T nv) {
                action.run();
                obs.removeListener(this);
            }
        };
        observable.addListener(listener);
    }
}
