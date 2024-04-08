package event.tickets.easv.bar.gui.component.main;

import event.tickets.easv.bar.be.Event;
import event.tickets.easv.bar.be.Ticket.TicketEvent;
import event.tickets.easv.bar.be.Ticket.TicketGenerated;
import event.tickets.easv.bar.be.User;
import event.tickets.easv.bar.be.Ticket.Ticket;
import event.tickets.easv.bar.bll.EntityManager;
import event.tickets.easv.bar.gui.common.*;
import event.tickets.easv.bar.gui.component.tickets.TicketEventModel;
import event.tickets.easv.bar.gui.component.tickets.TicketGeneratedModel;
import event.tickets.easv.bar.gui.util.BackgroundExecutor;
import event.tickets.easv.bar.util.Result;
import event.tickets.easv.bar.util.Result.Success;
import event.tickets.easv.bar.util.Result.Failure;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class MainController {
    private final EntityManager manager;
    private final MainModel model;

    private final HashMap<Integer, List<Integer>> eventToUsersMap = new HashMap<>();
    private final HashMap<Integer, List<Integer>> userToEventsMap = new HashMap<>();

    public MainController(MainModel model) {
        this.model = model;
        this.manager = new EntityManager();
        ActionHandler.initialize(model);

        model.eventsFetchedProperty().addListener((obs, ov, nv) -> syncAssociations());
        model.usersFetchedProperty().addListener((obs, ov, nv) -> syncAssociations());
        model.ticketsFetchedProperty().addListener((obs, ov, nv) -> syncAssociations());
        model.ticketEventsFetchedProperty().addListener((obs, ov, nv) -> syncAssociations());

        fetchEvents();
        fetchUsers();

        fetchTickets();
        fetchTicketEvents();
        fetchTicketsGenerated();
    }

    private void syncUsersAndEvents() {
        for (EventModel eventModel : model.eventModels()) {
            ObservableList<UserModel> associatedUsers = FXCollections.observableArrayList(
                    model.userModels().stream()
                                      .filter(userModel -> eventToUsersMap.getOrDefault(eventModel.id().get(), Collections.emptyList())
                                      .contains(userModel.id().get()))
                                      .collect(Collectors.toList())
            );
            eventModel.setUsers(associatedUsers);
        }

        for (UserModel userModel : model.userModels()) {
            ObservableList<EventModel> associatedEvents = FXCollections.observableArrayList(
                    model.eventModels().stream()
                                       .filter(eventModel -> userToEventsMap.getOrDefault(userModel.id().get(), Collections.emptyList())
                                       .contains(eventModel.id().get()))
                                       .collect(Collectors.toList())
            );
            userModel.setEvents(associatedEvents);
        }

        model.eventsUsersSynchronizedProperty().set(true);
    }

    private void syncAssociations() {
        // Need to make sure that both are finished processing before syncing
        if (model.eventsFetchedProperty().get() && model.usersFetchedProperty().get()
                && model.ticketsFetchedProperty().get() && model.ticketEventsFetchedProperty().get()
                && model.ticketsGeneratedProperty().get()) {

            syncUsersAndEvents();

            // TODO: Omskriv nedenstående for loops
            for (TicketModel ticketModel : model.ticketModels()) {
                ObservableList<TicketEventModel> list = FXCollections.observableArrayList();
                for (TicketEventModel tc : model.ticketEventModels())
                    if (ticketModel.id().get() == tc.ticketId().get())
                        list.add(tc);

                ticketModel.setTicketEvents(list);
            }

            for (TicketEventModel ticketEventModel : model.ticketEventModels()) {
                ObservableList<TicketGeneratedModel> list = FXCollections.observableArrayList();

                for (EventModel em : model.eventModels())
                    if (ticketEventModel.eventId().get() == em.id().get())
                        ticketEventModel.setEvent(em);


                for (TicketGeneratedModel tg : model.ticketGeneratedModels())
                    if (ticketEventModel.id().get() == tg.eventId().get())
                        list.add(tg);

                ticketEventModel.setTicketsGenerated(list);
            }

            for (EventModel eventModel : model.eventModels()) {
                ObservableList<TicketEventModel> associatedTicketEvents = FXCollections.observableArrayList(
                        model.ticketEventModels().stream()
                                .filter(ticketEventModel -> ticketEventModel.eventId().get() == eventModel.id().get())
                                .collect(Collectors.toList())
                );
                eventModel.setTickets(associatedTicketEvents);
            }

            model.eventsTicketsSynchronizedProperty().set(true);

            // Clear temp storage association maps; otherwise they take up a shit ton of memory
            eventToUsersMap.clear();
            userToEventsMap.clear();
        }
    }

    public void fetchTickets() {
        model.fetchingTicketsProperty().set(true);
        BackgroundExecutor.performBackgroundTask(
                () -> manager.allWithAssociations(Ticket.class),
                this::processTicketResult
        );
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

    public void fetchTicketEvents() {
        model.fetchingTicketEventsProperty().set(true);
        BackgroundExecutor.performBackgroundTask(
                () -> manager.allWithAssociations(TicketEvent.class),
                this::processTicketEventResult
        );
    }

    public void fetchTicketsGenerated() {
        model.fetchingTicketsGenerated().set(true);
        BackgroundExecutor.performBackgroundTask(
                () -> manager.allWithAssociations(TicketGenerated.class),
                this::processTicketsGenerated
        );
    }

    private void processTicketResult(Result<List<Ticket>> result) {
        model.fetchingTicketsProperty().set(false);
        switch (result) {
            case Success<List<Ticket>> s -> {
                model.ticketModels().setAll(convertToTicketModels(s.result()));
                model.ticketsFetchedProperty().set(true);
            }
            case Failure<List<Ticket>> f -> System.out.println("Error: " + f.cause());
        }
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


    private void processTicketEventResult(Result<List<TicketEvent>> result) {
        model.fetchingTicketEventsProperty().set(false);
        switch (result) {
            case Success<List<TicketEvent>> s -> {
                model.ticketEventModels().setAll(convertToTicketEventModels(s.result()));
                model.ticketEventsFetchedProperty().set(true);
            }
            case Failure<List<TicketEvent>> f -> System.out.println("Error: " + f);
        }
    }

    private void processTicketsGenerated(Result<List<TicketGenerated>> result) {
        model.fetchingTicketsGenerated().set(false);
        switch (result) {
            case Success<List<TicketGenerated>> s -> {
                model.ticketGeneratedModels().setAll(convertToTicketsGeneratedModels(s.result()));
                model.ticketsGeneratedProperty().set(true);
            }
            case Failure<List<TicketGenerated>> f -> System.out.println("Error: " + f);
        }
    }

    private void processUsers(Result<List<User>> result) {
        model.fetchingUsersProperty().set(false);
        switch (result) {
            case Success<List<User>> s -> {
                model.userModels().setAll(convertToUserModels(s.result()));
                model.usersFetchedProperty().set(true);
            }
            case Failure<List<User>> f -> System.out.println("Error: " + f);
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

    private List<TicketModel> convertToTicketModels(List<Ticket> tickets) {
        List<TicketModel> ticketModels = new ArrayList<>();

        for (var ticket : tickets)
            ticketModels.add(TicketModel.fromEntity(ticket));

        return ticketModels;
    }

    private List<TicketEventModel> convertToTicketEventModels(List<TicketEvent> tickets) {
        List<TicketEventModel> ticketModels = new ArrayList<>();

        for (var ticket : tickets)
            ticketModels.add(TicketEventModel.fromEntity(ticket));

        return ticketModels;
    }

    private List<TicketGeneratedModel> convertToTicketsGeneratedModels(List<TicketGenerated> tickets) {
        List<TicketGeneratedModel> ticketModels = new ArrayList<>();

        for (var ticket : tickets)
            ticketModels.add(TicketGeneratedModel.fromEntity(ticket));

        return ticketModels;
    }

}