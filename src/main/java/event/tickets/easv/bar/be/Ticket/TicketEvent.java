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
    private double price;

    private int quantity;

    private List<TicketGenerated> tickets = new ArrayList<>();
    private Event event;

    public TicketEvent(int id, int ticketId, int eventId, double price, int quantity) {
        this.id = id;
        this.ticketId = ticketId;
        this.eventId = eventId;
        this.price = price;
        this.quantity = quantity;
    }

    public TicketEvent(int ticketId, int eventId, double price, int quantity) {
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    public void setTickets(List<TicketGenerated> tickets) {
        this.tickets.addAll(tickets);
    }

    public List<TicketGenerated> getTickets() {
        return tickets;
    }

    public Event getEvent() {
        return event;
    }

    public int getLeft() {
        return quantity - tickets.size();
    }

    public int getBought() {
        return tickets.size();
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
        setQuantity(updatedData.getQuantity());
        setPrice(updatedData.getPrice());
    }

    @Override
    public int id() {
        return this.id;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setAssociations(List<?> associations) {
        if (associations.isEmpty()) return;

        Object first = associations.getFirst();
        if (first instanceof TicketGenerated) {
            this.tickets = (List<TicketGenerated>) associations;
        }
    }
}
