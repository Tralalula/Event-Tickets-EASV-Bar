package event.tickets.easv.bar.gui.component.events;

import event.tickets.easv.bar.gui.common.EventModel;
import event.tickets.easv.bar.gui.common.View;
import event.tickets.easv.bar.gui.common.ViewHandler;
import event.tickets.easv.bar.gui.common.ViewType;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;

public class ShowEventView implements View {
    private EventModel eventModel;

    public ShowEventView() {
        ViewHandler.currentViewDataProperty().subscribe((oldData, newData) -> {
            if (newData instanceof EventModel) {
                eventModel = (EventModel) newData;
                System.out.println(eventModel.title());
            }
        });
    }

    @Override
    public Region getView() {
        var btn = new Button("show event");
        btn.setOnAction(e -> ViewHandler.changeView(ViewType.ASSIGN_TICKET));
        return btn;
    }
}
