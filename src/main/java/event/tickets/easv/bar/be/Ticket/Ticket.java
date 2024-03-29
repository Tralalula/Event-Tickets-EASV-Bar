package event.tickets.easv.bar.be.Ticket;

public class Ticket {

    private int id, price;
    private String title, type;

    private TicketGenerated generatedTicket;
    private TicketEvent ticketEvent;

    public Ticket(String title, String type) {
        this.id = id;
        this.title = title;
        this.type = type;
    }

    public Ticket(int id, String title, String type) {
        this.id = id;
        this.title = title;
        this.type = type;
    }

    public Ticket(int id, Ticket ticket) {
        this(id, ticket.getTitle(), ticket.getType());
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setGeneratedTicket(TicketGenerated ticket) {
        this.generatedTicket = ticket;
    }

    public TicketGenerated getGeneratedTicket() {
        return generatedTicket;
    }

    public void setTicketEvent(TicketEvent ticket) {
        this.ticketEvent = ticket;
    }

    public TicketEvent getTicketEvent() {
        return ticketEvent;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", price=" + price +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
