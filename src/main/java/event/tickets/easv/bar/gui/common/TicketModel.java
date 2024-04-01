package event.tickets.easv.bar.gui.common;

import event.tickets.easv.bar.be.Event;
import event.tickets.easv.bar.be.Ticket.Ticket;
import event.tickets.easv.bar.be.Ticket.TicketEvent;
import event.tickets.easv.bar.gui.component.tickets.TicketEventModel;
import javafx.beans.Observable;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;


public class TicketModel {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty title = new SimpleStringProperty();
    private final StringProperty type = new SimpleStringProperty();
    private ObservableList<TicketEventModel> ticketEvents = FXCollections.observableArrayList();

    public TicketModel() {

    }

    public TicketModel(Ticket ticket) {
        id.set(ticket.getId());
        title.set(ticket.getTitle());
        type.set(ticket.getType());
    }

    public static TicketModel fromEntity(Ticket ticket) {
        return new TicketModel(ticket);
    }

    public static TicketModel Empty() {
        return new TicketModel();
    }

    public void update(TicketModel ticketModel) {
        this.id.set(ticketModel.id.get());
        this.title.set(ticketModel.title.get());
        this.type.set(ticketModel.title.get());
        this.ticketEvents.setAll(ticketModel.ticketEvents);
    }

    public Ticket toEntity() {
        return new Ticket(
                id.get(),
                title.get(),
                type.get(),
                new ArrayList<>(ticketEvents)
        );
    }

    public IntegerProperty id() {
        return id;
    }

    public StringProperty title() {
        return title;
    }

    public StringProperty type() {
        return type;
    }

    public void setTicketEvents(ObservableList<TicketEventModel> tickets) { this.ticketEvents = tickets; }

    public ObservableList<TicketEventModel> ticketEvents() { return ticketEvents; }

}