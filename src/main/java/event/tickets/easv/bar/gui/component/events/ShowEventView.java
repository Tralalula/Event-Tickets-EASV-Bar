package event.tickets.easv.bar.gui.component.events;

import event.tickets.easv.bar.gui.common.View;
import event.tickets.easv.bar.gui.common.ViewHandler;
import event.tickets.easv.bar.gui.common.ViewType;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;

public class ShowEventView implements View {
    @Override
    public Region getView() {
        var btn = new Button("show event");
        btn.setOnAction(e -> ViewHandler.changeView(ViewType.ASSIGN_TICKET));
        return btn;
    }
}
