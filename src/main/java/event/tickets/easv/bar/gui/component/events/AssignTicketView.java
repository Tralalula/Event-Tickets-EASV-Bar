package event.tickets.easv.bar.gui.component.events;

import event.tickets.easv.bar.gui.common.View;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;

public class AssignTicketView implements View {
    @Override
    public Region getView() {
        return new Button("Assign ticket view");
    }
}
