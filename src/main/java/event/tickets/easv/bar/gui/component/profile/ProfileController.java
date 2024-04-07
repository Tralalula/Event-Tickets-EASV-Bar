package event.tickets.easv.bar.gui.component.profile;

import event.tickets.easv.bar.be.User;
import event.tickets.easv.bar.bll.EntityManager;
import event.tickets.easv.bar.gui.common.*;
import event.tickets.easv.bar.gui.common.Action.EditUser;
import event.tickets.easv.bar.gui.common.Action.SaveProfile;
import event.tickets.easv.bar.gui.util.BackgroundExecutor;
import event.tickets.easv.bar.util.Result;
import event.tickets.easv.bar.util.Result.Success;
import javafx.beans.binding.Bindings;

public class ProfileController {
    private final ProfileModel model;

    public ProfileController(ProfileModel model) {
        this.model = model;

        model.okToSaveProfile().bind(Bindings.createBooleanBinding(
                this::canSaveProfile,
                model.firstName(),
                model.lastName(),
                model.mail()
        ));

        model.okToSavePsw().bind(Bindings.createBooleanBinding(
                this::canSavePassword,
                model.password()
        ));
    }

    void onSave(Runnable postTaskGuiActions) {
        BackgroundExecutor.performBackgroundTask(
                this::saveProfile,
                postTaskGuiActions,
                success -> {
                    ActionHandler.handle(new SaveProfile(model.userModel(), success.get()));
                    ViewHandler.notify(NotificationType.SUCCESS, "Profile saved");
                },
                failure -> ViewHandler.notify(NotificationType.FAILURE, "Failed to save profile")
        );
    }

    private Result<UserModel> saveProfile() {
        String mail = model.mail().get();
        String firstname = model.firstName().get();
        String lastname = model.lastName().get();
        String location = model.location().get();
        String phoneNumber = model.phoneNumber().get();
        String imageName = model.imageName().get();

        var editedUser = new User(
                model.userModel().id().get(),
                model.userModel().username().get(),
                mail,
                firstname,
                lastname,
                location,
                phoneNumber,
                imageName,
                model.userModel().rank().get(),
                model.userModel().theme().get(),
                model.userModel().language().get(),
                model.userModel().fontSize().get()
        );

        var result = new EntityManager().update(model.userModel().toEntity(), editedUser);

        if (result.isFailure()) return result.failAs();

        var updated = UserModel.fromEntity(editedUser);
        updated.setEvents(model.userModel().events());

        return Success.of(updated);
    }

    void onChangePassword(Runnable postTaskGuiActions) {
        BackgroundExecutor.performBackgroundTask(
                this::changePassword,
                postTaskGuiActions,
                success -> ViewHandler.notify(NotificationType.SUCCESS, "Password changed"),
                failure -> ViewHandler.notify(NotificationType.FAILURE, "Failed to change password")
        );
    }

    private Result<Boolean> changePassword() {
        var tempUser = new User(model.userModel().id().get(), model.userModel().username().get(), "");
        var result = new EntityManager().resetPassword(tempUser, model.password().get());

        if (result.isFailure()) return result.failAs();

        return Success.of(true);
    }

    private boolean canSaveProfile() {
        if (model.firstName().get() == null) return false;
        if (model.lastName().get() == null) return false;
        if (model.mail().get() == null) return false;

        return !model.firstName().get().isEmpty() &&
                !model.lastName().get().isEmpty() &&
                !model.mail().get().isEmpty();
    }

    private boolean canSavePassword() {
        if (model.password().get() == null) return false;

        return !model.password().get().isEmpty();
    }
}
