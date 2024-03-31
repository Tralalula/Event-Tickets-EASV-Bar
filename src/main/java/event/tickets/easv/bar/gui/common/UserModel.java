package event.tickets.easv.bar.gui.common;

import event.tickets.easv.bar.be.User;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class UserModel {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty username = new SimpleStringProperty();
    private final StringProperty imageName = new SimpleStringProperty();
    private ObservableList<EventModel> events = FXCollections.observableArrayList();

    public UserModel() {}

    public UserModel(User user) {
        // Don't want events here to ensure we initialize an empty ObservableList
        id.set(user.id());
        username.set(user.getUsername());
        imageName.set(user.imageName());
    }

    public void update(UserModel userModel) {
        this.id.set(userModel.id.get());
        this.username.set(userModel.username.get());
        this.imageName.set(userModel.imageName.get());
        this.events = userModel.events;
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
                imageName.get()
        );
    }

    public IntegerProperty id() {
        return id;
    }

    public StringProperty username() {
        return username;
    }

    public StringProperty imageName() {
        return imageName;
    }

    public void setEvents(ObservableList<EventModel> events) {
        this.events = events;
    }

    public ObservableList<EventModel> events() {
        return events;
    }
}
