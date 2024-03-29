package event.tickets.easv.bar.gui.component.main;

import event.tickets.easv.bar.be.Event;
import event.tickets.easv.bar.be.User;
import event.tickets.easv.bar.bll.EntityManager;
import event.tickets.easv.bar.gui.common.EventModel;
import event.tickets.easv.bar.gui.common.UserModel;
import event.tickets.easv.bar.gui.util.BackgroundExecutor;
import event.tickets.easv.bar.util.Result;
import event.tickets.easv.bar.util.Result.Success;
import event.tickets.easv.bar.util.Result.Failure;
import javafx.collections.transformation.FilteredList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class MainController {
    private final EntityManager manager;
    private final MainModel model;

    private final HashMap<Integer, List<Integer>> eventToUsersMap = new HashMap<>();
    private final HashMap<Integer, List<Integer>> userToEventsMap = new HashMap<>();

    public MainController(MainModel model) {
        this.model = model;
        this.manager = new EntityManager();

        model.eventsFetchedProperty().addListener((obs, ov, nv) -> syncAssociations());
        model.usersFetchedProperty().addListener((obs, ov, nv) -> syncAssociations());

        fetchEvents();
        fetchUsers();
    }

    private void syncAssociations() {
        // Need to make sure that both are finished processing before syncing
        if (model.eventsFetchedProperty().get() && model.usersFetchedProperty().get()) {
            for (EventModel eventModel : model.eventModels()) {
                FilteredList<UserModel> filteredUsers = new FilteredList<>(model.userModels(), userModel ->
                        eventToUsersMap.getOrDefault(eventModel.id().get(), Collections.emptyList()).contains(userModel.id().get()));
                eventModel.setUsers(filteredUsers);
            }

            for (UserModel userModel : model.userModels()) {
                FilteredList<EventModel> filteredEvents = new FilteredList<>(model.eventModels(), eventModel ->
                        userToEventsMap.getOrDefault(userModel.id().get(), Collections.emptyList()).contains(eventModel.id().get()));
                userModel.setEvents(filteredEvents);
            }

            // Clear temp storage association maps; otherwise they take up a shit ton of memory
            eventToUsersMap.clear();
            userToEventsMap.clear();
        }
    }

    public void fetchEvents() {
        model.fetchingEventsProperty().set(true);
        BackgroundExecutor.performBackgroundTask(
                () -> manager.allWithAssociations(Event.class),
                this::processEvents
        );
    }

    public void fetchUsers() {
        model.fetchingUsersProperty().set(true);
        BackgroundExecutor.performBackgroundTask(
                () -> manager.allWithAssociations(User.class),
                this::processUsers
        );
    }

    private void processEvents(Result<List<Event>> result) {
        model.fetchingEventsProperty().set(false);
        switch (result) {
            case Success<List<Event>> s -> {
                model.eventModels().setAll(convertToEventModels(s.result()));
                model.eventsFetchedProperty().set(true);
            }
            case Failure<List<Event>> f -> System.out.println("Error: " + f.cause());
        }
    }

    private void processUsers(Result<List<User>> result) {
        model.fetchingUsersProperty().set(false);
        switch (result) {
            case Success<List<User>> s -> {
                model.userModels().setAll(convertToUserModels(s.result()));
                model.usersFetchedProperty().set(true);
            }
            case Failure<List<User>> f -> System.out.println("Error: " + f.cause());
        }
    }

    private List<EventModel> convertToEventModels(List<Event> events) {
        List<EventModel> eventModels = new ArrayList<>();

        for (Event event : events) {
            eventModels.add(EventModel.fromEntity(event));
            List<Integer> userIds = event.users().stream().map(User::id).toList();
            eventToUsersMap.put(event.id(), userIds);
        }

        return eventModels;
    }

    private List<UserModel> convertToUserModels(List<User> users) {
        List<UserModel> userModels = new ArrayList<>();

        for (User user : users) {
            userModels.add(UserModel.fromEntity(user));
            List<Integer> eventIds = user.events().stream().map(Event::id).toList();
            userToEventsMap.put(user.id(), eventIds);
        }

        return userModels;
    }
}