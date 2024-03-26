package event.tickets.easv.bar.gui.component.auth;

import event.tickets.easv.bar.be.User;
import event.tickets.easv.bar.bll.AuthHandler;
import event.tickets.easv.bar.util.SessionManager;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AuthModel {

    private AuthHandler authHandler;

    private BooleanProperty login = new SimpleBooleanProperty();
    private StringProperty username = new SimpleStringProperty();
    private StringProperty password = new SimpleStringProperty();

    public AuthModel() throws Exception {
        this.authHandler = new AuthHandler();
    }

    public boolean login() throws Exception {
        String username = usernameProperty().get();
        String password = passwordProperty().get();

        // Authenticate brugeren
        User authenticatedUser = authHandler.loginUser(new User(username, password));

        if (authenticatedUser != null) {
            SessionManager.getInstance().login(authenticatedUser);
            return true;
        } else
            return false;

    }

    public boolean userExists() throws Exception {
        return authHandler.userExists(usernameProperty().get());
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public StringProperty passwordProperty() {
        return password;
    }

}
