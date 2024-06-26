package event.tickets.easv.bar.gui.common;

import atlantafx.base.controls.ModalPane;
import atlantafx.base.controls.Notification;
import atlantafx.base.theme.Styles;
import event.tickets.easv.bar.gui.widgets.Dialog;
import event.tickets.easv.bar.gui.widgets.ModalDialog;
import event.tickets.easv.bar.gui.widgets.ModalOverlay;
import event.tickets.easv.bar.gui.widgets.NotificationBox;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2OutlinedAL;
import org.kordamp.ikonli.material2.Material2OutlinedMZ;

import java.util.*;

/**
 * A Singleton handler for managing views and their navigation history within an application.
 */
public class ViewHandler {
    private static final ViewHandler INSTANCE = new ViewHandler();
    private final ModalPane overlay = new ModalOverlay();

    private final BooleanProperty showNotification = new SimpleBooleanProperty(false);
    private final NotificationBox notificationBox = new NotificationBox();

    private final ObjectProperty<Object> currentViewData = new SimpleObjectProperty<>();
    private final ObjectProperty<WindowType> activeWindow = new SimpleObjectProperty<>(WindowType.NONE);
    private final ObjectProperty<ViewType> activeView = new SimpleObjectProperty<>(ViewType.NO_VIEW);
    private final ObjectProperty<ViewType> previousView = new SimpleObjectProperty<>(ViewType.NO_VIEW);
    private final ObjectProperty<ViewType> activeRootView = new SimpleObjectProperty<>(ViewType.NO_VIEW);
    private final List<ViewType> navigationStack = new LinkedList<>();

    // Rep invariant:
    // - The last element of navigationStack, if present, matches activeView.
    // - activeView's windowType matches activeWindow.

    // Abstraction function:
    // - AF(activeWindow, activeView, previousView, navigationStack) = a view handler where:
    //   - activeWindow represents the current type of window being displayed
    //   - activeView represents the current view within the active window.
    //   - previousView represents the view that was active before the current view.
    //   - navigationStack is a history of views that have been activated within a window, with the most recent at the end.
    private void instanceCheckRep() {
        assert activeWindow != null : "activeWindow must not be null";
        assert activeView != null : "activeView must not be null";
        assert previousView != null : "previousView must not be null";
        assert navigationStack != null : "navigationStack must not be null";
        assert !navigationStack.contains(null) : "navigationStack must not contain null elements";
        assert navigationStack.isEmpty() || navigationStack.getLast().equals(activeView.get()) : "activeView should match the last view in navigationStack";
        assert activeView.get().windowType().equals(activeWindow.get()) : "activeView's window type must match activeWindow";
    }

    private static void checkRep() {
        INSTANCE.instanceCheckRep();
    }

    private ViewHandler() {
        navigationStack.add(ViewType.NO_VIEW);
    }


    /**
     * Updates the active view and active window to
     * match the ViewType and WindowType from the specified newView.

     * @param newView the ViewType to be changed to. Must not be null.
     * @throws IllegalArgumentException if newView is null.
     */
    public static void changeView(ViewType newView) {
        if (newView == null) throw new IllegalArgumentException("newView must not be null");
        INSTANCE.changeViewInstance(newView);
    }

    public static void changeView(ViewType newView, Object data) {
        if (newView == null) throw new IllegalArgumentException("newView must not be null");
        if (data != null) INSTANCE.currentViewData.set(data);

        INSTANCE.changeViewInstance(newView);
    }

    public static void showOverlay(Node node) {
        INSTANCE.overlay.show(node);
    }

    public static void showOverlay(Node content, int width, int height) {
        var dialog = new Dialog(width, height);
        dialog.getChildren().setAll(content);
        INSTANCE.overlay.show(dialog);
    }

    public static void showOverlay(String title, Node content, int width, int height) {
        var dialog = new ModalDialog(title, content, width, height);
        INSTANCE.overlay.show(dialog);
    }

    public static void showOverlay(String title, Node content, Node footer, int width, int height) {
        var dialog = new ModalDialog(title, content, footer, width, height);
        INSTANCE.overlay.show(dialog);
    }

    public static void hideOverlay() {
        INSTANCE.overlay.hide();
    }

    public static void notify(NotificationType type, String message) {
        var notification = new Notification(message);
        notification.setPrefHeight(Region.USE_PREF_SIZE);
        notification.setMaxHeight(Region.USE_PREF_SIZE);

        notification.setGraphic(switch (type) {
            case REGULAR -> new FontIcon(Material2OutlinedAL.CHAT_BUBBLE_OUTLINE);
            case INFO -> new FontIcon(Material2OutlinedAL.HELP_OUTLINE);
            case SUCCESS -> new FontIcon(Material2OutlinedAL.CHECK_CIRCLE_OUTLINE);
            case WARNING -> new FontIcon(Material2OutlinedMZ.OUTLINED_FLAG);
            case FAILURE -> new FontIcon(Material2OutlinedAL.ERROR_OUTLINE);
        });

        var style = switch (type) {
            case INFO -> Styles.ACCENT;
            case SUCCESS -> Styles.SUCCESS;
            case WARNING -> Styles.WARNING;
            case FAILURE -> Styles.DANGER;
            default -> "";
        };

        notification.getStyleClass().addAll(Styles.INTERACTIVE, style);
        INSTANCE.notificationBox.addNotification(notification);
    }

