package event.tickets.easv.bar.gui.component.events;

import atlantafx.base.controls.Card;
import event.tickets.easv.bar.gui.common.EventModel;
import event.tickets.easv.bar.gui.common.View;
import event.tickets.easv.bar.gui.common.ViewHandler;
import event.tickets.easv.bar.gui.common.ViewType;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import javafx.scene.layout.TilePane;
import javafx.util.Callback;
import org.controlsfx.control.GridCell;
import org.controlsfx.control.GridView;

public class EventsView implements View {
    private final ObservableList<EventModel> model;

    public EventsView(ObservableList<EventModel> model) {
        this.model = model;
    }

    @Override
    public Region getView() {
        var btn = new Button("events");
        btn.setOnAction(e -> ViewHandler.changeView(ViewType.SHOW_EVENT));

        var gridview = new GridView<EventModel>();


        gridview.setItems(model);

        gridview.setCellWidth(326);
        gridview.setCellHeight(370);

        gridview.setCellFactory(cell -> eventCell());
        return gridview;
    }


    private GridCell<EventModel> eventCell() {
        return new GridCell<>() {
            private final Card card = new Card();
            private final Label title = new Label();

            {
                card.setHeader(title);
            }

            @Override
            protected void updateItem(EventModel item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    title.textProperty().bind(item.title());
                    setGraphic(card);
                }
            }
        };
    }

}
