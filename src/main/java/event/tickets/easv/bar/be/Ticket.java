package event.tickets.easv.bar.be;

public class Ticket {

    private int id;
    private String title;
    private int price;
    private String type;

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
}
