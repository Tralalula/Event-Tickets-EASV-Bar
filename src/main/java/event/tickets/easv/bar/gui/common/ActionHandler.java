package event.tickets.easv.bar.gui.common;

import event.tickets.easv.bar.gui.common.Action.AssignCoordinator;
import event.tickets.easv.bar.gui.component.main.MainModel;
import javafx.collections.ObservableList;

import java.util.Optional;
import java.util.function.Predicate;

public class ActionHandler {
    private static final ActionHandler INSTANCE = new ActionHandler();
    private static MainModel model;

    private ActionHandler() {

    }

    public static void initialize(MainModel mainModel) {
        model = mainModel;
    }

    public static void handle(Action action) {
        switch (action) {
            case AssignCoordinator assignCoordinatorAction -> handleAssignCoordinator(assignCoordinatorAction);
            default -> throw new IllegalStateException("Unexpected action type: " + action.getClass());
        }
    }

    private static void handleAssignCoordinator(AssignCoordinator action) {
        UserModel assignedUser = action.coordinator();
        EventModel assignedEvent = action.eventModel();

        for (EventModel eventModel : model.eventModels()) {
            if (eventModel.id().get() == assignedEvent.id().get()) {
                eventModel.users().add(assignedUser);
                break;
            }
        }

        for (UserModel userModel : model.userModels()) {
            if (userModel.id().get() == assignedUser.id().get()) {
                userModel.events().add(assignedEvent);
                break;
            }
        }
    }

}
