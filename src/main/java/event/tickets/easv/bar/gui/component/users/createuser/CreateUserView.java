package event.tickets.easv.bar.gui.component.users.createuser;

import atlantafx.base.theme.Styles;
import event.tickets.easv.bar.be.User;
import event.tickets.easv.bar.gui.common.UserModel;
import event.tickets.easv.bar.gui.common.View;
import event.tickets.easv.bar.gui.util.StyleConfig;
import event.tickets.easv.bar.gui.widgets.Labels;
import event.tickets.easv.bar.gui.widgets.TextFields;
import javafx.collections.ObservableList;
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

    public CreateUserView(ObservableList<UserModel> models) {
        this.model = new CreateUserModel();
        this.controller = new CreateUserController(model, models);
    }

    @Override
    public Region getView() {
        var results = new VBox(StyleConfig.STANDARD_SPACING * 2);

        var title = Labels.styledLabel("Create user", Styles.TITLE_1);
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
                createSaveButton()
        );
        results.setPadding(new Insets(10));
        return results;
    }

    public Node rank() {
        var toggleGroup = new ToggleGroup();

        var adminRadio = new RadioButton("Admin");
        adminRadio.setUserData(User.Rank.ADMIN);
        adminRadio.setToggleGroup(toggleGroup);
        styleRadioButton(adminRadio);

        var coordinatorRadio = new RadioButton("Event coordinator");
        coordinatorRadio.setUserData(User.Rank.EVENT_COORDINATOR);
        coordinatorRadio.setToggleGroup(toggleGroup);
        styleRadioButton(coordinatorRadio);

        toggleGroup.selectToggle(model.rankProperty().get() == User.Rank.ADMIN ? adminRadio : coordinatorRadio);
        toggleGroup.selectedToggleProperty().addListener((obs, ov, nv) -> {
            if (nv != null) model.rankProperty().set((User.Rank) nv.getUserData());
        });

        model.rankProperty().addListener((obs, ov, nv) -> {
            if (nv == User.Rank.ADMIN) {
                toggleGroup.selectToggle(adminRadio);
            } else if (nv == User.Rank.EVENT_COORDINATOR) {
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

        // Optional: Add some spacing between the columns
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
        var saveButton = new Button("Create user");
        saveButton.disableProperty().bind(model.okToCreateProperty().not());
        saveButton.setOnAction(evt -> {
            saveButton.disableProperty().unbind();
            saveButton.setDisable(true);
            controller.createUser(() -> saveButton.disableProperty().bind(model.okToCreateProperty().not()));
        });

        saveButton.getStyleClass().addAll(StyleConfig.ACTIONABLE, Styles.ACCENT);
        saveButton.setMaxWidth(Double.MAX_VALUE);
        return saveButton;
    }
}
