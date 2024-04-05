package event.tickets.easv.bar.gui.component.events;

import event.tickets.easv.bar.be.Event;
import event.tickets.easv.bar.bll.EntityManager;
import event.tickets.easv.bar.gui.common.*;
import event.tickets.easv.bar.gui.common.Action.DeleteEvent;
import event.tickets.easv.bar.gui.util.BackgroundExecutor;
import event.tickets.easv.bar.util.Result;

public class ShowEventController {
    public ShowEventController() {
    }

    void onDeleteEvent(EventModel eventModel) {
        BackgroundExecutor.performBackgroundTask(
                () -> deleteEvent(eventModel),
                () -> {},
                success -> {
                    ActionHandler.handle(new DeleteEvent(eventModel));
                    ViewHandler.previousView();
                },
                failure -> ViewHandler.notify(NotificationType.FAILURE, "Failed to delete event")
        );
    }

    private Result<Boolean> deleteEvent(EventModel eventModel) {
        return new EntityManager().delete(eventModel.toEntity());
    }
}
