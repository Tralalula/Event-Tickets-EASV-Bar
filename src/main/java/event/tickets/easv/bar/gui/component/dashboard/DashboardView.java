package event.tickets.easv.bar.gui.component.dashboard;

import event.tickets.easv.bar.gui.component.common.View;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;

public class DashboardView implements View {
    @Override
    public Region getView() {
        return new Button("Dashboard");
    }
}
