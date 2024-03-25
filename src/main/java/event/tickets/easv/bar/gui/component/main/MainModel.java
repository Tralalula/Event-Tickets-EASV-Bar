package event.tickets.easv.bar.gui.component.main;

import event.tickets.easv.bar.gui.common.EventModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MainModel {
    private static final ObservableList<EventModel> eventModels = FXCollections.observableArrayList();

    public ObservableList<EventModel> eventModels() {
        return eventModels;
    }
}
