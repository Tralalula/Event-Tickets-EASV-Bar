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
    CREATE_EVENT(WindowType.MAIN_APP, EVENTS),
    SHOW_EVENT(WindowType.MAIN_APP, EVENTS),
    ASSIGN_TICKET(WindowType.MAIN_APP, SHOW_EVENT),

    // Tickets
    TICKETS(WindowType.MAIN_APP),
    ADD_TICKET(WindowType.MAIN_APP, TICKETS),
    SHOW_TICKET(WindowType.MAIN_APP, TICKETS),
    ADD_TICKET_EVENT(WindowType.MAIN_APP, SHOW_TICKET),
    ASSIGN_TICKET_VIEW(WindowType.MAIN_APP, SHOW_TICKET),

    // Users
    USERS(WindowType.MAIN_APP),
    SHOW_USER(WindowType.MAIN_APP, USERS),
    CREATE_USER(WindowType.MAIN_APP, USERS);

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