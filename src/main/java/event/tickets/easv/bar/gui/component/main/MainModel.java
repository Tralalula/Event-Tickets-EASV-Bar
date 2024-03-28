package event.tickets.easv.bar.gui.component.main;

import event.tickets.easv.bar.be.Ticket;
import event.tickets.easv.bar.gui.common.EventModel;
import event.tickets.easv.bar.gui.common.TicketModel;
import event.tickets.easv.bar.gui.common.ViewHandler;
import event.tickets.easv.bar.gui.common.ViewType;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MainModel {
    private final ObservableList<EventModel> eventModels = FXCollections.observableArrayList();
    private final BooleanProperty fetchingData = new SimpleBooleanProperty(true);

    private final ObservableList<TicketModel> ticketModels = FXCollections.observableArrayList();
    private final ObjectProperty<TicketModel> ticketModelProperty = new SimpleObjectProperty<>();

    public ObservableList<TicketModel> ticketModels() {
        return ticketModels;
    }

    public ObjectProperty<TicketModel> ticketModelProperty() {
        return ticketModelProperty;
    }

    public void changeTicketView(TicketModel model) {
        this.ticketModelProperty().set(model);
        ViewHandler.changeView(ViewType.SHOW_TICKET);
    }

    public ObservableList<EventModel> eventModels() {
        return eventModels;
    }

    public BooleanProperty fetchingDataProperty() {
        return fetchingData;
    }

}
