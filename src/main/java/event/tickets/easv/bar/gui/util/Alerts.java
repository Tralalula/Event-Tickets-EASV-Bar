package event.tickets.easv.bar.gui.util;

import event.tickets.easv.bar.gui.common.EventModel;
import event.tickets.easv.bar.gui.common.UserModel;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;

import java.util.Optional;
import java.util.function.Consumer;

public class Alerts {

    public static void confirmDeleteEvent(EventModel eventModel, Consumer<EventModel> onConfirm) {
        confirmDelete(eventModel, onConfirm, "event", eventModel.title().get());
    }

    public static void confirmDeleteUser(UserModel userModel, Consumer<UserModel> onConfirm) {
        confirmDelete(userModel, onConfirm, "user", userModel.firstName().get() + " " + userModel.lastName().get());
    }

    public static <T> void confirmDelete(T entityModel, Consumer<T> onConfirm, String entityType, String entityName) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Delete " + entityType + "?");
        alert.setHeaderText("Are you sure you want to delete '" + entityName + "'?");
        alert.setContentText("This action cannot be undone.");

        var yesBtn = new ButtonType("Yes", ButtonData.YES);
        var noBtn = new ButtonType("No", ButtonData.NO);

        alert.getButtonTypes().setAll(yesBtn, noBtn);
        alert.initOwner(null);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get().getButtonData() == ButtonData.YES) {
            onConfirm.accept(entityModel);
        } else {
            alert.close();
        }
    }
}