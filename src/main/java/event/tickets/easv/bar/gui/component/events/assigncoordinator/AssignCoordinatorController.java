package event.tickets.easv.bar.gui.component.events.assigncoordinator;

import event.tickets.easv.bar.bll.EntityManager;
import event.tickets.easv.bar.gui.common.EventModel;
import event.tickets.easv.bar.gui.common.UserModel;
import event.tickets.easv.bar.util.Result;
import event.tickets.easv.bar.util.Result.Success;
import event.tickets.easv.bar.util.Result.Failure;
import javafx.concurrent.Task;

public class AssignCoordinatorController {
    private final AssignCoordinatorModel model;
    private final EventModel eventModel;

    public AssignCoordinatorController(AssignCoordinatorModel model, EventModel eventModel) {
        this.model = model;
        this.eventModel = eventModel;
    }

    void assignCoordinator(Runnable postTaskGuiActions, UserModel userModel) {
        Task<Boolean> assignTask = new Task<>() {
            @Override
            protected Boolean call() {
                return assignCoordinator(userModel);
            }
        };

        assignTask.setOnSucceeded(evt -> {
            postTaskGuiActions.run();
            System.out.println("yoooo");
            if (!assignTask.getValue()) {
                System.out.println("hey-o");
            }
        });

        var assignThread = new Thread(assignTask);
        assignThread.start();
    }

    private boolean assignCoordinator(UserModel userModel) {
        Result<Boolean> result = new EntityManager().addAssociation(eventModel.toEntity(), userModel.toEntity());
        boolean ret = false;
        switch (result) {
            case Success<Boolean> s -> ret = s.result();
            case Failure<Boolean> f -> System.out.println("Fejl ved tildeling af koordinator: " + f);
        }

        return ret;
    }
}
