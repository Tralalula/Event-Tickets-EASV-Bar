package event.tickets.easv.bar.be.Ticket;

public class TicketEvent {
    private int id;
    private int ticketId;
    private int eventId;
    private float price;
    private int quantity;

    public TicketEvent(int id, int ticketId, int eventId, float price, int quantity) {
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
}
