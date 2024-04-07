package event.tickets.easv.bar.gui.component.events;

import event.tickets.easv.bar.bll.EntityManager;
import event.tickets.easv.bar.gui.common.*;
import event.tickets.easv.bar.gui.common.Action.DeleteEvent;
import event.tickets.easv.bar.gui.common.Action.RemoveCoordinator;
import event.tickets.easv.bar.gui.util.BackgroundExecutor;
import event.tickets.easv.bar.util.Result;

public class DeleteEventController {
    public DeleteEventController() {
    }

    void onDeleteEvent(Runnable postTaskGuiActions, EventModel eventModel) {
        BackgroundExecutor.performBackgroundTask(
                () -> deleteEvent(eventModel),
                postTaskGuiActions,
                success -> ActionHandler.handle(new DeleteEvent(eventModel)),
                failure -> ViewHandler.notify(NotificationType.FAILURE, "Failed to delete event")
        );
    }

    private Result<Boolean> deleteEvent(EventModel eventModel) {
        return new EntityManager().delete(eventModel.toEntity());
    }

    void onRemoveCoordinator(Runnable postTaskGuiActions, EventModel eventModel, UserModel userModel) {
        BackgroundExecutor.performBackgroundTask(
                () -> removeCoordinator(eventModel, userModel),
                postTaskGuiActions,
                success -> ActionHandler.handle(new RemoveCoordinator(eventModel, userModel)),
                failure -> ViewHandler.notify(NotificationType.FAILURE, "Failed to remove coordinator")
        );
    }

    private Result<Boolean> removeCoordinator(EventModel eventModel, UserModel userModel) {
        System.out.println("hey");
        return new EntityManager().removeAssociation(eventModel.toEntity(), userModel.toEntity());
    }
}
