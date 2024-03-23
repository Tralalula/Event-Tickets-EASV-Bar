package event.tickets.easv.bar.gui.component.events;

import event.tickets.easv.bar.gui.component.common.View;
import event.tickets.easv.bar.gui.util.ViewHandler;
import event.tickets.easv.bar.gui.util.ViewType;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;

public class EventsView implements View {
    @Override
    public Region getView() {
        var btn = new Button("events");
        btn.setOnAction(e -> ViewHandler.changeView(ViewType.SHOW_EVENT));
        return btn;
    }
}
