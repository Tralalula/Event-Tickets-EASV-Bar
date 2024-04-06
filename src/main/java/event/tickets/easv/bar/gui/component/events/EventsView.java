package event.tickets.easv.bar.gui.component.events;

import event.tickets.easv.bar.gui.common.EventModel;
import event.tickets.easv.bar.gui.common.View;
import event.tickets.easv.bar.gui.common.ViewHandler;
import event.tickets.easv.bar.gui.common.ViewType;
import event.tickets.easv.bar.gui.util.NodeUtils;
import event.tickets.easv.bar.gui.util.StyleConfig;
import event.tickets.easv.bar.gui.widgets.Buttons;
import javafx.beans.property.BooleanProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.*;
import org.kordamp.ikonli.material2.Material2AL;

public class EventsView implements View {
    private final ObservableList<EventModel> model;
    private final BooleanProperty fetchingData;
    private final BooleanProperty eventsUsersSynchronized;

    public EventsView(ObservableList<EventModel> model, BooleanProperty fetchingData, BooleanProperty eventsUsersSynchronized) {
        this.model = model;
        this.fetchingData = fetchingData;
        this.eventsUsersSynchronized = eventsUsersSynchronized;
    }

    @Override
    public Region getView() {
        var spacer = new Region();
        Button add = Buttons.actionIconButton(Material2AL.ADD, e -> ViewHandler.changeView(ViewType.CREATE_EVENT), StyleConfig.ACTIONABLE);
        HBox.setHgrow(spacer, Priority.ALWAYS);
        var header = new HBox(add, spacer);
        header.setAlignment(Pos.TOP_RIGHT);
        header.setPadding(new Insets(0, 10, 10, 10));
        ProgressIndicator progressIndicator = new ProgressIndicator();
        NodeUtils.bindVisibility(progressIndicator, fetchingData);

        var gridview = new EventGridView(model, eventsUsersSynchronized);
        var content = new StackPane(gridview.getView(), progressIndicator);

        return new VBox(header, content);
    }
}
