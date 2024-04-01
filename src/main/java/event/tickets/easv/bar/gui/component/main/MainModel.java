package event.tickets.easv.bar.gui.component.main;

import event.tickets.easv.bar.gui.common.EventModel;
import event.tickets.easv.bar.gui.common.UserModel;
import event.tickets.easv.bar.gui.component.tickets.TicketEventModel;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import event.tickets.easv.bar.gui.common.TicketModel;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MainModel {
    private final ObservableList<EventModel> eventModels = FXCollections.observableArrayList();
    private final ObservableList<UserModel> userModels = FXCollections.observableArrayList();

    private final BooleanProperty fetchingEvents = new SimpleBooleanProperty(false);
    private final BooleanProperty fetchingUsers = new SimpleBooleanProperty(false);
    private final BooleanProperty fetchingTickets = new SimpleBooleanProperty(false);
    private final BooleanProperty eventsFetched = new SimpleBooleanProperty(false);
    private final BooleanProperty usersFetched = new SimpleBooleanProperty(false);
    private final BooleanProperty ticketsFetched = new SimpleBooleanProperty(false);
    private final BooleanProperty ticketEventsFetched = new SimpleBooleanProperty(false);

    private final StringProperty username = new SimpleStringProperty();


    private final ObservableList<TicketModel> ticketModels = FXCollections.observableArrayList();
    private final ObservableList<TicketEventModel> ticketEventModels = FXCollections.observableArrayList();

    public ObservableList<TicketModel> ticketModels() {
        return ticketModels;
    }

    public ObservableList<TicketEventModel> ticketEventModels() {
        return ticketEventModels;
    }

    public ObservableList<EventModel> eventModels() {
        return eventModels;
    }

    public ObservableList<UserModel> userModels() {
        return userModels;
    }

    public BooleanProperty fetchingEventsProperty() {
        return fetchingEvents;
    }

    public BooleanProperty fetchingUsersProperty() {
        return fetchingUsers;
    }

    public BooleanProperty fetchingTicketsProperty() {
        return fetchingTickets;
    }

    public BooleanProperty fetchingTicketEventsProperty() {
        return fetchingTickets;
    }


    public BooleanProperty eventsFetchedProperty() {
        return eventsFetched;
    }

    public BooleanProperty usersFetchedProperty() {
        return usersFetched;
    }

    public BooleanProperty ticketsFetchedProperty() {
        return ticketsFetched;
    }

    public BooleanProperty ticketEventsFetchedProperty() {
        return ticketEventsFetched;
    }
}
