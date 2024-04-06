package event.tickets.easv.bar.gui.component.dashboard;

import event.tickets.easv.bar.gui.common.NotificationType;
import event.tickets.easv.bar.gui.common.View;
import event.tickets.easv.bar.gui.common.ViewHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;

public class DashboardView implements View {


    @Override
    public Region getView() {
        var btn = new Button("dashboard");


        return btn;

    }
}
