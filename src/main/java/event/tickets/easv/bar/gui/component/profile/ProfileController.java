package event.tickets.easv.bar.gui.component.profile;

import event.tickets.easv.bar.gui.common.UserModel;
import event.tickets.easv.bar.util.Result;
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

    }

    private Result<Boolean> changePassword() {
        return null;
    }

    private boolean canSaveProfile() {
        return !model.firstName().get().isEmpty() &&
                !model.lastName().get().isEmpty() &&
                !model.mail().get().isEmpty();
    }

    private boolean canSavePassword() {
        return !model.password().get().isEmpty();
    }
}
