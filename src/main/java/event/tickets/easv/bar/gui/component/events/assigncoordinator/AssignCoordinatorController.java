package event.tickets.easv.bar.gui.component.events.assigncoordinator;

import event.tickets.easv.bar.bll.EntityManager;
import event.tickets.easv.bar.gui.common.Action.AssignCoordinator;
import event.tickets.easv.bar.gui.common.Action.CreateUser;
import event.tickets.easv.bar.gui.common.ActionHandler;
import event.tickets.easv.bar.gui.common.EventModel;
import event.tickets.easv.bar.gui.common.UserModel;
import event.tickets.easv.bar.gui.util.BackgroundExecutor;
import event.tickets.easv.bar.util.Result;
import event.tickets.easv.bar.util.Result.Success;
import event.tickets.easv.bar.util.Result.Failure;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

public class AssignCoordinatorController {
    private final AssignCoordinatorModel model;
    private final EventModel currentEventModel;

    public AssignCoordinatorController(AssignCoordinatorModel model, EventModel currentEventModel) {
        this.model = model;
        this.currentEventModel = currentEventModel;
    }

    void onAssignCoordinator(Runnable postTaskGuiActions, UserModel userModel) {
        BackgroundExecutor.performBackgroundTask(
                () -> assignCoordinator(userModel),
                postTaskGuiActions,
                success -> ActionHandler.handle(new AssignCoordinator(currentEventModel, userModel)),
                failure -> System.out.println("Fejl: " + failure)
        );
    }

    private Result<Boolean> assignCoordinator(UserModel userModel) {
        return new EntityManager().addAssociation(currentEventModel.toEntity(), userModel.toEntity());
    }
}
