package event.tickets.easv.bar.gui.component.events;

import event.tickets.easv.bar.gui.common.EventModel;
import event.tickets.easv.bar.gui.common.View;
import event.tickets.easv.bar.gui.common.ViewHandler;
import event.tickets.easv.bar.gui.common.ViewType;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;

public class EventsView implements View {
    private final ObservableList<EventModel> model;

    public EventsView(ObservableList<EventModel> model) {
        this.model = model;
    }


    @Override
    public Region getView() {
        var btn = new Button("events");
        btn.setOnAction(e -> ViewHandler.changeView(ViewType.SHOW_EVENT));

        var listView = new ListView<EventModel>();
        listView.setItems(model);


        return listView;
    }
}
