package event.tickets.easv.bar.gui.common;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.ArrayDeque;
import java.util.Deque;

public class ViewHandler {
    private static final ViewHandler INSTANCE = new ViewHandler();

    private final ObjectProperty<WindowType> activeWindow = new SimpleObjectProperty<>(WindowType.MAIN_APP);
    private final ObjectProperty<ViewType> activeView = new SimpleObjectProperty<>(ViewType.DASHBOARD);
    private final ObjectProperty<ViewType> previousView = new SimpleObjectProperty<>(ViewType.DASHBOARD);
    private final Deque<ViewType> navigationStack = new ArrayDeque<>();

    private ViewHandler() {
        if (INSTANCE != null) {
            throw new AssertionError("Use getInstance() method to get single instance of this class.");
        }
        navigationStack.push(ViewType.DASHBOARD);
    }

    public static void changeView(ViewType newView) {
        INSTANCE.changeViewInstance(newView);
    }

    public static ObjectProperty<ViewType> activeViewProperty() {
        return INSTANCE.activeView;
    }

    public static ObjectProperty<WindowType> activeWindowProperty() {
        return INSTANCE.activeWindow;
    }

    public static Deque<ViewType> navigationStack() {
        return INSTANCE.navigationStack;
    }

    private void changeViewInstance(ViewType newView) {
        previousView.set(activeView.get());
        activeView.set(newView);
        activeWindow.set(newView.windowType());
        navigationStack.push(newView);
    }


    // readResolve is used to make sure that the singleton instance
    // remains a singleton even in cases of serialization
    protected ViewHandler readResolve() {
        return INSTANCE;
    }
}
