package event.tickets.easv.bar.be;

public class Ticket {

    private int id;
    private String title;
    private int price;
    private String type;

    private int categoryId;
    private String categoryName;

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

    public Ticket(int id, String title, String classification, int categoryId, String categoryName) {
        this.id = id;
        this.title = title;
        this.type = classification;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
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

    public int getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
