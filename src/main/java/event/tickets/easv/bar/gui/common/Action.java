package event.tickets.easv.bar.gui.common;

public sealed interface Action {
    record CreateEvent(EventModel eventModel) implements Action {}
    record EditEvent(EventModel original, EventModel updated) implements Action {}
    record DeleteEvent(EventModel eventModel) implements Action {}
    record CreateUser(UserModel userModel) implements Action {}
    record EditUser(UserModel original, UserModel updated) implements Action {}
    record SaveProfile(UserModel original, UserModel updated) implements Action {}
    record DeleteUser(UserModel userModel) implements Action {}
    record AssignCoordinator(EventModel eventModel, UserModel coordinator) implements Action {}
    record RemoveCoordinator(EventModel eventModel, UserModel coordinator) implements Action {}
    record DeleteTicket(TicketModel ticketModel) implements Action {}
}
