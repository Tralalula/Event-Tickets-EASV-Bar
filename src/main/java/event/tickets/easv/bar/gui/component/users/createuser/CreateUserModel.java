package event.tickets.easv.bar.gui.component.users.createuser;

import event.tickets.easv.bar.be.User;
import event.tickets.easv.bar.be.enums.Rank;
import event.tickets.easv.bar.gui.common.UserModel;
import javafx.beans.property.*;

public class CreateUserModel {

    private final BooleanProperty isCreating = new SimpleBooleanProperty(false);
    private final StringProperty viewTitle = new SimpleStringProperty("Create user");

    private final ObjectProperty<Rank> rank = new SimpleObjectProperty<>(Rank.ADMIN);
    private final StringProperty firstName = new SimpleStringProperty("");
    private final StringProperty lastName = new SimpleStringProperty("");
    private final StringProperty username = new SimpleStringProperty("");
    private final StringProperty mail = new SimpleStringProperty("");
    private final StringProperty location = new SimpleStringProperty("");
    private final StringProperty phoneNumber = new SimpleStringProperty("");

    private final BooleanProperty okToCreate = new SimpleBooleanProperty(false);

    public void set(UserModel userModel) {
        rank.set(userModel.rank().get());
        firstName.set(userModel.firstName().get());
        lastName.set(userModel.lastName().get());
        username.set(userModel.username().get());
        mail.set(userModel.mail().get());
        location.set(userModel.location().get());
        phoneNumber.set(userModel.phoneNumber().get());
    }

    public void reset() {
        rank.set(Rank.ADMIN);
        firstName.set("");
        lastName.set("");
        username.set("");
        mail.set("");
        location.set("");
        phoneNumber.set("");
    }

    public ObjectProperty<Rank> rankProperty() {
        return rank;

    }

    public BooleanProperty isCreatingProperty() {
        return isCreating;
    }

    public StringProperty viewTitleProperty() {
        return viewTitle;
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
