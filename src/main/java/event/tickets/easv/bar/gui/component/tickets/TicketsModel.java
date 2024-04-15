package event.tickets.easv.bar.gui.component.tickets;

import com.resend.services.emails.model.Attachment;
import event.tickets.easv.bar.be.Customer;
import event.tickets.easv.bar.be.Event;
import event.tickets.easv.bar.be.Ticket.Ticket;
import event.tickets.easv.bar.be.Ticket.TicketEvent;
import event.tickets.easv.bar.be.Ticket.TicketGenerated;
import event.tickets.easv.bar.bll.EmailSender;
import event.tickets.easv.bar.bll.EntityManager;
import event.tickets.easv.bar.bll.QRCodeManager;
import event.tickets.easv.bar.gui.common.EventModel;
import event.tickets.easv.bar.gui.common.TicketModel;
import event.tickets.easv.bar.gui.component.main.MainModel;
import event.tickets.easv.bar.util.Result;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TicketsModel {
    private final MainModel model;
    private final EntityManager entityManager;

    private EmailSender emailSender;

    public TicketsModel(MainModel model) {
        this.model = model;
        this.entityManager = new EntityManager();

        try {
            this.emailSender = new EmailSender();
        } catch (IOException e) {
            System.out.println("Error occurred...\n" + e.getMessage());
        }
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
        List<TicketEvent> newEntries = new ArrayList();

        if (events.size() > 0) {
            // Laver om til ticketevent list for hver eventid.
            newEntries = events.stream()
                    .map(event -> new TicketEvent(ticketId, event.id().get(), price, total))
                    .collect(Collectors.toList());
        } else { // må være en promotional
            newEntries.add(new TicketEvent(ticketId, 0, 0, total));
        }

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
    public boolean generateTickets(TicketModel ticketModel, TicketEventModel ticketEvent, int amount, String email) throws Exception {
        if (ticketEvent.left().get() < amount)
            throw new Exception("Not enough tickets left");

        if (!isValidEmail(email))
            throw new Exception("Not valid email");

        List<TicketGenerated> newEntries = new ArrayList<>();
        Customer customer = getCustomer(email);

        for (int i = 0; i < amount; i++) {

            String unique = Integer.toString(ticketEvent.id().get()) + customer.id() + UUID.randomUUID().toString();
            UUID uniqueUUID = QRCodeManager.generateUniqueUUID(unique);

            newEntries.add(new TicketGenerated(ticketEvent.id().get(), customer.id(), uniqueUUID.toString()));
        }

        Result<List<TicketGenerated>> result = entityManager.addAll(newEntries);
        handleAddGenerated(result, ticketEvent, email, ticketModel);

        return true;
    }

    private Customer handleCustomer(Result<Customer> result) throws Exception {
        switch (result) {
            case Result.Success<Customer> s -> {
                return s.result();
            }
            case Result.Failure<Customer> f -> throw new Exception("Error occurred while trying to get Customer from DB");
        }
    }

    private List<TicketGenerated> handleAddGenerated(Result<List<TicketGenerated>> result, TicketEventModel ticketEvent, String email, TicketModel ticketModel) throws Exception {
        ObservableList<TicketGenerated> tickets = FXCollections.observableArrayList();
        switch (result) {
            case Result.Success<List<TicketGenerated>> s -> {
                List<File> emailAttachment = new ArrayList();

                String title = ticketEvent.event().get() != null ? ticketEvent.event().get().title().get() : "Available for all events";

                for (TicketGenerated ticketGenerated : s.result()) {
                    File generate = QRCodeManager.getQrCodeFilePath(UUID.fromString(ticketGenerated.getUniqueCode()), title);
                    emailAttachment.add(generate);

                    TicketGeneratedModel ticketGeneratedModel = TicketGeneratedModel.fromEntity(ticketGenerated);

                    //Opdater hovedeliste
                    model.ticketGeneratedModels().add(ticketGeneratedModel);

                    //Opdater selve ticketEvent
                    ticketEvent.ticketsGenerated().add(ticketGeneratedModel);
                }

                emailSender.sendMultipleTickets(email, title, ticketModel.title().get(), ticketModel.type().get(), emailAttachment);
            }
            case Result.Failure<List<TicketGenerated>> f -> throw new Exception("Error occurred while trying to add generated to main models");
        }


        return tickets;
    }

    public void deleteTicket(TicketModel ticketModel) throws Exception {
        Result<Boolean> result = entityManager.delete(ticketModel.toEntity());

        if (result.isFailure())
            throw new Exception("Couldn't delete ticket from DB");

        for (TicketEventModel ticketEventModel : ticketModel.ticketEvents()) {
            model.ticketGeneratedModels().removeAll(ticketEventModel.ticketsGenerated());
        }

        model.ticketEventModels().removeAll(ticketModel.ticketEvents());
        model.ticketModels().remove(ticketModel);
    }

    public void deleteTicketEvent(TicketEventModel ticketEventModel, TicketModel ticket) throws Exception {
        Result<Boolean> result = entityManager.delete(ticketEventModel.toEntity());

        if (result.isFailure())
            throw new Exception("Couldn't delete ticket event from DB");

        model.ticketGeneratedModels().removeAll(ticketEventModel.ticketsGenerated());
        model.ticketEventModels().remove(ticketEventModel);
        ticket.ticketEvents().remove(ticketEventModel);
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

    public void addSpecialTicket(TicketModel model, MainModel main, int ticketId, int total, List<EventModel> events) throws Exception {
        Result<TicketEvent> result = entityManager.add(new TicketEvent(ticketId, 0, 0, total));
        switch (result) {
            case Result.Success<TicketEvent> s -> {
                TicketEventModel created = TicketEventModel.fromEntity(s.result());
                model.ticketEvents().add(created);
                main.ticketEventModels().add(created);
            }
            case Result.Failure<TicketEvent> f -> throw new Exception("Error occurred while trying to add special ticket");
        }
    }

    public boolean editTicket(TicketModel ticket, TicketModel updated) throws Exception {
        if (isEmpty(updated.title().get()))
            throw new Exception("Title cannot be empty");

        if (ticketExists(updated.toEntity()))
            throw new Exception("Ticket already exists");

        Result<Boolean> result = entityManager.update(ticket.toEntity(), updated.toEntity());

        if (result.isFailure())
            throw new Exception("Error occurred while trying to add special ticket");

        ticket.title().set(updated.title().get());

        return true;
    }

    public boolean editEventTicket(TicketEventModel ticket, TicketEventModel updated) throws Exception {
        Result<Boolean> result = entityManager.update(ticket.toEntity(), updated.toEntity());

        if (result.isFailure())
            throw new Exception("Error occurred while trying to add special ticket");

        ticket.price().set(updated.price().get());
        ticket.total().set(updated.total().get());

        return true;
    }
}
