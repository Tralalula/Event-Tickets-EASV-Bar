package event.tickets.easv.bar.be.Ticket;

import event.tickets.easv.bar.be.Entity;
import event.tickets.easv.bar.be.Event;
import event.tickets.easv.bar.bll.EntityManager;

import java.util.ArrayList;
import java.util.List;

public class TicketEvent implements Entity<TicketEvent> {
    private int id;
    private int ticketId;
    private int eventId;
    private float price;

    private int quantity;

    private List<TicketGenerated> tickets = new ArrayList<>();
    private Event connectedEvent;

    public TicketEvent(int id, int ticketId, int eventId, float price, int quantity) {
        this.id = id;
        this.ticketId = ticketId;
        this.eventId = eventId;
        this.price = price;
        this.quantity = quantity;
    }

    public TicketEvent(int ticketId, int eventId, float price, int quantity) {
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
        if (associations.isEmpty()) return;

        Object first = associations.getFirst();
        if (first instanceof TicketGenerated) {
            this.tickets = (List<TicketGenerated>) associations;
        }
    }
}
