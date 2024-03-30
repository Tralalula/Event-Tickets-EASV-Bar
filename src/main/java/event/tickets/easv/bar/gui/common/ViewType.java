package event.tickets.easv.bar.gui.common;

/**
 * The different views available within the application,
 * associating each view with a specific WindowType to denote its context<br>
 * <br>
 * Additionally, it supports a tree-like structure among views,
 * giving parent-child relationships for navigation and organizational purposes.
 */
public enum ViewType {
    NO_VIEW(WindowType.NONE),

    /* ------ Auth Views ------ */
    LOGIN(WindowType.AUTH),
    FORGOT_PASSWORD(WindowType.AUTH),
    VERIFY(WindowType.AUTH),
    RESET_PASSWORD(WindowType.AUTH),

    /* ------ Main App Views ------ */
    // Dashboard
    DASHBOARD(WindowType.MAIN_APP),

    // Events
    EVENTS(WindowType.MAIN_APP),
    SHOW_EVENT(WindowType.MAIN_APP, EVENTS),
    ASSIGN_TICKET(WindowType.MAIN_APP, SHOW_EVENT),

    // Tickets
    TICKETS(WindowType.MAIN_APP),
    ADD_TICKET(WindowType.MAIN_APP, TICKETS),
    SHOW_TICKET(WindowType.MAIN_APP, TICKETS),

    // Users
    USERS(WindowType.MAIN_APP);

    private final WindowType windowType;
    private final ViewType parent;

    ViewType(WindowType windowType) {
        this(windowType, null);
    }

    ViewType(WindowType windowType, ViewType parent) {
        this.windowType = windowType;
        this.parent = parent;
    }

    /**
     * @return the associated window type of this view type.
     */
    public WindowType windowType() {
        return windowType;
    }

    /**
     * @return the parent view type of this view type.
     */
    public ViewType parent() {
        return parent;
    }
}