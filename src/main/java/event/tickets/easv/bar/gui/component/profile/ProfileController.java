package event.tickets.easv.bar.gui.component.profile;

import event.tickets.easv.bar.be.User;
import event.tickets.easv.bar.bll.EntityManager;
import event.tickets.easv.bar.gui.common.*;
import event.tickets.easv.bar.gui.common.Action.SaveProfile;
import event.tickets.easv.bar.gui.util.BackgroundExecutor;
import event.tickets.easv.bar.util.AppConfig;
import event.tickets.easv.bar.util.FileManagementService;
import event.tickets.easv.bar.util.Result;
import event.tickets.easv.bar.util.Result.Success;
import javafx.beans.binding.Bindings;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;

import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

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

        model.firstNameValid().bind(Bindings.createBooleanBinding(
                () -> model.firstName().get() != null && model.firstName().get().length() > 1,
                model.firstName()
        ));

        model.lastNameValid().bind(Bindings.createBooleanBinding(
                () -> model.lastName().get() != null && model.lastName().get().length() > 1,
                model.lastName()
        ));

        model.mailValid().bind(Bindings.createBooleanBinding(
                () -> model.mail().get() != null && model.mail().get().length() > 1,
                model.mail()
        ));

        model.phoneNumberValid().bind(Bindings.createBooleanBinding(
                () -> model.phoneNumber().get() != null && model.phoneNumber().get().length() > 1,
                model.phoneNumber()
        ));

        model.locationValid().bind(Bindings.createBooleanBinding(
                () -> model.location().get() != null && model.location().get().length() > 1,
                model.location()
        ));
    }

    void onChangePicture() {
        var chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image files", "*.png", "*.jpg", "*.jpeg"));

        var file = chooser.showOpenDialog(null);
        if (file == null) return;

        var path = file.toURI().toString();
        model.imagePath().set(path);
        model.imageName().set(file.getName());
        model.image().set(new Image(model.imagePath().get(), true));
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

        if (!model.imagePath().get().isEmpty()) {
            var imageCopied = FileManagementService.copyImageToDir(model.imagePath().get(), AppConfig.PROFILE_TEMP_IMAGE_DIR, imageName);
            if (imageCopied.isFailure()) return imageCopied.failAs();
        }

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

        var finalDir = AppConfig.PROFILE_IMAGES_DIR2 + "\\" + editedUser.id();
        if (!model.imagePath().get().isEmpty()) {
            var fileMoved = FileManagementService.moveFile(
                    Paths.get(AppConfig.PROFILE_TEMP_IMAGE_DIR, imageName),
                    Paths.get(finalDir, imageName),
                    StandardCopyOption.REPLACE_EXISTING
            );

            if (fileMoved.isFailure()) return fileMoved.failAs();
        }

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
