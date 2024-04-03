package event.tickets.easv.bar.be.Ticket;

import event.tickets.easv.bar.be.Entity;
import event.tickets.easv.bar.be.Event;
import event.tickets.easv.bar.bll.TicketManager;

import java.util.ArrayList;
import java.util.List;

public class Ticket implements Entity<Ticket> {

    private int id, price;
    private String title, type;

    private TicketGenerated generatedTicket;
    private List<TicketEvent> ticketEvent = new ArrayList<>();

    public Ticket(String title, String type) {
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

    public Ticket(int id, String title, String type, ArrayList ticketEvent) {
        this(id, title, type);
        setAssociations(ticketEvent);
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

    public int getEventCount() {
        return ticketEvent.size();
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

    public List<TicketEvent> getTicketEvent() {
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

    @Override
    public void update(Ticket updatedData) {
        throw new UnsupportedOperationException("Er ikke implementeret. Ticket.update()");
/*        setTitle(updatedData.title);
        setPrice(updatedData.price);
        */
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
        if (first instanceof TicketEvent) {
            this.ticketEvent = (List<TicketEvent>) associations;
        }
    }
}
