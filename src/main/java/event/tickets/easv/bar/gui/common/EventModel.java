package event.tickets.easv.bar.gui.common;

import event.tickets.easv.bar.be.Event;
import javafx.beans.property.*;

import java.time.LocalDate;
import java.time.LocalTime;

public record EventModel(
        IntegerProperty id,
        StringProperty title,
        StringProperty imageName,
        StringProperty location,
        ObjectProperty<LocalDate> startDate,
        ObjectProperty<LocalDate> endDate,
        ObjectProperty<LocalTime> startTime,
        ObjectProperty<LocalTime> endTime,
        StringProperty locationGuidance,
        StringProperty extraInfo
) {
    public static EventModel fromEntity(Event event) {
        return new EventModel(
                new SimpleIntegerProperty(event.id()),
                new SimpleStringProperty(event.title()),
                new SimpleStringProperty(event.imageName()),
                new SimpleStringProperty(event.location()),
                new SimpleObjectProperty<>(event.startDate()),
                new SimpleObjectProperty<>(event.endDate()),
                new SimpleObjectProperty<>(event.startTime()),
                new SimpleObjectProperty<>(event.endTime()),
                new SimpleStringProperty(event.locationGuidance()),
                new SimpleStringProperty(event.extraInfo())
        );
    }

    public Event toEntity() {
        return new Event(
                id.get(),
                title.get(),
                imageName.get(),
                location.get(),
                startDate.get(),
                endDate.get(),
                startTime.get(),
                endTime.get(),
                locationGuidance.get(),
                extraInfo.get()
        );
    }
}