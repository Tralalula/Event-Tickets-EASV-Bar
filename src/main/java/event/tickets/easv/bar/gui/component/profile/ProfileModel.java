package event.tickets.easv.bar.gui.component.profile;

import event.tickets.easv.bar.gui.common.UserModel;
import event.tickets.easv.bar.gui.util.ImageUtils;
import javafx.beans.property.*;
import javafx.scene.image.Image;

public class ProfileModel {
    private final StringProperty imagePath = new SimpleStringProperty("");
    private final ObjectProperty<Image> image = new SimpleObjectProperty<>();

    private final StringProperty firstName = new SimpleStringProperty("");
    private final StringProperty lastName = new SimpleStringProperty("");
    private final StringProperty mail = new SimpleStringProperty("");
    private final StringProperty phoneNumber = new SimpleStringProperty("");
    private final StringProperty location = new SimpleStringProperty("");
    private final StringProperty password = new SimpleStringProperty("");

    private final BooleanProperty okToSavePsw = new SimpleBooleanProperty(false);
    private final BooleanProperty okToSaveProfile = new SimpleBooleanProperty(false);

    public void set(UserModel userModel) {
        imagePath.set("");
        image.set(ImageUtils.getProfileImage(userModel.id().get() + "/" + userModel.imageName().get()));
        firstName.set(userModel.firstName().get());
        lastName.set(userModel.lastName().get());
        mail.set(userModel.mail().get());
        phoneNumber.set(userModel.phoneNumber().get());
        location.set(userModel.location().get());
        password.set("");
    }

    public void reset() {
        imagePath.set("");
        image.set(null);
        firstName.set("");
        lastName.set("");
        mail.set("");
        phoneNumber.set("");
        location.set("");
        password.set("");
    }

    public StringProperty imagePath() {
        return imagePath;
    }

    public ObjectProperty<Image> image() {
        return image;
    }

    public StringProperty firstName() {
        return firstName;
    }

    public StringProperty lastName() {
        return lastName;
    }

    public StringProperty mail() {
        return mail;
    }

    public StringProperty phoneNumber() {
        return phoneNumber;
    }

    public StringProperty location() {
        return location;
    }

    public StringProperty password() {
        return password;
    }

    public BooleanProperty okToSavePsw() {
        return okToSavePsw;
    }

    public BooleanProperty okToSaveProfile() {
        return okToSaveProfile;
    }
}
