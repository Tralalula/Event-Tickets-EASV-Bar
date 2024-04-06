package event.tickets.easv.bar.gui.component.events.createevent;

import event.tickets.easv.bar.gui.common.EventModel;
import event.tickets.easv.bar.gui.util.ImageUtils;
import javafx.beans.property.*;
import javafx.scene.image.Image;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

public class CreateEventModel {
    private final BooleanProperty isCreating = new SimpleBooleanProperty(true);
    private final StringProperty viewTitle = new SimpleStringProperty("Create event");

    private final StringProperty eventTitle = new SimpleStringProperty("");
    private final StringProperty location = new SimpleStringProperty("");
    private final ObjectProperty<LocalTime> startTime = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalTime> endTime = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDate> startDate = new SimpleObjectProperty<>(LocalDate.now(ZoneId.systemDefault()));
    private final ObjectProperty<LocalDate> endDate = new SimpleObjectProperty<>();
    private final StringProperty imageName = new SimpleStringProperty("");
    private final StringProperty extraInfo = new SimpleStringProperty("");
    private final StringProperty locationGuidance = new SimpleStringProperty("");

    private final StringProperty imagePath = new SimpleStringProperty("");
    private final ObjectProperty<Image> image = new SimpleObjectProperty<>();
    private final BooleanProperty okToCreate = new SimpleBooleanProperty(false);

    public void set(EventModel eventModel) {
        eventTitle.set(eventModel.title().get());
        location.set(eventModel.location().get());
        startTime.set(eventModel.startTime().get());
        endTime.set(eventModel.endTime().get());
        startDate.set(eventModel.startDate().get());
        endDate.set(eventModel.endDate().get());
        imageName.set(eventModel.imageName().get());
        extraInfo.set(eventModel.extraInfo().get());
        locationGuidance.set(eventModel.locationGuidance().get());
        imagePath.set("");
        image.set(ImageUtils.getImage(eventModel.id().get() + "/" + eventModel.imageName().get()));
    }

    public void reset() {
        eventTitle.set("");
        location.set("");
        startTime.set(null);
        endTime.set(null);
        startDate.set(LocalDate.now(ZoneId.systemDefault()));
        endDate.set(null);
        imageName.set("");
        extraInfo.set("");
        locationGuidance.set("");
        imagePath.set("");
        image.set(null);
    }

    public BooleanProperty isCreatingProperty() {
        return isCreating;
    }

    public StringProperty viewTitleProperty() {
        return viewTitle;
    }

    public StringProperty eventTitleProperty() {
        return eventTitle;
    }


    public StringProperty locationProperty() {
        return location;
    }


    public ObjectProperty<LocalTime> startTimeProperty() {
        return startTime;
    }


    public ObjectProperty<LocalTime> endTimeProperty() {
        return endTime;
    }

    public ObjectProperty<LocalDate> startDateProperty() {
        return startDate;
    }


    public ObjectProperty<LocalDate> endDateProperty() {
        return endDate;
    }


    public StringProperty imageNameProperty() {
        return imageName;
    }


    public StringProperty imagePathProperty() {
        return imagePath;
    }


    public StringProperty extraInfoProperty() {
        return extraInfo;
    }



    public StringProperty locationGuidanceProperty() {
        return locationGuidance;
    }


    public ObjectProperty<Image> imageProperty() {
        return image;
    }

    public BooleanProperty okToCreateProperty() {
        return okToCreate;
    }
}
