package event.tickets.easv.bar.gui.component.main;

import event.tickets.easv.bar.be.Event;
import event.tickets.easv.bar.be.Ticket.Ticket;
import event.tickets.easv.bar.bll.EntityManager;
import event.tickets.easv.bar.bll.TicketManager;
import event.tickets.easv.bar.gui.common.EventModel;
import event.tickets.easv.bar.gui.common.TicketModel;
import event.tickets.easv.bar.gui.util.BackgroundExecutor;
import event.tickets.easv.bar.util.Result;
import event.tickets.easv.bar.util.Result.Success;
import event.tickets.easv.bar.util.Result.Failure;

import java.util.ArrayList;
import java.util.List;

public class MainController {
    private final EntityManager manager;
    private final MainModel model;
    private TicketManager ticketManager;

    public MainController(MainModel model) {
        this.model = model;
        this.manager = new EntityManager();

        this.ticketManager = new TicketManager();

        fetchEvents();
        fetchTickets();
    }

    public void fetchTickets() {
        model.fetchingDataProperty().set(true);
        BackgroundExecutor.performBackgroundTask(
                () -> manager.all(Ticket.class),
                this::processTicketResult
        );
    }

    public void fetchEvents() {
        model.fetchingDataProperty().set(true);
        BackgroundExecutor.performBackgroundTask(
                () -> manager.all(Event.class),
                this::processResult
        );
    }

    private void processTicketResult(Result<List<Ticket>> result) {
        model.fetchingDataProperty().set(false);
        switch (result) {
            case Success<List<Ticket>> s -> model.ticketModels().setAll(convertToTicketModels(s.result()));
            case Failure<List<Ticket>> f -> System.out.println("Error: " + f.cause());
        }
    }

    private void processResult(Result<List<Event>> result) {
        model.fetchingDataProperty().set(false);
        switch (result) {
            case Success<List<Event>> s -> model.eventModels().setAll(convertToModels(s.result()));
            case Failure<List<Event>> f -> System.out.println("Error: " + f.cause());
        }
    }

    private List<EventModel> convertToModels(List<Event> events) {
        List<EventModel> eventModels = new ArrayList<>();

        for (var event : events) {
            eventModels.add(EventModel.fromEntity(event));
        }

        return eventModels;
    }

    private List<TicketModel> convertToTicketModels(List<Ticket> tickets) {
        List<TicketModel> ticketModels = new ArrayList<>();

        for (var ticket : tickets) {
            ticketModels.add(TicketModel.fromEntity(ticket));
        }

        return ticketModels;
    }
}