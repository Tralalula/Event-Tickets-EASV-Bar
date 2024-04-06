package event.tickets.easv.bar.gui.common;

import event.tickets.easv.bar.be.User;
import event.tickets.easv.bar.be.enums.Language;
import event.tickets.easv.bar.be.enums.Rank;
import event.tickets.easv.bar.be.enums.Theme;
import event.tickets.easv.bar.gui.util.ImageUtils;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;

public class UserModel {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty username = new SimpleStringProperty();
    private final StringProperty mail = new SimpleStringProperty();
    private final StringProperty firstName = new SimpleStringProperty();
    private final StringProperty lastName = new SimpleStringProperty();
    private final StringProperty location = new SimpleStringProperty();
    private final StringProperty phoneNumber = new SimpleStringProperty();
    private final StringProperty imageName = new SimpleStringProperty();
    private final ObjectProperty<Rank> rank = new SimpleObjectProperty<>();
    private final ObjectProperty<Theme> theme = new SimpleObjectProperty<>();
    private final ObjectProperty<Language> language = new SimpleObjectProperty<>();
    private final IntegerProperty fontSize = new SimpleIntegerProperty();

    private ObservableList<EventModel> events = FXCollections.observableArrayList();

    private final ObjectProperty<Image> image = new SimpleObjectProperty<>();

    public UserModel() {}

    public UserModel(User user) {
        // Don't want events here to ensure we initialize an empty ObservableList
        id.set(user.id());
        username.set(user.username());
        mail.set(user.mail());
        firstName.set(user.firstName());
        lastName.set(user.lastName());
        location.set(user.location());
        phoneNumber.set(user.phoneNumber());
        imageName.set(user.imageName());
        rank.set(user.rank());
        theme.set(user.theme());
        language.set(user.language());
        fontSize.set(user.fontSize());

        image.set(ImageUtils.getProfileImage(user.id() + "/" + user.imageName()));
    }

    public void update(UserModel userModel) {
        this.id.set(userModel.id.get());
        this.username.set(userModel.username.get());
        this.mail.set(userModel.mail.get());
        this.firstName.set(userModel.firstName.get());
        this.lastName.set(userModel.lastName.get());
        this.location.set(userModel.location.get());
        this.phoneNumber.set(userModel.phoneNumber.get());
        this.imageName.set(userModel.imageName.get());
        this.rank.set(userModel.rank.get());
        this.theme.set(userModel.theme.get());
        this.language.set(userModel.language.get());
        this.fontSize.set(userModel.fontSize.get());

        this.events = userModel.events;

        this.image.set(userModel.image.get());
    }


    public static UserModel Empty() {
        return new UserModel();
    }

    public static UserModel fromEntity(User user) {
        return new UserModel(user);
    }

    public User toEntity() {
        return new User(
                id.get(),
                username.get(),
                mail.get(),
                firstName.get(),
                lastName.get(),
                location.get(),
                phoneNumber.get(),
                imageName.get(),
                rank.get(),
                theme.get(),
                language.get(),
                fontSize.get()
        );
    }

    public IntegerProperty id() {
        return id;
    }

    public StringProperty username() {
        return username;
    }

    public StringProperty mail() {
        return mail;
    }

    public StringProperty firstName() {
        return firstName;
    }

    public StringProperty lastName() {
        return lastName;
    }

    public StringProperty location() {
        return location;
    }

    public StringProperty phoneNumber() {
        return phoneNumber;
    }

    public StringProperty imageName() {
        return imageName;
    }

    public ObjectProperty<Rank> rank() {
        return rank;
    }

    public ObjectProperty<Theme> theme() {
        return theme;
    }

    public ObjectProperty<Language> language() {
        return language;
    }

    public IntegerProperty fontSize() {
        return fontSize;
    }

    public void setEvents(ObservableList<EventModel> events) {
        this.events = events;
    }

    public ObservableList<EventModel> events() {
        return events;
    }

    public ObjectProperty<Image> image() {
        return image;
    }
}
