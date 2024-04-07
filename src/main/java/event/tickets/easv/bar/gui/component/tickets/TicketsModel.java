package event.tickets.easv.bar.gui.component.tickets;

import event.tickets.easv.bar.be.Customer;
import event.tickets.easv.bar.be.Event;
import event.tickets.easv.bar.be.Ticket.Ticket;
import event.tickets.easv.bar.be.Ticket.TicketEvent;
import event.tickets.easv.bar.be.Ticket.TicketGenerated;
import event.tickets.easv.bar.bll.EntityManager;
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
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TicketsModel {
    private final MainModel model;
    private final EntityManager entityManager;

    public TicketsModel(MainModel model) {
        this.model = model;
        this.entityManager = new EntityManager();
    }

    private boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    private boolean ticketExists(Ticket ticket) {
        for (TicketModel t : model.ticketModels())
            if (t.title().get().equals(ticket.getTitle()) && t.type().get().equals(ticket.getType()))
                return true;

        return false;
    }

    public Ticket addTicket(Ticket ticket) throws Exception {
        if (isEmpty(ticket.getTitle()))
            throw new Exception("Title cannot be empty");

        if (ticketExists(ticket))
            throw new Exception("Ticket already exists");

        // Tilføj til databasen
        Result<Ticket> result = entityManager.add(ticket);
        switch (result) {
            case Result.Success<Ticket> s -> {
                model.ticketModels().add(TicketModel.fromEntity(s.result()));
                return s.result();
            }
            case Result.Failure<Ticket> f -> throw new Exception("Error occurred while trying to add ticket\n" + f.cause());
        }
    }

    public List<TicketEvent> addToEvent(TicketModel ticketModel, MainModel main, int ticketId, int total, double price, List<EventModel> events) {
        // Laver om til ticketevent list for hver eventid.
        List<TicketEvent> newEntries = events.stream()
                .map(event -> new TicketEvent(ticketId, event.id().get(), price, total))
                .collect(Collectors.toList());

        // Tilføj alle i listen til DB
        Result<List<TicketEvent>> result = entityManager.addAll(newEntries);

        switch (result) {
            case Result.Success<List<TicketEvent>> s -> {
                    for (TicketEvent ticketEvent : s.result()) {
                        TicketEventModel created = new TicketEventModel(ticketEvent);

                        // Få fat i eventId og lav det om til eventModel, så vi kan tilføje den.
                        int eventId = created.eventId().get();
                        EventModel eventModel = events.stream()
                                .filter(event -> event.id().get() == eventId)
                                .findFirst()
                                .orElse(null);

                        if (eventModel != null)
                            created.setEvent(eventModel);

                        //Opdater hovedliste
                        main.ticketEventModels().add(created);

                        //Opdaterer nuværende view i baggrunden
                        ticketModel.ticketEvents().add(created);
                    }
            }
            case Result.Failure<List<TicketEvent>> f -> System.out.println("Error: " + f.cause());
        }

        return newEntries;
    }

    public Customer getCustomer(String email) throws Exception {
        Result<Optional<Customer>> exists = entityManager.get(Customer.class, email);
        Customer createdCustomer = null;

        if(exists.isSuccess()) {
            Optional<Customer> customer = exists.get();
            createdCustomer = customer.isEmpty() ? handleCustomer(entityManager.add(new Customer(email))) : customer.get();
        }

        return createdCustomer;
    }

    private static final String EMAIL_REGEX = "^[\\w.-]+@[a-zA-Z\\d.-]+\\.[a-zA-Z]{2,}$";
    private static final Pattern pattern = Pattern.compile(EMAIL_REGEX);

    public static boolean isValidEmail(String email) {
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    //TODO: Refactor
    public List<TicketGenerated> generateTickets(TicketEventModel ticketEvent, int amount, String email) throws Exception {
        if (ticketEvent.left().get() < amount)
            throw new Exception("Not enough tickets left");

        if (!isValidEmail(email))
            throw new Exception("Not valid email");

        List<TicketGenerated> newEntries = new ArrayList<>();
        Customer customer = getCustomer(email);

        for (int i = 0; i < amount; i++) {
            newEntries.add(new TicketGenerated(ticketEvent.id().get(), customer.id()));
        }

        Result<List<TicketGenerated>> result = entityManager.addAll(newEntries);
        handleAddGenerated(result, ticketEvent);

        return newEntries;
    }

    private Customer handleCustomer(Result<Customer> result) throws Exception {
        switch (result) {
            case Result.Success<Customer> s -> {
                return s.result();
            }
            case Result.Failure<Customer> f -> throw new Exception("Error occurred while trying to get Customer from DB");
        }
    }

    private List<TicketGenerated> handleAddGenerated(Result<List<TicketGenerated>> result, TicketEventModel ticketEvent) throws Exception {
        ObservableList<TicketGenerated> tickets = FXCollections.observableArrayList();
        switch (result) {
            case Result.Success<List<TicketGenerated>> s -> {
                for (TicketGenerated ticketGenerated : s.result()) {
                    TicketGeneratedModel ticketGeneratedModel = TicketGeneratedModel.fromEntity(ticketGenerated);

                    //Opdater hovedeliste
                    model.ticketGeneratedModels().add(ticketGeneratedModel);

                    //Opdater selve ticketEvent
                    ticketEvent.ticketsGenerated().add(ticketGeneratedModel);
                }

            }
            case Result.Failure<List<TicketGenerated>> f -> throw new Exception("Error occurred while trying to add generated to main models");
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
