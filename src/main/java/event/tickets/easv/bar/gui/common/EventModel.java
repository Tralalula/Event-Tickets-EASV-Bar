package event.tickets.easv.bar.gui.common;

import event.tickets.easv.bar.be.Event;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.time.LocalTime;

public class EventModel {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty title = new SimpleStringProperty();
    private final StringProperty imageName = new SimpleStringProperty();
    private final StringProperty location = new SimpleStringProperty();
    private final ObjectProperty<LocalDate> startDate = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDate> endDate = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalTime> startTime = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalTime> endTime = new SimpleObjectProperty<>();
    private final StringProperty locationGuidance = new SimpleStringProperty();
    private final StringProperty extraInfo = new SimpleStringProperty();
    private ObservableList<UserModel> users = FXCollections.observableArrayList();

    public EventModel(Event event) {
        // Don't want users here to ensure we initialize an empty ObservableList
        id.set(event.id());
        title.set(event.title());
        imageName.set(event.imageName());
        location.set(event.location());
        startDate.set(event.startDate());
        endDate.set(event.endDate());
        startTime.set(event.startTime());
        endTime.set(event.endTime());
        locationGuidance.set(event.locationGuidance());
        extraInfo.set(event.extraInfo());
    }

    public static EventModel fromEntity(Event event) {
        return new EventModel(event);
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

    public IntegerProperty id() {
        return id;
    }

    public StringProperty title() {
        return title;
    }

    public StringProperty imageName() {
        return imageName;
    }

    public StringProperty location() {
        return location;
    }

    public ObjectProperty<LocalDate> startDate() {
        return startDate;
    }

    public ObjectProperty<LocalDate> endDate() {
        return endDate;
    }

    public ObjectProperty<LocalTime> startTime() {
        return startTime;
    }

    public ObjectProperty<LocalTime> endTime() {
        return endTime;
    }

    public StringProperty locationGuidance() {
        return locationGuidance;
    }

    public StringProperty extraInfo() {
        return extraInfo;
    }

    public void setUsers(ObservableList<UserModel> users) {
        this.users = users;
    }

    public ObservableList<UserModel> users() {
        return users;
    }
}