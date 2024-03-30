package event.tickets.easv.bar.gui.common;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

// kun til test for at få test tickets ind i ShowEventView
public class TestModel {
    public final StringProperty title = new SimpleStringProperty("VIP");
    public final StringProperty type = new SimpleStringProperty("Paid");
    public final StringProperty quantity = new SimpleStringProperty("250");
    public final StringProperty price = new SimpleStringProperty("DKK 250,-");

    public TestModel(String title, String type, String quantity, String price) {
        this.title.set(title);
        this.type.set(type);
        this.quantity.set(quantity);
        this.price.set(price);
    }
}
