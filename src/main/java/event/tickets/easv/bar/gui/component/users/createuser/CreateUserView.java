package event.tickets.easv.bar.gui.component.users.createuser;

import atlantafx.base.theme.Styles;
import event.tickets.easv.bar.be.enums.Rank;
import event.tickets.easv.bar.gui.common.UserModel;
import event.tickets.easv.bar.gui.common.View;
import event.tickets.easv.bar.gui.common.ViewHandler;
import event.tickets.easv.bar.gui.common.ViewType;
import event.tickets.easv.bar.gui.util.NodeUtils;
import event.tickets.easv.bar.gui.util.StyleConfig;
import event.tickets.easv.bar.gui.widgets.Labels;
import event.tickets.easv.bar.gui.widgets.TextFields;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.*;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;
import static javafx.scene.layout.Region.USE_PREF_SIZE;

public class CreateUserView implements View {
    private final CreateUserModel model;
    private final CreateUserController controller;
    private final UserModel userToBeEdited = UserModel.Empty();

    public CreateUserView() {
        this.model = new CreateUserModel();
        this.controller = new CreateUserController(model);

        ViewHandler.activeViewProperty().subscribe((oldView, newView) -> {
            if (newView == ViewType.CREATE_USER) {
                model.reset();
                model.viewTitleProperty().set("Create user");
                model.isCreatingProperty().set(true);
            }

            if (newView == ViewType.EDIT_USER) {
                Object data = ViewHandler.currentViewDataProperty().get();
                if (data instanceof UserModel) {
                    userToBeEdited.update((UserModel) data);
                    model.set(userToBeEdited);
                    model.isCreatingProperty().set(false);
                }
                model.viewTitleProperty().set("Edit user");
            }
        });
    }

    @Override
    public Region getView() {
        var results = new VBox(StyleConfig.STANDARD_SPACING * 2);

        var title = Labels.styledLabel(model.viewTitleProperty(), Styles.TITLE_1);
        title.setAlignment(Pos.CENTER);
        title.setMaxWidth(Double.MAX_VALUE);

        results.getChildren().addAll(
                title,
                rank(),
                name(),
                TextFields.promptedTextField("Username", model.usernameProperty()),
                TextFields.promptedTextField("Mail", model.mailProperty()),
                TextFields.promptedTextField("Location", model.locationProperty()),
                TextFields.promptedTextField("Phone number", model.phoneNumberProperty()),
                buttons()
        );
        results.setPadding(new Insets(10));
        return results;
    }

    public Node rank() {
        var toggleGroup = new ToggleGroup();

        var adminRadio = new RadioButton("Admin");
        adminRadio.setUserData(Rank.ADMIN);
        adminRadio.setToggleGroup(toggleGroup);
        styleRadioButton(adminRadio);

        var coordinatorRadio = new RadioButton("Event coordinator");
        coordinatorRadio.setUserData(Rank.EVENT_COORDINATOR);
        coordinatorRadio.setToggleGroup(toggleGroup);
        styleRadioButton(coordinatorRadio);

        toggleGroup.selectToggle(model.rankProperty().get() == Rank.ADMIN ? adminRadio : coordinatorRadio);
        toggleGroup.selectedToggleProperty().addListener((obs, ov, nv) -> {
            if (nv != null) model.rankProperty().set((Rank) nv.getUserData());
        });

        model.rankProperty().addListener((obs, ov, nv) -> {
            if (nv == Rank.ADMIN) {
                toggleGroup.selectToggle(adminRadio);
            } else if (nv == Rank.EVENT_COORDINATOR) {
                toggleGroup.selectToggle(coordinatorRadio);
            }
        });

        var gridPane = new GridPane();
        gridPane.setMaxWidth(Double.MAX_VALUE);
        gridPane.getColumnConstraints().add(new ColumnConstraints(USE_COMPUTED_SIZE, USE_PREF_SIZE, Double.MAX_VALUE, Priority.ALWAYS, HPos.LEFT, true));
        gridPane.getColumnConstraints().add(new ColumnConstraints(USE_COMPUTED_SIZE, USE_PREF_SIZE, Double.MAX_VALUE, Priority.ALWAYS, HPos.LEFT, true));

        GridPane.setHgrow(adminRadio, Priority.ALWAYS);
        GridPane.setHgrow(coordinatorRadio, Priority.ALWAYS);

        gridPane.add(adminRadio, 0, 0);
        gridPane.add(coordinatorRadio, 1, 0);

        gridPane.setHgap(StyleConfig.STANDARD_SPACING);

        return gridPane;
    }

    private void styleRadioButton(RadioButton radioButton) {
        radioButton.setMaxWidth(Double.MAX_VALUE);
        radioButton.setMaxHeight(Double.MAX_VALUE);

        radioButton.setStyle("-fx-padding: 8; " +
                "-fx-background-color: #FFFFFF; " +
                "-fx-background-radius: 5; " +
                "-fx-border-radius: 5; " +
                "-fx-border-width: 0; " +
                "-fx-border-color: #CCCCCC;");
    }

    private Node name() {
        var field1 = TextFields.promptedTextField("First name", model.firstNameProperty());
        var field2 = TextFields.promptedTextField("Last name", model.lastNameProperty());
        var results = new HBox(StyleConfig.STANDARD_SPACING, field1, field2);
        results.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(field1, Priority.ALWAYS);
        HBox.setHgrow(field2, Priority.ALWAYS);
        return results;
    }

    private Node createSaveButton() {
        var saveButton = new Button("");
        saveButton.textProperty().bind(model.viewTitleProperty());

        saveButton.disableProperty().bind(model.okToCreateProperty().not());
        saveButton.setOnAction(evt -> {
            saveButton.disableProperty().unbind();
            saveButton.setDisable(true);

            if (model.isCreatingProperty().get()) {
                controller.onCreateUser(() -> {
                    saveButton.disableProperty().bind(model.okToCreateProperty().not());
                    ViewHandler.previousView();
                });
            } else {
                controller.onEditUser(() -> {
                    saveButton.disableProperty().bind(model.okToCreateProperty().not());
                    ViewHandler.previousView();
                }, userToBeEdited);
            }
        });

        saveButton.getStyleClass().addAll(StyleConfig.ACTIONABLE, Styles.ACCENT);
        saveButton.setMaxWidth(Double.MAX_VALUE);
        return saveButton;
    }

    private Node resetPassword() {
        var resetPasswordButton = new Button("Reset password");
        resetPasswordButton.setOnAction(evt -> {
            controller.onResetPassword();
        });

        resetPasswordButton.getStyleClass().addAll(StyleConfig.ACTIONABLE, Styles.DANGER);
        resetPasswordButton.setMaxWidth(Double.MAX_VALUE);
        return resetPasswordButton;
    }

    public Node buttons() {
        var resetPswBtn = resetPassword();
        var saveBtn = createSaveButton();

        HBox.setHgrow(resetPswBtn, Priority.ALWAYS);
        HBox.setHgrow(saveBtn, Priority.ALWAYS);

        NodeUtils.bindVisibility(resetPswBtn, model.isCreatingProperty().not());

        HBox hbox = new HBox(StyleConfig.STANDARD_SPACING, resetPswBtn, saveBtn);
        hbox.setAlignment(Pos.CENTER);
        hbox.setMaxWidth(Double.MAX_VALUE);

        return hbox;
    }
}
