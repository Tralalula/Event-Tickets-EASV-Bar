package event.tickets.easv.bar.gui.common;

import event.tickets.easv.bar.gui.common.Action.*;
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
            case EditEvent a -> handleEditEvent(a);
            case DeleteEvent a -> handleDeleteEvent(a);
            case CreateUser a -> handleCreateUser(a);
            case DeleteUser a -> handleDeleteUser(a);
            case AssignCoordinator a -> handleAssignCoordinator(a);
            default -> throw new IllegalStateException("Unexpected action type: " + action.getClass());
        }
    }

    private static void handleCreateEvent(CreateEvent action) {
        model.eventModels().add(action.eventModel());
    }

    private static void handleEditEvent(EditEvent action) {
        for (EventModel eventModel : model.eventModels()) {
            if (eventModel.id().get() == action.original().id().get()) {
                eventModel.update(action.updated());
                break;
            }
        }
    }

    private static void handleDeleteEvent(DeleteEvent action) {
        Predicate<EventModel> eventModelPredicate = eventModel -> eventModel.id().get() == action.eventModel().id().get();
        Optional<EventModel> eventModel = model.eventModels().stream().filter(eventModelPredicate).findFirst();
        eventModel.ifPresent(model.eventModels()::remove);

        for (UserModel userModel : model.userModels()) {
            userModel.events().removeIf(eventModelPredicate);
        }
    }

    private static void handleCreateUser(CreateUser action) {
        model.userModels().add(action.userModel());
    }

    private static void handleDeleteUser(DeleteUser action) {
        Predicate<UserModel> userModelPredicate = userModel -> userModel.id().get() == action.userModel().id().get();
        Optional<UserModel> userModel = model.userModels().stream().filter(userModelPredicate).findFirst();
        userModel.ifPresent(model.userModels()::remove);

        for (EventModel eventModel : model.eventModels()) {
            eventModel.users().removeIf(userModelPredicate);
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
