package event.tickets.easv.bar.gui.common;

public sealed interface Action {
    record AssignCoordinator(EventModel eventModel, UserModel coordinator) implements Action {}

}
