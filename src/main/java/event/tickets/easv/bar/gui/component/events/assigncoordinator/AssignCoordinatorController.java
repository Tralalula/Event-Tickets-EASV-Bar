package event.tickets.easv.bar.gui.component.events.assigncoordinator;

import event.tickets.easv.bar.bll.EntityManager;
import event.tickets.easv.bar.gui.common.Action.AssignCoordinator;
import event.tickets.easv.bar.gui.common.ActionHandler;
import event.tickets.easv.bar.gui.common.EventModel;
import event.tickets.easv.bar.gui.common.UserModel;
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
        Task<Boolean> assignTask = new Task<>() {
            @Override
            protected Boolean call() {
                return onAssignCoordinator(userModel);
            }
        };

        assignTask.setOnSucceeded(evt -> {
            postTaskGuiActions.run();
            if (assignTask.getValue()) {
                ActionHandler.handle(new AssignCoordinator(currentEventModel, userModel));
            }
        });

        var assignThread = new Thread(assignTask);
        assignThread.start();
    }

    private boolean onAssignCoordinator(UserModel userModel) {
        Result<Boolean> result = new EntityManager().addAssociation(currentEventModel.toEntity(), userModel.toEntity());
        switch (result) {
            case Success<Boolean> s -> {
                return s.result();
            }
            case Failure<Boolean> f -> System.out.println("Fejl ved tildeling af koordinator: " + f);
        }

        return false;
    }
}
