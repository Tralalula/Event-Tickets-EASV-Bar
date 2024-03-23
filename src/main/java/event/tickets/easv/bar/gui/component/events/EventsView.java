package event.tickets.easv.bar.gui.component.events;

import event.tickets.easv.bar.gui.component.common.View;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;

public class EventsView implements View {
    @Override
    public Region getView() {
        return new Button("events");
    }
}
