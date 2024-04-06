package event.tickets.easv.bar.gui.component.users.createuser;

import event.tickets.easv.bar.be.User;
import event.tickets.easv.bar.be.enums.Rank;
import event.tickets.easv.bar.bll.EntityManager;
import event.tickets.easv.bar.gui.common.Action.CreateUser;
import event.tickets.easv.bar.gui.common.Action.EditUser;
import event.tickets.easv.bar.gui.common.ActionHandler;
import event.tickets.easv.bar.gui.common.UserModel;
import event.tickets.easv.bar.gui.common.ViewHandler;
import event.tickets.easv.bar.gui.util.BackgroundExecutor;
import event.tickets.easv.bar.util.Generator;
import event.tickets.easv.bar.util.Result;
import event.tickets.easv.bar.util.Result.Success;
import javafx.beans.binding.Bindings;

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

    void onCreateUser(Runnable postTaskGuiActions) {
        BackgroundExecutor.performBackgroundTask(
                this::createUser,
                postTaskGuiActions,
                success -> ActionHandler.handle(new CreateUser(UserModel.fromEntity(success.get()))),
                failure -> System.out.println("Fejl: " + failure)
        );
    }

    void onEditUser(Runnable postTaskGuiActions, UserModel userModel) {
        BackgroundExecutor.performBackgroundTask(
                () -> editUser(userModel),
                postTaskGuiActions,
                success -> {
                    ActionHandler.handle(new EditUser(userModel, success.get()));
                    ViewHandler.currentViewDataProperty().set(success.get()); // need a better solution than this, but it works for now
                },
                System.out::println
        );
    }

    private Result<UserModel> editUser(UserModel userModel) {
        String username = model.usernameProperty().get();
        String mail = model.mailProperty().get();
        Rank rank = model.rankProperty().get();
        String firstName = model.firstNameProperty().get();
        String lastName = model.lastNameProperty().get();
        String location = model.locationProperty().get();
        String phoneNumber = model.phoneNumberProperty().get();

        var editedUser = new User(
                userModel.id().get(),
                username,
                mail,
                firstName,
                lastName,
                location,
                phoneNumber,
                userModel.imageName().get(),
                rank, userModel.theme().get(),
                userModel.language().get(),
                userModel.fontSize().get()
        );

        var result = new EntityManager().update(userModel.toEntity(), editedUser);

        if (result.isFailure()) return result.failAs();

        var updated = UserModel.fromEntity(editedUser);

        return Success.of(updated);
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
