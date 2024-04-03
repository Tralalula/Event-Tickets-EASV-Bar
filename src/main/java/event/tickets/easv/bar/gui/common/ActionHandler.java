package event.tickets.easv.bar.gui.common;

import event.tickets.easv.bar.gui.common.Action.AssignCoordinator;
import event.tickets.easv.bar.gui.common.Action.CreateEvent;
import event.tickets.easv.bar.gui.common.Action.CreateUser;
import event.tickets.easv.bar.gui.component.main.MainModel;
import javafx.collections.ObservableList;

import java.util.Optional;
import java.util.function.Predicate;

public class ActionHandler {
    private static MainModel model;

    private ActionHandler() {

    }

    public static void initialize(MainModel mainModel) {
        model = mainModel;
    }

    public static void handle(Action action) {
        switch (action) {
            case CreateEvent a -> handleCreateEvent(a);
            case CreateUser a -> handleCreateUser(a);
            case AssignCoordinator a -> handleAssignCoordinator(a);
            default -> throw new IllegalStateException("Unexpected action type: " + action.getClass());
        }
    }

    private static void handleCreateEvent(CreateEvent action) {
        model.eventModels().add(action.eventModel());
    }

    private static void handleCreateUser(CreateUser action) {
        model.userModels().add(action.userModel());
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
