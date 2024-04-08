package event.tickets.easv.bar;

import atlantafx.base.theme.NordLight;
import atlantafx.base.theme.PrimerDark;
import atlantafx.base.theme.PrimerLight;
import event.tickets.easv.bar.be.User;
import event.tickets.easv.bar.bll.EntityManager;
import event.tickets.easv.bar.gui.common.ViewHandler;
import event.tickets.easv.bar.gui.common.ViewType;
import event.tickets.easv.bar.gui.component.main.MainView;
import event.tickets.easv.bar.util.Result;
import event.tickets.easv.bar.util.SessionManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Optional;

public class Main extends Application {
    public static void main(String[] args) {
        Application.launch();
    }

    @Override
    public void start(Stage stage) {
        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());


        stage.setScene(new Scene(new MainView().getView(), 1340, 940));
//        loginOnStart(); // for testing
        ViewHandler.changeView(ViewType.DASHBOARD);
//
//        stage.setMinHeight(980);
//        stage.setMinWidth(1000);

        stage.show();
    }

    /**
     * Testing purposes only
     */
    public void loginOnStart() {
        Result<User> authenticatedUser = new EntityManager().loginUser("test", "test");

        if (authenticatedUser.isFailure()) return;

        if (authenticatedUser.get() == null) return;

        Result<Optional<User>> user = new EntityManager().get(User.class, authenticatedUser.get().id());

        if (user.isPresent()) SessionManager.getInstance().login(user.get().get());
    }
}