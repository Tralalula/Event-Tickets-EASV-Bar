package event.tickets.easv.bar.gui.component.users.createuser;

import event.tickets.easv.bar.be.User;
import event.tickets.easv.bar.bll.EntityManager;
import event.tickets.easv.bar.bll.cryptographic.BCrypt;
import event.tickets.easv.bar.gui.common.UserModel;
import event.tickets.easv.bar.util.Result;
import event.tickets.easv.bar.util.Result.Success;
import event.tickets.easv.bar.util.Result.Failure;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

public class CreateUserController {
    private final CreateUserModel model;
    private final ObservableList<UserModel> models;

    public CreateUserController(CreateUserModel model, ObservableList<UserModel> models) {
        this.model = model;
        this.models = models;
        model.okToCreateProperty().bind(Bindings.createBooleanBinding(
                this::isDataValid,
                model.rankProperty(),
                model.firstNameProperty(),
                model.usernameProperty(),
                model.mailProperty()
        ));
    }

    void createUser(Runnable postTaskGuiActions) {
        Task<Boolean> createTask = new Task<>() {
            @Override
            protected Boolean call() {
                return createUser();
            }
        };

        createTask.setOnSucceeded(evt -> {
            postTaskGuiActions.run();
            if (!createTask.getValue()) {
                System.out.println("hey");
            }
        });

        var saveThread = new Thread(createTask);
        saveThread.start();
    }

    private boolean createUser() {
        String username = model.usernameProperty().get();
        String mail = model.mailProperty().get();
        String password = psw("test");
        String imageName = "";
        User.Rank rank = model.rankProperty().get();
        String firstName = model.firstNameProperty().get();
        String lastName = model.lastNameProperty().get();
        String location = model.locationProperty().get();
        String phoneNumber = model.phoneNumberProperty().get();

        var user = new User(username, password);
        Result<User> result = new EntityManager().add(user);
        switch (result) {
            case Success<User> s -> {
                return true;
            }
            case Failure<User> f -> {
                return false;
            }
        }
    }

    private String psw(String text) {
        String salt = BCrypt.gensalt(10);
        return BCrypt.hashpw(text, salt);
    }

    private boolean isDataValid() {
        // Rank (required)
        // First name (required), last name (optional)
        // Username (required)
        // Mail (required)
        // Location (optional)
        // Phone number (optional)

        return model.rankProperty().get() != null &
                !model.firstNameProperty().get().isEmpty() &&
                !model.usernameProperty().get().isEmpty() &&
                !model.mailProperty().get().isEmpty();
    }
}
