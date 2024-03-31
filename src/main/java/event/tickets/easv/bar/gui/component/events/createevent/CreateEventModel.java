package event.tickets.easv.bar.gui.component.events.createevent;

import javafx.beans.property.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

public class CreateEventModel {
    private final StringProperty eventTitle = new SimpleStringProperty("");
    private final StringProperty location = new SimpleStringProperty("");
    private final StringProperty startTime = new SimpleStringProperty("");
    private final StringProperty endTime = new SimpleStringProperty("");
    private final ObjectProperty<LocalDate> startDate = new SimpleObjectProperty<>(LocalDate.now(ZoneId.systemDefault()));
    private final ObjectProperty<LocalDate> endDate = new SimpleObjectProperty<>();
    private final StringProperty imageName = new SimpleStringProperty("");
    private final StringProperty imagePath = new SimpleStringProperty("");
    private final StringProperty extraInfo = new SimpleStringProperty("");
    private final StringProperty locationGuidance = new SimpleStringProperty("");
    private final BooleanProperty okToCreate = new SimpleBooleanProperty(false);

    public StringProperty eventTitleProperty() {
        return eventTitle;
    }


    public StringProperty locationProperty() {
        return location;
    }


    public StringProperty startTimeProperty() {
        return startTime;
    }


    public StringProperty endTimeProperty() {
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


    public BooleanProperty okToCreateProperty() {
        return okToCreate;
    }
}
