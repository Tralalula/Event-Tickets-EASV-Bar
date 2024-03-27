package event.tickets.easv.bar.be;

public class Ticket {

    private TicketType type;
    private String title;
    private int price;

    public enum TicketType {
        EVENT,
        PROMOTIONAL
    }

    public Ticket(TicketType type, String title, int price) {
        this.type = type;
        this.title = title;
        this.price = price;
    }

    public TicketType getType() {
        return type;
    }

    public void setType(TicketType type) {
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
}
