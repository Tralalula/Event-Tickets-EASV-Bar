package event.tickets.easv.bar.gui.component.tickets;

import event.tickets.easv.bar.gui.common.EventModel;

public class EventModelWrapper {
    private final EventModel eventModel;

    public EventModelWrapper(EventModel eventModel) {
        this.eventModel = eventModel;
    }

    public EventModel getEventModel() {
        return eventModel;
    }

    @Override
    public String toString() {
        return eventModel.title().get();
    }
}