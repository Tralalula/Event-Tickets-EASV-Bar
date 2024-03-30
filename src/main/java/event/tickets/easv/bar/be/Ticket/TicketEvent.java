package event.tickets.easv.bar.be.Ticket;

import event.tickets.easv.bar.be.Entity;
import event.tickets.easv.bar.be.Event;
import event.tickets.easv.bar.bll.EntityManager;
import event.tickets.easv.bar.dal.dao.TicketGeneratedDAO;
import event.tickets.easv.bar.util.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TicketEvent implements Entity<TicketEvent> {
    private int id;
    private int ticketId;
    private int eventId;
    private float price;

    private int quantity;

    private ArrayList<TicketGenerated> tickets = new ArrayList<>();
    private Event connectedEvent;

    // ???????????????
    private EntityManager entityManager;

    public TicketEvent(int id, int ticketId, int eventId, float price, int quantity) {
        entityManager = new EntityManager();

        this.id = id;
        this.ticketId = ticketId;
        this.eventId = eventId;
        this.price = price;
        this.quantity = quantity;
    }

    public TicketEvent(int id, TicketEvent ticket) {
        this(id, ticket.getTicketId(), ticket.getEventId(), ticket.getPrice(), ticket.getQuantity());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setConnectedEvent(Event event) {
        this.connectedEvent = event;
    }

    public Event getConnectedEvent() {
        return connectedEvent;
    }

    // TODO: Håndter et andet sted end i entiteten selv
    /*
    public void setTickets(int eventId) {
        if (this.tickets.isEmpty()) {
            Result<List<TicketGenerated>> result = entityManager.all(TicketGenerated.class);
            switch (result) {
                case Result.Success<List<TicketGenerated>> s -> {
                    List<TicketGenerated> allTickets = s.result();
                    List<TicketGenerated> filteredTickets = allTickets.stream()
                            .filter(ticket -> ticket.getId() == eventId)
                            .collect(Collectors.toList());

                    this.tickets.addAll(filteredTickets);
                }
                case Result.Failure<List<TicketGenerated>> f -> System.out.println("Error: " + f.cause());
            }
        }
    }*/

    public void setTickets(List<TicketGenerated> tickets) {
        this.tickets.addAll(tickets);
    }

        public List<TicketGenerated> getTickets() {
        return tickets;
    }

    public int getLeft() {
        return quantity - getTickets().size();
    }

    public int getBought() {
        return getTickets().size();
    }

    @Override
    public String toString() {
        return "TicketEvent{" +
                "id=" + id +
                ", ticketId=" + ticketId +
                ", eventId=" + eventId +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }

    @Override
    public void update(TicketEvent updatedData) {
        throw new UnsupportedOperationException("Er ikke implementeret. TicketEvent.update()");
    }

    @Override
    public int id() {
        return this.id;
    }

    @Override
    public void setAssociations(List<?> associations) {
        throw new UnsupportedOperationException("Er ikke implementeret. TicketEvent.setAssociations()");
    }
}