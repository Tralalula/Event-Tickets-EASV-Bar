package event.tickets.easv.bar.gui.common;

import event.tickets.easv.bar.be.Event;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public record EventModel(IntegerProperty id, StringProperty title) {
    public static EventModel fromEntity(Event event) {
        return new EventModel(
                new SimpleIntegerProperty(event.getId()),
                new SimpleStringProperty(event.getTitle())
        );
    }

    public Event toEntity() {
        return new Event(
                id.get(),
                title.get()
        );
    }
}