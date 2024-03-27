package event.tickets.easv.bar.gui.component.main;

import atlantafx.base.controls.PasswordTextField;
import atlantafx.base.theme.Styles;
import com.resend.core.exception.ResendException;
import event.tickets.easv.bar.bll.EmailSender;
import event.tickets.easv.bar.gui.common.View;
import event.tickets.easv.bar.gui.common.ViewHandler;
import event.tickets.easv.bar.gui.common.ViewType;
import event.tickets.easv.bar.gui.util.NodeUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.javafx.Icon;

import java.io.IOException;

public class AuthView implements View {
    private AuthModel model;
    private AuthController controller;

    private Region loginView;
    private Region forgotPasswordView;
    private Region verifyView;
    private Region resetPasswordView;

    private static final int MAX_WIDTH = 300;

    public AuthView() throws Exception {
        this.model = new AuthModel();

        this.controller = new AuthController(model);

        this.loginView = loginView();
        this.forgotPasswordView = forgotPasswordView();
        this.verifyView = verifyView();
        this.resetPasswordView = resetPasswordView();
    }

    @Override
    public Region getView() {
        NodeUtils.bindVisibility(loginView, ViewHandler.activeViewProperty().isEqualTo(ViewType.LOGIN));
        NodeUtils.bindVisibility(forgotPasswordView , ViewHandler.activeViewProperty().isEqualTo(ViewType.FORGOT_PASSWORD));
        NodeUtils.bindVisibility(verifyView, ViewHandler.activeViewProperty().isEqualTo(ViewType.VERIFY));
        NodeUtils.bindVisibility(resetPasswordView, ViewHandler.activeViewProperty().isEqualTo(ViewType.RESET_PASSWORD));

        return new StackPane(loginView, forgotPasswordView, verifyView, resetPasswordView);
    }

    private VBox mainBox() {
        VBox vBox = new VBox(0);
        VBox.setVgrow(vBox, Priority.ALWAYS);
        vBox.setMaxWidth(MAX_WIDTH);
        vBox.setAlignment(Pos.CENTER);

        return vBox;
    }

    private Label title(String title) {
        Label signInLabel = new Label(title);
        signInLabel.getStyleClass().add(Styles.TITLE_2);
        VBox.setMargin(signInLabel, new Insets(0, 0, 20, 0));

        return signInLabel;
    }

    private VBox textField(String name, int bottomPadding) {
        Label label = new Label(name);
        TextField textfield = new TextField();
        textfield.setPromptText(name);
        textfield.setFocusTraversable(false);
        VBox.setMargin(textfield, new Insets(0, 0, 10, 0));

        return new VBox(label, textfield);
    }

    private HBox forgotPasswordLabel() {
        Label forgotPasswordLabel = new Label("Forgot password?");
        forgotPasswordLabel.getStyleClass().addAll("actionable", "underline_hover");
        forgotPasswordLabel.setOnMouseClicked(e -> ViewHandler.changeView(ViewType.FORGOT_PASSWORD));

        HBox forgotPasswordBox = new HBox(forgotPasswordLabel);
        forgotPasswordBox.setAlignment(Pos.CENTER_RIGHT);

        return forgotPasswordBox;
    }

    private Button continueBtn(String text, Runnable method) {
        Button login = new Button(text);
        login.setMaxWidth(MAX_WIDTH);
        login.setFocusTraversable(false);
        login.setOnAction(e -> method.run());
        VBox.setMargin(login, new Insets(5, 0, 5, 0));
        return login;
    }

    private Label errLabel() {
        Label err = new Label("");
        err.getStyleClass().addAll(Styles.TEXT, Styles.DANGER, Styles.TEXT_BOLDER);
        return err;
    }

    private TextField textField(String name) {
        TextField textfield = new TextField();
        textfield.setPromptText(name);
        textfield.setFocusTraversable(false);
        VBox.setMargin(textfield, new Insets(0, 0, 10, 0));
        return textfield;
    }

    private PasswordTextField passTextField(String name) {
        PasswordTextField tf = new PasswordTextField();
        tf.setPrefWidth(250);
        tf.setFocusTraversable(false);
        tf.setPromptText(name);

        var icon = new FontIcon(Feather.EYE_OFF);
        icon.setCursor(Cursor.HAND);
        icon.setVisible(false);
        icon.setOnMouseClicked(e -> {
            icon.setIconCode(tf.getRevealPassword()
                    ? Feather.EYE_OFF : Feather.EYE
            );
            tf.setRevealPassword(!tf.getRevealPassword());
        });

        tf.textProperty().addListener((observable, oldValue, newValue) -> {
           icon.setVisible(!newValue.isEmpty());
        });

        tf.setRight(icon);
        VBox.setMargin(tf, new Insets(0, 0, 10, 0));

        return tf;
    }

    private Region loginView() {
        VBox main = mainBox();

        TextField username = textField("Username");
        PasswordTextField password = passTextField("Password");

        Label err = errLabel();

        main.getChildren().addAll(title("Sign in"),
                username,
                password,
                forgotPasswordLabel(),
                continueBtn("Log in", () -> {
                    model.usernameProperty().set(username.getText());
                    model.passwordProperty().set(password.getText());
                    try {
                        if (model.login())
                            ViewHandler.changeView(ViewType.DASHBOARD);
                    } catch (Exception e) {
                        err.setText("Forkert login");
                    }
                })
        );

        main.getChildren().add(err);
        return main;
    }

    private Region forgotPasswordView() {
        VBox main = mainBox();
        Label err = errLabel();

        TextField username = textField("Username");

        main.getChildren().addAll(
                title("Forgot your password?"),
                username,
                continueBtn("Continue", () -> {
                    model.usernameProperty().set(username.getText());
                    try {
                        if (model.userExists())
                            ViewHandler.changeView(ViewType.VERIFY);
                    } catch (Exception e) {
                        err.setText("Bruger eksisterer ikke");
                    }
                })
        );
        main.getChildren().add(err);

        return main;
    }
    private Region verifyView() {
        VBox main = mainBox();
        Label text = new Label("Didnt get the code?");
        Label resend = new Label("Resend");
        resend.getStyleClass().addAll(Styles.TEXT_UNDERLINED, "actionable");

        HBox box = new HBox(2);
        box.getChildren().addAll(text, resend);

        box.setAlignment(Pos.CENTER_LEFT);
        VBox.setMargin(box, new Insets(0, 0, 10, 0));

        main.getChildren().addAll(title("Let us know it's you"),
                textField("Code"),
                continueBtn("Continue", () -> ViewHandler.changeView(ViewType.RESET_PASSWORD)),
                box
        );
        return main;
    }
    private Region resetPasswordView() {
        VBox main = mainBox();

        Label back = new Label("Return to login");
        back.setOnMouseClicked(e -> ViewHandler.changeView(ViewType.LOGIN));
        back.getStyleClass().addAll("actionable", "underline_hover");

        HBox box = new HBox(back);
        box.setAlignment(Pos.CENTER_LEFT);
        HBox.setMargin(box, new Insets(0, 0, 5, 0));

        main.getChildren().addAll(title("Reset your password"),
                passTextField("New Password"),
                passTextField("Confirm new password"),
                continueBtn("Set password", () -> ViewHandler.changeView(ViewType.LOGIN)),
                box
        );
        return main;
    }
}
