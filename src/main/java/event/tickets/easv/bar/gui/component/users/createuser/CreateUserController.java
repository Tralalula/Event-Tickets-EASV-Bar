package event.tickets.easv.bar.gui.component.users.createuser;

import com.resend.core.exception.ResendException;
import event.tickets.easv.bar.be.User;
import event.tickets.easv.bar.be.enums.Rank;
import event.tickets.easv.bar.bll.EmailSender;
import event.tickets.easv.bar.bll.EntityManager;
import event.tickets.easv.bar.bll.cryptographic.BCrypt;
import event.tickets.easv.bar.gui.common.Action;
import event.tickets.easv.bar.gui.common.Action.CreateUser;
import event.tickets.easv.bar.gui.common.ActionHandler;
import event.tickets.easv.bar.gui.common.UserModel;
import event.tickets.easv.bar.util.FailureType;
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

    public CreateUserController(CreateUserModel model) {
        this.model = model;
        model.okToCreateProperty().bind(Bindings.createBooleanBinding(
                this::isDataValid,
                model.rankProperty(),
                model.firstNameProperty(),
                model.usernameProperty(),
                model.mailProperty()
        ));
    }

    void createUser(Runnable postTaskGuiActions) {
        Task<Result<User>> createTask = new Task<>() {
            @Override
            protected Result<User> call() {
                return createUser();
            }
        };

        createTask.setOnSucceeded(evt -> {
            postTaskGuiActions.run();

            var result = createTask.getValue();
            if (result.isSuccess()) ActionHandler.handle(new CreateUser(UserModel.fromEntity(result.get())));

            // todo: håndter failure
        });

        new Thread(createTask).start();
    }

    private Result<User> createUser() {
        String username = model.usernameProperty().get();
        String mail = model.mailProperty().get();
        Rank rank = model.rankProperty().get();
        String firstName = model.firstNameProperty().get();
        String lastName = model.lastNameProperty().get();
        String location = model.locationProperty().get();
        String phoneNumber = model.phoneNumberProperty().get();

        String password = Generator.generatePassword(8);
        var user = new User(username, mail, password, firstName, lastName, location, phoneNumber, rank);
        var result = new EntityManager().add(user);

        if (result.isFailure()) return result;


        // todo: sender ikke mail på test

/*        try {
            EmailSender emailSender = new EmailSender();
            emailSender.sendPassword(mail, firstName, username, password);
        } catch (IOException e) {
            return Failure.of(FailureType.IO_FAILURE, "CreateUserController.createUser - failed during reading prop file for sending email", e);
        } catch (ResendException e) {
            return Failure.of(FailureType.EMAIL_FAILURE, "CreateUserController.createUser - failed during sending mail");
        }*/

        return result;
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
