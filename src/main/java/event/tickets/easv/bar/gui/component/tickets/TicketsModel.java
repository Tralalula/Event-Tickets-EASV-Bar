package event.tickets.easv.bar.gui.component.tickets;

import event.tickets.easv.bar.Main;
import event.tickets.easv.bar.be.Customer;
import event.tickets.easv.bar.be.Event;
import event.tickets.easv.bar.be.Ticket.Ticket;
import event.tickets.easv.bar.be.Ticket.TicketEvent;
import event.tickets.easv.bar.be.Ticket.TicketGenerated;
import event.tickets.easv.bar.bll.EntityManager;
import event.tickets.easv.bar.bll.TicketManager;
import event.tickets.easv.bar.gui.common.EventModel;
import event.tickets.easv.bar.gui.common.TicketModel;
import event.tickets.easv.bar.gui.component.main.MainModel;
import event.tickets.easv.bar.util.Result;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TicketsModel {
    private MainModel model;

    private EntityManager entityManager;

    public TicketsModel(MainModel model) {
        this.model = model;
        this.entityManager = new EntityManager();
    }

    public Ticket add(Ticket ticket) {
        Result<Ticket> result = entityManager.add(ticket);

        switch (result) {
            case Result.Success<Ticket> s -> {
                model.ticketModels().add(TicketModel.fromEntity(s.result()));
                return s.result();
            }
            case Result.Failure<Ticket> f -> System.out.println("Error: " + f.cause());
        }
        return null;
    }

    public List<TicketEvent> addToEvent(int ticketId, int total, double price, List<Integer> eventIds) {
        List<TicketEvent> newEntries = new ArrayList<>();

        if (!eventIds.isEmpty()) {
            for (int i : eventIds) {
                handleAddResult(entityManager.add(new TicketEvent(ticketId, i, price, total)), newEntries);
            }
        } else
            handleAddResult(entityManager.add(new TicketEvent(ticketId, 0, price, total)), newEntries);

        //TODO: Refactor - virker ikke korrekt
        for (TicketEvent ticketEvent : newEntries) {
            System.out.println(ticketEvent.getPrice());
            model.ticketEventModels().add(TicketEventModel.fromEntity(ticketEvent));
        }

        return newEntries;
    }

    private List<TicketEvent> handleAddResult(Result<TicketEvent> result, List<TicketEvent> list) {
        switch (result) {
            case Result.Success<TicketEvent> s -> list.add(s.result());
            case Result.Failure<TicketEvent> f -> System.out.println("Error: " + f.cause());
        }

        return list;
    }

    //TODO: Refactor
    public List<TicketGenerated> generateTickets(int id, int amount, String email) {
        List<TicketGenerated> newEntries = new ArrayList<>();
        Customer createdCustomer = handleCustomer(entityManager.add(new Customer(email)));

        for (int i = 0; i < amount; i++) {
            handleAddGenerated(entityManager.add(new TicketGenerated(id, createdCustomer.id())));
        }

        return newEntries;
    }

    private Customer handleCustomer(Result<Customer> result) {
        switch (result) {
            case Result.Success<Customer> s -> {
                return s.result();
            }
            case Result.Failure<Customer> f -> {
                System.out.println("Error: " + f.cause());
                return null;
            }
        }
    }

    private List<TicketGenerated> handleAddGenerated(Result<TicketGenerated> result) {
        ObservableList<TicketGenerated> tickets = FXCollections.observableArrayList();
        switch (result) {
            case Result.Success<TicketGenerated> s -> tickets.add(s.result());
            case Result.Failure<TicketGenerated> f -> System.out.println("Error: " + f.cause());
        }

        return tickets;
    }

    /** Sorts a TicketModel list to newest from integer list: 1, 2, 3, 4, 5 returns 5, 4, 3, 2, 1
     *
     * @param ObservableList<TicketModel>
     * @return sorted list
     */
    public SortedList<TicketModel> sortToNewest(ObservableList<TicketModel> list) {
        FilteredList<TicketModel> filteredList = new FilteredList<>(list);

        return new SortedList<>(filteredList,
                (ticket1, ticket2) -> Integer.compare(ticket2.id().get(), ticket1.id().get()));
    }

    public MainModel getMain() {
        return model;
    }
}
