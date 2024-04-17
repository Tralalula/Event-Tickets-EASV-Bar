package event.tickets.easv.bar.gui.component.tickets;

import event.tickets.easv.bar.be.Event;
import event.tickets.easv.bar.be.Ticket.Ticket;
import event.tickets.easv.bar.be.Ticket.TicketEvent;
import event.tickets.easv.bar.gui.common.EventModel;
import event.tickets.easv.bar.gui.common.TestModel;
import event.tickets.easv.bar.gui.common.TicketModel;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class TicketEventModel {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final IntegerProperty ticketId = new SimpleIntegerProperty();
    private final IntegerProperty eventId = new SimpleIntegerProperty();
    private final IntegerProperty total = new SimpleIntegerProperty();
    private final IntegerProperty left = new SimpleIntegerProperty();
    private final IntegerProperty bought = new SimpleIntegerProperty();
    private final DoubleProperty price = new SimpleDoubleProperty();

    private SimpleObjectProperty<EventModel> event = new SimpleObjectProperty<>();
    private ObservableList<TicketGeneratedModel> ticketsGenerated = FXCollections.observableArrayList();

    public TicketEventModel() {

    }

    public TicketEventModel(TicketEvent ticketEvent) {
        id.set(ticketEvent.id());
        ticketId.set(ticketEvent.getTicketId());
        eventId.set(ticketEvent.getEventId());
        total.set(ticketEvent.getQuantity());
        left.set(ticketEvent.getLeft());
        bought.set(ticketEvent.getBought());
        price.set(ticketEvent.getPrice());
    }

    public TicketEvent toEntity() {
        return new TicketEvent(
                id.get(),
                ticketId.get(),
                eventId.get(),
                price.get(),
                total.get()
        );
    }

    public static TicketEventModel fromEntity(TicketEvent ticket) {
        return new TicketEventModel(ticket);
    }

    public static TicketEventModel Empty() {
        return new TicketEventModel();
    }

    public void update(TicketEventModel ticketEventModel) {
        this.id.set(ticketEventModel.id.get());
        this.ticketId.set(ticketEventModel.ticketId.get());
        this.eventId.set(ticketEventModel.eventId.get());
        this.total.set(ticketEventModel.total.get());
        this.left.set(ticketEventModel.left.get());
        this.bought.set(ticketEventModel.bought.get());
        this.price.set(ticketEventModel.price.get());
        this.ticketsGenerated = ticketEventModel.ticketsGenerated;
    }

    public IntegerProperty id() {
        return id;
    }

    public IntegerProperty eventId() {
        return eventId;
    }

    public IntegerProperty total() {
        return total;
    }

    public IntegerProperty left() {
        left.bind(Bindings.subtract(total, bought));
        return left;
    }

    public IntegerProperty bought() {
        bought.bind(Bindings.size(ticketsGenerated));
        return bought;
    }

    public IntegerProperty ticketId() {
        return ticketId;
    }


    public DoubleProperty price() {
        return price;
    }

    public void setEvent(EventModel event) { this.event.set(event); }

    public SimpleObjectProperty<EventModel> event() { return event; }

    public void setTicketsGenerated(ObservableList<TicketGeneratedModel> tickets) { this.ticketsGenerated = tickets; }

    public ObservableList<TicketGeneratedModel> ticketsGenerated() { return ticketsGenerated; }
}
