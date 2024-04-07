package event.tickets.easv.bar.gui.component.profile;

import event.tickets.easv.bar.be.User;
import event.tickets.easv.bar.bll.EntityManager;
import event.tickets.easv.bar.gui.common.UserModel;
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

    }

    private Result<UserModel> editUser() {
        return null;
    }

    void onChangePassword(Runnable postTaskGuiActions) {
        BackgroundExecutor.performBackgroundTask(
                this::changePassword,
                postTaskGuiActions,
                success -> {},
                failure -> {}
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
