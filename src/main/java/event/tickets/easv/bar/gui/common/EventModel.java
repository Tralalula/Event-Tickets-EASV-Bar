package event.tickets.easv.bar.gui.common;

import event.tickets.easv.bar.be.Event;
import javafx.beans.property.*;

import java.time.LocalDate;
import java.time.LocalTime;

public record EventModel(
        IntegerProperty id,
        StringProperty title,
        ObjectProperty<LocalDate> startDate,
        ObjectProperty<LocalDate> endDate,
        ObjectProperty<LocalTime> startTime,
        ObjectProperty<LocalTime> endTime,
        StringProperty imageName
) {
    public static EventModel fromEntity(Event event) {
        return new EventModel(
                new SimpleIntegerProperty(event.getId()),
                new SimpleStringProperty(event.getTitle()),
                new SimpleObjectProperty<>(event.getStartDate()),
                new SimpleObjectProperty<>(event.getEndDate()),
                new SimpleObjectProperty<>(event.getStartTime()),
                new SimpleObjectProperty<>(event.getEndTime()),
                new SimpleStringProperty(event.getImageName())
        );
    }

    public Event toEntity() {
        return new Event(
                id.get(),
                title.get(),
                startDate.get(),
                endDate.get(),
                startTime.get(),
                endTime.get(),
                imageName.get()
        );
    }
}