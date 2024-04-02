package event.tickets.easv.bar.gui.component.events.assigncoordinator;

import event.tickets.easv.bar.bll.EntityManager;
import event.tickets.easv.bar.gui.common.EventModel;
import event.tickets.easv.bar.gui.common.UserModel;
import event.tickets.easv.bar.gui.util.EventBus;
import event.tickets.easv.bar.util.Result;
import event.tickets.easv.bar.util.Result.Success;
import event.tickets.easv.bar.util.Result.Failure;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

public class AssignCoordinatorController {
    private final AssignCoordinatorModel model;
    private final EventModel currentEventModel;
    private final ObservableList<EventModel> masterEventList;
    private final ObservableList<UserModel> masterUserList;

    public AssignCoordinatorController(AssignCoordinatorModel model, EventModel currentEventModel, ObservableList<EventModel> masterEventList, ObservableList<UserModel> masterUserList) {
        this.model = model;
        this.currentEventModel = currentEventModel;
        this.masterEventList = masterEventList;
        this.masterUserList = masterUserList;
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
            if (assignTask.getValue()) {
                Platform.runLater(() -> {
                    EventBus.publish(new CoordinatorAssignedEvent(userModel, currentEventModel));
                });
            }
        });

        var assignThread = new Thread(assignTask);
        assignThread.start();
    }

    private void assignCoordinatorInLists(UserModel currentUserModel) {
        UserModel masterUser = masterUserList.stream().filter(u -> u.id().get() == currentUserModel.id().get())
                .findFirst()
                .orElse(null);

        EventModel masterEvent = masterEventList.stream().filter(e -> e.id().get() == currentEventModel.id().get())
                .findFirst()
                .orElse(null);

        if (masterUser != null && masterEvent != null) {
            masterEvent.users().add(masterUser);
            masterUser.events().add(masterEvent);
        } else {
            System.err.println("AssignCoordinatorController: Could not find corresponding models in master lists.");
        }
    }

    private boolean assignCoordinator(UserModel userModel) {
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
