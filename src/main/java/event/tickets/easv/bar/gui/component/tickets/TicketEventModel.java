package event.tickets.easv.bar.gui.component.tickets;

import event.tickets.easv.bar.be.Event;
import event.tickets.easv.bar.be.Ticket.TicketEvent;
import event.tickets.easv.bar.gui.common.EventModel;
import event.tickets.easv.bar.gui.common.TestModel;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TicketEventModel {
    private final IntegerProperty ticketId = new SimpleIntegerProperty();
    private final IntegerProperty eventId = new SimpleIntegerProperty();
    private final IntegerProperty total = new SimpleIntegerProperty();
    private final IntegerProperty left = new SimpleIntegerProperty();
    private final IntegerProperty bought = new SimpleIntegerProperty();
    private final FloatProperty price = new SimpleFloatProperty();

    private SimpleObjectProperty<EventModel> event = new SimpleObjectProperty<>();

    public TicketEventModel() {

    }

    public TicketEventModel(TicketEvent ticketEvent) {
        ticketId.set(ticketEvent.getTicketId());
        eventId.set(ticketEvent.getId());
        total.set(ticketEvent.getQuantity());
        left.set(ticketEvent.getLeft());
        bought.set(ticketEvent.getBought());
        price.set(ticketEvent.getPrice());
    }

    public static TicketEventModel fromEntity(TicketEvent ticket) {
        return new TicketEventModel(ticket);
    }

    public static TicketEventModel Empty() {
        return new TicketEventModel();
    }

    public void update(TicketEventModel ticketEventModel) {
        this.ticketId.set(ticketEventModel.ticketId.get());
        this.eventId.set(ticketEventModel.eventId.get());
        this.total.set(ticketEventModel.total.get());
        this.left.set(ticketEventModel.left.get());
        this.bought.set(ticketEventModel.bought.get());
        this.price.set(ticketEventModel.price.get());
        this.event.set(ticketEventModel.event.get());
    }

    public IntegerProperty eventId() {
        return eventId;
    }


    public IntegerProperty total() {
        return total;
    }


    public IntegerProperty left() {
        return left;
    }


    public IntegerProperty bought() {
        return bought;
    }

    public IntegerProperty ticketId() {
        return ticketId;
    }


    public FloatProperty price() {
        return price;
    }

    public void setEvent(EventModel event) { this.event.set(event); }

    public SimpleObjectProperty<EventModel> event() { return event; }

}
