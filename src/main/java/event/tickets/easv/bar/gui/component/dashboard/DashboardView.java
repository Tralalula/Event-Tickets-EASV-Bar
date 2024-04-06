package event.tickets.easv.bar.gui.component.dashboard;

import event.tickets.easv.bar.gui.common.EventModel;
import event.tickets.easv.bar.gui.common.NotificationType;
import event.tickets.easv.bar.gui.common.View;
import event.tickets.easv.bar.gui.common.ViewHandler;
import event.tickets.easv.bar.gui.component.events.EventGridView;
import javafx.beans.property.BooleanProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;

public class DashboardView implements View {
    private final ObservableList<EventModel> eventsMasterList;
    private final BooleanProperty fetchingData;
    private final BooleanProperty eventsUsersSynchronized;


    public DashboardView(ObservableList<EventModel> eventsMasterList, BooleanProperty fetchingData, BooleanProperty eventsUsersSynchronized) {
        this.eventsMasterList = eventsMasterList;
        this.fetchingData = fetchingData;
        this.eventsUsersSynchronized = eventsUsersSynchronized;
    }

    @Override
    public Region getView() {
        var gridView = new EventGridView(eventsMasterList, eventsUsersSynchronized);

        return gridView.getView();
    }
}
