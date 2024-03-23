package event.tickets.easv.bar.gui.component.auth;

import event.tickets.easv.bar.gui.component.common.View;
import event.tickets.easv.bar.gui.util.ViewHandler;
import event.tickets.easv.bar.gui.util.ViewType;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;

public class LoginView implements View {
    @Override
    public Region getView() {
        var login = new Button("login");

        login.setOnAction(e -> ViewHandler.changeView(ViewType.DASHBOARD));

        return login;
    }
}
