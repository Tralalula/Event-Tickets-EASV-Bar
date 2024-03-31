package event.tickets.easv.bar.gui.component.events.createevent;

import javafx.beans.property.*;
import javafx.scene.image.Image;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

public class CreateEventModel {
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
