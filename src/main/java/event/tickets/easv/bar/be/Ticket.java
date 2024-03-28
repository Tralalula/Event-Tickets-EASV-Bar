package event.tickets.easv.bar.be;

public class Ticket {

    private int id;
    //private TicketType type;
    private String title;
    private int price;
    private int categoryId;

    private String type;

    /*
    public enum TicketType {
        EVENT,
        PROMOTIONAL
    }
    */
    public Ticket(String title, String type, int categoryId) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.categoryId = categoryId;
    }

    public Ticket(int id, String title, String type, int categoryId) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.categoryId = categoryId;
    }

    public Ticket(int id, Ticket ticket) {
        this(id, ticket.getType(), ticket.getTitle(), ticket.getPrice());
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
}
