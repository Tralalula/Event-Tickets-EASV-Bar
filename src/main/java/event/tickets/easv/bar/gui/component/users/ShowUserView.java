package event.tickets.easv.bar.gui.component.users;

import atlantafx.base.controls.Spacer;
import atlantafx.base.theme.Styles;
import event.tickets.easv.bar.gui.common.EventModel;
import event.tickets.easv.bar.gui.common.UserModel;
import event.tickets.easv.bar.gui.common.View;
import event.tickets.easv.bar.gui.common.ViewHandler;
import event.tickets.easv.bar.gui.component.events.EventGridView;
import event.tickets.easv.bar.gui.component.events.EventsView;
import event.tickets.easv.bar.gui.util.Alerts;
import event.tickets.easv.bar.gui.util.NodeUtils;
import event.tickets.easv.bar.gui.util.StyleConfig;
import event.tickets.easv.bar.gui.widgets.CircularImageView;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2OutlinedAL;

public class ShowUserView implements View {
    private final UserModel model;
    private final DeleteUserController controller;
    private final CircularImageView circularImageView = new CircularImageView(80);
    private final EventGridView eventGridView;

    public ShowUserView(UserModel model) {
        this.model = model;
        this.controller = new DeleteUserController();
        this.eventGridView = new EventGridView(model.events());

        ViewHandler.currentViewDataProperty().subscribe((oldData, newData) -> {
            if (newData instanceof UserModel) {
                model.update((UserModel) newData);
                circularImageView.setImage(EventsView.getProfileImage(model.id().get() + "/" + model.imageName().get()));

                String firstName = model.firstName().get();
                String lastName = model.lastName().get();
                String initials;
                if (lastName == null || lastName.isEmpty()) {
                    initials = firstName.length() > 1 ? firstName.substring(0, 2) : firstName;
                } else {
                    initials = firstName.substring(0, 1) + lastName.substring(0, 1);
                }

                circularImageView.setText(initials.toUpperCase());

                eventGridView.setItems(model.events());
            }
        });
    }

    @Override
    public Region getView() {
        var results = new VBox(StyleConfig.STANDARD_SPACING);
        results.getStyleClass().addAll(StyleConfig.PADDING_DEFAULT);

        var eventGridContainer = eventGridContainer();
        VBox.setVgrow(eventGridContainer, Priority.ALWAYS);
        results.getChildren().addAll(userDataContainer(), eventGridContainer);

        return results;
    }

    private Node eventGridContainer() {
        var results = new VBox(StyleConfig.STANDARD_SPACING);
        results.getStyleClass().addAll(StyleConfig.PADDING_DEFAULT);

        var title = new Label();
        title.textProperty().bind(Bindings.createStringBinding(() -> {
            String name = model.firstName().get();
            if (name == null) return "";
            if (name.endsWith("s")) {
                return name + "' upcoming events";
            } else {
                return name + "'s upcoming events";
            }
        }, model.firstName()));
        title.getStyleClass().addAll(Styles.TITLE_3, Styles.TEXT_BOLD);

        var eventGrid = eventGridView.getView();
        VBox.setVgrow(eventGrid, Priority.ALWAYS);

        results.getChildren().addAll(title, eventGrid);

        return results;
    }

    public Node userDataContainer() {
        var results = new HBox(StyleConfig.STANDARD_SPACING);
        results.getStyleClass().addAll(Styles.BG_DEFAULT, StyleConfig.PADDING_DEFAULT, StyleConfig.ROUNDING_DEFAULT);

        results.getChildren().addAll(circularImageView.get(), userInfoBox(), new Spacer(), actionIcons());

        return results;
    }

    private Node actionIcons() {
        var results = new HBox(StyleConfig.STANDARD_SPACING);

        var editBtn = new Button(null, new FontIcon(Material2OutlinedAL.EDIT));
        editBtn.getStyleClass().addAll(Styles.BUTTON_CIRCLE, StyleConfig.ACTIONABLE, Styles.FLAT);

        var deleteBtn = new Button(null, new FontIcon(Material2OutlinedAL.DELETE));
        deleteBtn.getStyleClass().addAll(Styles.BUTTON_CIRCLE, StyleConfig.ACTIONABLE, Styles.FLAT, Styles.DANGER);

        deleteBtn.setOnAction(e -> Alerts.confirmDeleteUser(
                model,
                userModel -> controller.onDeleteUser(ViewHandler::previousView, model))
        );

        results.getChildren().addAll(editBtn, deleteBtn);

        return results;
    }

    public Node userInfoBox() {
        var results = new VBox(StyleConfig.STANDARD_SPACING * 2);

        var name = new Label();
        var rank = new Label();
        var email = new Label();
        var phone = new Label();
        var location = new Label();

        var nameRank = new VBox((double) StyleConfig.STANDARD_SPACING / 2, name, rank);
        var info = new VBox(StyleConfig.STANDARD_SPACING, email, phone, location);

        name.textProperty().bind(model.firstName().concat(" ").concat(model.lastName()));
        rank.textProperty().bind(Bindings.createObjectBinding(
                () -> switch (model.rank().get()) {
                    case null -> "";
                    case ADMIN -> "Admin";
                    case EVENT_COORDINATOR -> "Event coordinator";
                }, model.rank()));

        email.textProperty().bind(model.mail());
        phone.textProperty().bind(model.phoneNumber());
        location.textProperty().bind(model.location());

        name.getStyleClass().addAll(Styles.TEXT_BOLD, Styles.TITLE_4);
        rank.getStyleClass().addAll(Styles.TEXT_NORMAL, Styles.TEXT_MUTED);
        email.getStyleClass().addAll(Styles.TEXT_NORMAL, Styles.TEXT_MUTED);
        phone.getStyleClass().addAll(Styles.TEXT_NORMAL, Styles.TEXT_MUTED);
        location.getStyleClass().addAll(Styles.TEXT_NORMAL, Styles.TEXT_MUTED);

        var emailIcon = new FontIcon(Feather.MAIL);
        var phoneIcon = new FontIcon(Feather.PHONE);
        var locationIcon = new FontIcon(Feather.MAP_PIN);

        email.setGraphic(emailIcon);
        phone.setGraphic(phoneIcon);
        location.setGraphic(locationIcon);

        NodeUtils.bindVisibility(email, model.mail().isNotEmpty());
        NodeUtils.bindVisibility(phone, model.phoneNumber().isNotEmpty());
        NodeUtils.bindVisibility(location, model.location().isNotEmpty());

        results.getChildren().addAll(nameRank, info);

        return results;
    }
}
