package event.tickets.easv.bar.gui.component.main;

import event.tickets.easv.bar.be.User;
import event.tickets.easv.bar.bll.AuthHandler;
import event.tickets.easv.bar.bll.EntityManager;
import event.tickets.easv.bar.util.Result;
import event.tickets.easv.bar.util.SessionManager;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Optional;

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
        Result<User> authenticatedUser = new EntityManager().loginUser(username, password);

        if (authenticatedUser.isFailure()) return false;

        if (authenticatedUser.get() == null) return false;

        Result<Optional<User>> user = new EntityManager().get(User.class, authenticatedUser.get().id());

        if (user.isPresent()) {
            SessionManager.getInstance().login(user.get().get());
            return true;
        } else {
            return false;
        }

//        if (authenticatedUser != null) {
//            SessionManager.getInstance().login(authenticatedUser);
//            return true;
//        } else
//            return false;

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