    public static ObjectProperty<Object> currentViewDataProperty() {
        return INSTANCE.currentViewData;
    }

    /**
     * Navigates to the previous view within the current window type.<br>
     * It updates the active view and active window to match the view that was before the current view.<br>
     * <br>
     * Navigation history is maintained separately for each window type.<br>
     * This means that navigating to a previous view will only go back to the initial view within the current window type.<br>
     * <br>
     * Usage scenarios:<br>
     *  - If no view change has occurred, calling this method will not alter the active view or active window type.<br>
     *  - If a view within the current window type has been changed,
     *    calling this method will navigate to the previous view within the same window type.<br>
     *  - If the window type has changed due to a view change,
     *    calling this method will not navigate back to a view belonging to a different window type,
     *    but will navigate to the initial view of the current window type.<br>
     * <br>
     * Example:<br>
     * Initial state            : ViewType / WindowType = NO_VIEW / NONE<br>
     * Chang view to LOGIN      : ViewType / WindowType = LOGIN / AUTH (new initial view, because window change)<br>
     * Change view to DASHBOARD : ViewType / WindowType = DASHBOARD / MAIN_APP (new initial view, because window change)<br>
     * Change view to EVENTS    : ViewType / WindowType = EVENTS / MAIN_APP<br>
     * Change view to SHOW_EVENT: ViewType / WindowType = SHOW_EVENT / MAIN_APP<br>
     * -- Go back to initial view --<br>
     * Previous view            : ViewType / WindowType = EVENTS / MAIN_APP<br>
     * Previous view            : ViewType / WindowType = DASHBOARD / MAIN_APP<br>
     * Previous view            : ViewType / WindowType = DASHBOARD / MAIN_APP (cannot go back further, this is 1st view of active window)
     */
    public static void previousView() {
        var stack = INSTANCE.navigationStack;
        var currentView = INSTANCE.activeView.get();
        var previousView = INSTANCE.previousView.get();

        var currentWindow = currentView.windowType();
        var previousWindow = previousView.windowType();

        if (stack.size() <= 1) return;
        if (!previousWindow.equals(currentWindow)) return;

        stack.removeLast();

        INSTANCE.activeView.set(previousView);
        INSTANCE.activeWindow.set(previousWindow);
        INSTANCE.previousView.set(stack.size() > 1 ? stack.get(stack.size() - 2) : ViewType.NO_VIEW);
        INSTANCE.updateActiveRootView(INSTANCE.activeView.get());
        checkRep();
    }

    public static ReadOnlyObjectProperty<ViewType> activeViewProperty() {
        return INSTANCE.activeView;
    }

    private void updateActiveRootView(ViewType viewType) {
        INSTANCE.activeRootView.set(ViewTypeHelper.rootOf(viewType));
    }

    public static ReadOnlyObjectProperty<ViewType> activeRootViewProperty() {
        return INSTANCE.activeRootView;
    }

    public static ReadOnlyObjectProperty<WindowType> activeWindowProperty() {
        return INSTANCE.activeWindow;
    }

    public static ModalPane overlay() {
        return INSTANCE.overlay;
    }

    public static StackPane notificationArea() {
        return INSTANCE.notificationBox.getNotificationArea();
    }

    private void changeViewInstance(ViewType newView) {
        if (!navigationStack.isEmpty() && !newView.windowType().equals(activeView.get().windowType())) {
            navigationStack.clear();
            previousView.set(ViewType.NO_VIEW);
        }

        previousView.set(activeView.get());
        activeView.set(newView);
        activeWindow.set(newView.windowType());
        navigationStack.add(newView);
        INSTANCE.updateActiveRootView(INSTANCE.activeView.get());
        checkRep();
    }

    // readResolve is used to make sure that the singleton instance
    // remains a singleton even in cases of serialization
    protected ViewHandler readResolve() {
        return INSTANCE;
    }

    /**
     * To reset Singleton state for testing purposes.
     */
    static void reset() {
        INSTANCE.activeWindow.set(WindowType.NONE);
        INSTANCE.activeView.set(ViewType.NO_VIEW);
        INSTANCE.previousView.set(ViewType.NO_VIEW);
        INSTANCE.navigationStack.clear();
        checkRep();
    }
}
