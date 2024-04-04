package event.tickets.easv.bar.gui.common;

public sealed interface Action {
    record CreateEvent(EventModel eventModel) implements Action {}
    record CreateUser(UserModel userModel) implements Action {}
    record AssignCoordinator(EventModel eventModel, UserModel coordinator) implements Action {}

}