package event.tickets.easv.bar.gui.component.profile;

import event.tickets.easv.bar.gui.common.UserModel;
import event.tickets.easv.bar.gui.util.ImageUtils;
import javafx.beans.property.*;
import javafx.scene.image.Image;

public class ProfileModel {
    private final StringProperty imageName = new SimpleStringProperty("");
    private final StringProperty imagePath = new SimpleStringProperty("");
    private final ObjectProperty<Image> image = new SimpleObjectProperty<>();

    private final StringProperty firstName = new SimpleStringProperty("");
    private final StringProperty lastName = new SimpleStringProperty("");
    private final StringProperty mail = new SimpleStringProperty("");
    private final StringProperty phoneNumber = new SimpleStringProperty("");
    private final StringProperty location = new SimpleStringProperty("");
    private final StringProperty password = new SimpleStringProperty("password");

    private final BooleanProperty okToSavePsw = new SimpleBooleanProperty(false);
    private final BooleanProperty okToSaveProfile = new SimpleBooleanProperty(false);

    private UserModel userModel = null;

    public void set(UserModel userModel) {
        this.userModel = userModel;

        imageName.set(userModel.imageName().get());
        imagePath.set("");
        image.set(ImageUtils.getProfileImage(userModel.id().get() + "/" + userModel.imageName().get()));
        firstName.set(userModel.firstName().get());
        lastName.set(userModel.lastName().get());
        mail.set(userModel.mail().get());
        phoneNumber.set(userModel.phoneNumber().get());
        location.set(userModel.location().get());
        password.set("password");
    }

    public void reset() {
        imageName.set("");
        imagePath.set("");
        image.set(null);
        firstName.set("");
        lastName.set("");
        mail.set("");
        phoneNumber.set("");
        location.set("");
        password.set("password");
    }

    public StringProperty imageName() {
        return imageName;
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

    public UserModel userModel() {
        return userModel;
    }
}
