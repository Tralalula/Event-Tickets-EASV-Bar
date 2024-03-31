package event.tickets.easv.bar.gui.component.users.createuser;

import event.tickets.easv.bar.be.User;
import javafx.beans.property.*;

public class CreateUserModel {
    // role?
    private final ObjectProperty<User.Rank> rank = new SimpleObjectProperty<>(User.Rank.ADMIN);
    private final StringProperty firstName = new SimpleStringProperty("");
    private final StringProperty lastName = new SimpleStringProperty("");
    private final StringProperty username = new SimpleStringProperty("");
    private final StringProperty mail = new SimpleStringProperty("");
    private final StringProperty location = new SimpleStringProperty("");
    private final StringProperty phoneNumber = new SimpleStringProperty("");

    private final BooleanProperty okToCreate = new SimpleBooleanProperty(false);

    public ObjectProperty<User.Rank> rankProperty() {
        return rank;

    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public StringProperty mailProperty() {
        return mail;
    }

    public StringProperty locationProperty() {
        return location;
    }


    public StringProperty phoneNumberProperty() {
        return phoneNumber;
    }

    public BooleanProperty okToCreateProperty() {
        return okToCreate;
    }

}
