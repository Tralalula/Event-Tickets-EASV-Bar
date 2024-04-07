package event.tickets.easv.bar.gui.component.profile;

import atlantafx.base.theme.Styles;
import event.tickets.easv.bar.gui.common.View;
import event.tickets.easv.bar.gui.util.BindingsUtils;
import event.tickets.easv.bar.gui.util.StyleConfig;
import event.tickets.easv.bar.gui.widgets.CircularImageView;
import event.tickets.easv.bar.gui.widgets.Labels;
import event.tickets.easv.bar.gui.widgets.TextFields;
import event.tickets.easv.bar.util.SessionManager;
import javafx.beans.property.StringProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.*;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

public class ProfileView implements View {
    private final CircularImageView circularImageView = new CircularImageView(80);

    public ProfileView() {
        circularImageView.imageProperty().bind(SessionManager.getInstance().getUserModel().image());
        circularImageView.textProperty().bind(BindingsUtils.initialize(SessionManager.getInstance().getUserModel().firstName(), SessionManager.getInstance().getUserModel().lastName()));
    }

    @Override
    public Region getView() {
        var userdata = new VBox(StyleConfig.STANDARD_SPACING);
        userdata.getStyleClass().addAll(StyleConfig.PADDING_DEFAULT);

        userdata.getChildren().addAll(
                profilePictureBox(),
                gridPaneField("First name", SessionManager.getInstance().getUserModel().firstName()),
                gridPaneField("Last name", SessionManager.getInstance().getUserModel().lastName()),
                gridPaneField("Mail", SessionManager.getInstance().getUserModel().mail()),
                gridPaneField("Phone number", SessionManager.getInstance().getUserModel().phoneNumber()),
                gridPaneField("Location", SessionManager.getInstance().getUserModel().location()),
                gridPanePassword("Password")
        );

        var saveBtn = new Button("Save");
        saveBtn.getStyleClass().addAll(Styles.ACCENT, StyleConfig.ACTIONABLE, Styles.TEXT_BOLD);
        saveBtn.setPrefHeight(40);
        saveBtn.setPrefWidth(80);

        var btnContainer = new HBox(saveBtn);
        btnContainer.setAlignment(Pos.BOTTOM_RIGHT);
        btnContainer.setPadding(new Insets(StyleConfig.STANDARD_SPACING));

        var results = new BorderPane();
        results.setCenter(userdata);
        results.setBottom(btnContainer);

        return results;
    }

    public Node profilePictureBox() {
        var results = new HBox(40);
        results.getStyleClass().addAll(Styles.BG_DEFAULT, StyleConfig.PADDING_DEFAULT, StyleConfig.ROUNDING_DEFAULT);


        var changePictureBox = new VBox(StyleConfig.STANDARD_SPACING);
        var changePictureBtn = new Button("Change profile picture", new FontIcon(Feather.CAMERA));
        changePictureBtn.getStyleClass().addAll(StyleConfig.ACTIONABLE, Styles.TEXT_BOLD);

        var changePictureLbl = Labels.styledLabel("Must be JPEG or PNG and cannot exceed 10MB.", Styles.TEXT_MUTED);
        changePictureBox.getChildren().addAll(changePictureBtn, changePictureLbl);

        changePictureBox.setAlignment(Pos.CENTER_LEFT);

        results.getChildren().addAll(circularImageView.get(), changePictureBox);

        results.setAlignment(Pos.CENTER_LEFT);

        return results;
    }

    public Node gridPaneField(String prompt, StringProperty boundProperty) {
        var results = new GridPane();
        results.getStyleClass().addAll(Styles.BG_DEFAULT, StyleConfig.PADDING_DEFAULT, StyleConfig.ROUNDING_DEFAULT);

        ColumnConstraints column1 = new ColumnConstraints();
        column1.setHgrow(Priority.NEVER);
        column1.setMinWidth(200);
        column1.setPrefWidth(Control.USE_COMPUTED_SIZE);

        ColumnConstraints column2 = new ColumnConstraints();
        column2.setHgrow(Priority.ALWAYS);
        column2.setMinWidth(350);
        column2.setMaxWidth(350);

        ColumnConstraints column3 = new ColumnConstraints();
        column3.setHgrow(Priority.ALWAYS);

        results.getColumnConstraints().addAll(column1, column2, column3);

        results.add(Labels.styledLabel(prompt, Styles.TEXT_BOLD), 0, 0);
        results.add(TextFields.boundTextField(boundProperty), 1, 0);

        return results;
    }

    public Node gridPanePassword(String prompt) {
        var results = new GridPane();
        results.getStyleClass().addAll(Styles.BG_DEFAULT, StyleConfig.PADDING_DEFAULT, StyleConfig.ROUNDING_DEFAULT);

        ColumnConstraints column1 = new ColumnConstraints();
        column1.setHgrow(Priority.NEVER);
        column1.setMinWidth(200);
        column1.setPrefWidth(Control.USE_COMPUTED_SIZE);

        ColumnConstraints column2 = new ColumnConstraints();
        column2.setHgrow(Priority.ALWAYS);
        column2.setMinWidth(350);
        column2.setMaxWidth(350);

        ColumnConstraints column3 = new ColumnConstraints();
        column3.setHgrow(Priority.ALWAYS);
        column3.setMinWidth(20);
        column3.setMaxWidth(20);

        ColumnConstraints column4 = new ColumnConstraints();
        column4.setHgrow(Priority.ALWAYS);

        results.getColumnConstraints().addAll(column1, column2, column3, column4);

        results.add(Labels.styledLabel(prompt, Styles.TEXT_BOLD), 0, 0);

        var passwordField = new PasswordField();
        passwordField.setText("password");
        passwordField.setEditable(false);

        results.add(passwordField, 1, 0);
        results.add(savePasswordBtn(passwordField), 2, 0);

        return results;
    }

    public Node savePasswordBtn(PasswordField passwordField) {
        var changePasswordButton = new Button("", new FontIcon(Feather.KEY));
        changePasswordButton.getStyleClass().addAll(Styles.BUTTON_CIRCLE, StyleConfig.ACTIONABLE, Styles.FLAT);
        changePasswordButton.setVisible(true);

        var savePasswordButton = new Button("", new FontIcon(Feather.CHECK_CIRCLE));
        savePasswordButton.getStyleClass().addAll(Styles.BUTTON_CIRCLE, StyleConfig.ACTIONABLE, Styles.FLAT);
        savePasswordButton.setVisible(false);

        changePasswordButton.setOnAction(event -> {
            passwordField.setEditable(true);
            passwordField.setText("");
            passwordField.requestFocus();
            savePasswordButton.setVisible(true);
            changePasswordButton.setVisible(false);
        });

        savePasswordButton.setOnAction(event -> {
            passwordField.setEditable(false);
            passwordField.setText("password");
            savePasswordButton.setVisible(false);
            changePasswordButton.setVisible(true);
        });

        return new StackPane(changePasswordButton, savePasswordButton);

    }
}
