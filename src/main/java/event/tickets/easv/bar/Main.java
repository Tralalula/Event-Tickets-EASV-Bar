package event.tickets.easv.bar;

import atlantafx.base.theme.PrimerDark;
import atlantafx.base.theme.PrimerLight;
import event.tickets.easv.bar.gui.component.main.MainView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        Application.launch();
    }

    @Override
    public void start(Stage stage) {
        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());

        stage.setScene(new Scene(new MainView().getView(), 1200, 800));
        stage.show();
    }
}