package event.tickets.easv.bar.gui.component.users.createuser;

import com.resend.core.exception.ResendException;
import event.tickets.easv.bar.be.User;
import event.tickets.easv.bar.be.enums.Rank;
import event.tickets.easv.bar.bll.EmailSender;
import event.tickets.easv.bar.bll.EntityManager;
import event.tickets.easv.bar.bll.cryptographic.BCrypt;
import event.tickets.easv.bar.gui.common.UserModel;
import event.tickets.easv.bar.util.Generator;
import event.tickets.easv.bar.util.Result;
import event.tickets.easv.bar.util.Result.Success;
import event.tickets.easv.bar.util.Result.Failure;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import java.io.IOException;

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
        Rank rank = model.rankProperty().get();
        String firstName = model.firstNameProperty().get();
        String lastName = model.lastNameProperty().get();
        String location = model.locationProperty().get();
        String phoneNumber = model.phoneNumberProperty().get();

        String password = Generator.generatePassword(8);
        System.out.println("password: " + password);
        var user = new User(username, mail, password, firstName, lastName, location, phoneNumber, rank);
        Result<User> result = new EntityManager().add(user);
        switch (result) {
            case Success<User> s -> {
                System.out.println("lol");
//                try {
//                    EmailSender emailSender = new EmailSender();
////                    emailSender.sendPassword(mail, firstName, username, password);
//                } catch (IOException e) {
//                    System.out.println("fejl ved at læse prop fil til email sending... " + e);
//                } catch (ResendException e) {
//                    System.out.println("fejl ved at sende mail... " + e);
//                }
                return true;
            }
            case Failure<User> f -> {
                System.out.println(f);
                return false;
            }
        }
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
