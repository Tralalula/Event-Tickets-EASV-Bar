package event.tickets.easv.bar.gui.common;

import event.tickets.easv.bar.be.Event;
import event.tickets.easv.bar.gui.component.tickets.TicketEventModel;
import event.tickets.easv.bar.gui.util.ImageUtils;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

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
    private ObservableList<TicketEventModel> tickets = FXCollections.observableArrayList();

    private final ObjectProperty<Image> image = new SimpleObjectProperty<>();

    public EventModel() {

    }

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

        image.set(ImageUtils.getImage(event.id() + "/" + event.imageName()));
    }

    public static EventModel fromEntity(Event event) {
        return new EventModel(event);
    }

    public static EventModel Empty() {
        return new EventModel();
    }

    public boolean isEmpty() {
        return this.id.get() < 0;
    }

    public void update(EventModel eventModel) {
        this.id.set(eventModel.id.get());
        this.title.set(eventModel.title.get());
        this.imageName.set(eventModel.imageName.get());
        this.location.set(eventModel.location.get());
        this.startDate.set(eventModel.startDate.get());
        this.endDate.set(eventModel.endDate.get());
        this.startTime.set(eventModel.startTime.get());
        this.endTime.set(eventModel.endTime.get());
        this.locationGuidance.set(eventModel.locationGuidance.get());
        this.extraInfo.set(eventModel.extraInfo.get());
        this.image.set(eventModel.image.get());

        this.users = eventModel.users;
        this.tickets = eventModel.tickets;
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

    public void setTickets(ObservableList<TicketEventModel> tickets) {
        this.tickets = tickets;
    }

    public ObservableList<TicketEventModel> tickets() {
        return tickets;
    }

    public ObjectProperty<Image> image() {
        return image;
    }

    @Override
    public String toString() {
        return id.get() + " " + title.get() + " " + startDate.get() + " " + startTime.get() + " " + location.get() + " " + locationGuidance.get() + " " + extraInfo.get();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        EventModel that = (EventModel) obj;
        return Objects.equals(id.get(), that.id.get());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id.get());
    }
}