package event.tickets.easv.bar.gui.component.events.assigncoordinator;

import event.tickets.easv.bar.gui.common.EventModel;
import event.tickets.easv.bar.gui.common.UserModel;

public class CoordinatorAssignedEvent {
    private final UserModel userModel;
    private final EventModel eventModel;

    public CoordinatorAssignedEvent(UserModel userModel, EventModel eventModel) {
        this.userModel = userModel;
        this.eventModel = eventModel;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public EventModel getEventModel() {
        return eventModel;
    }
}
