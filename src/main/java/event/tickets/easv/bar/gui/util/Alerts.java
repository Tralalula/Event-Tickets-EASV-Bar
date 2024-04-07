package event.tickets.easv.bar.gui.util;

import event.tickets.easv.bar.gui.common.EventModel;
import event.tickets.easv.bar.gui.common.TicketModel;
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

    public static void confirmDeleteTicket(TicketModel ticketModel, Consumer<TicketModel> onConfirm) {
        confirmDelete(ticketModel, onConfirm, "ticket", ticketModel.title().get());
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

    public static <T> void confirm(String title, String header, String content, T entityModel, Consumer<T> onConfirm) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

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
