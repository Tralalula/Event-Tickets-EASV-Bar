package event.tickets.easv.bar.gui.component.main;

import event.tickets.easv.bar.gui.common.EventModel;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MainModel {
    private final ObservableList<EventModel> eventModels = FXCollections.observableArrayList();
    private final BooleanProperty fetchingData = new SimpleBooleanProperty(true);

    public ObservableList<EventModel> eventModels() {
        return eventModels;
    }
    public BooleanProperty fetchingDataProperty() {
        return fetchingData;
    }
}
