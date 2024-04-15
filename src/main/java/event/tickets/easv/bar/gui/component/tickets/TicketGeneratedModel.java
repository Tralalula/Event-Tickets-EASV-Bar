package event.tickets.easv.bar.gui.component.tickets;

import event.tickets.easv.bar.be.Ticket.Ticket;
import event.tickets.easv.bar.be.Ticket.TicketGenerated;
import javafx.beans.property.*;
import javafx.collections.ObservableList;

import java.util.ArrayList;


public class TicketGeneratedModel {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final IntegerProperty eventId = new SimpleIntegerProperty();
    private final IntegerProperty customerId = new SimpleIntegerProperty();
    private final BooleanProperty assigned = new SimpleBooleanProperty();
    private final BooleanProperty used = new SimpleBooleanProperty();
    private final StringProperty uniqueCode = new SimpleStringProperty();

    public TicketGeneratedModel() {

    }

    public TicketGeneratedModel(TicketGenerated ticket) {
        id.set(ticket.getId());
        eventId.set(ticket.getEventId());
        customerId.set(ticket.getCustomerId());
        assigned.set(ticket.isAssigned());
        used.set(ticket.isUsed());
        uniqueCode.set(ticket.getUniqueCode());
    }

    public static TicketGeneratedModel fromEntity(TicketGenerated ticket) {
        return new TicketGeneratedModel(ticket);
    }

    public static TicketGeneratedModel Empty() {
        return new TicketGeneratedModel();
    }

    public void update(TicketGeneratedModel ticketModel) {
        this.id.set(ticketModel.id.get());
        this.eventId.set(ticketModel.eventId.get());
        this.customerId.set(ticketModel.customerId.get());
        this.assigned.set(ticketModel.assigned.get());
        this.used.set(ticketModel.used.get());
        this.uniqueCode.set(ticketModel.uniqueCode.get());
    }

    public TicketGenerated toEntity() {
        return new TicketGenerated(
                id.get(),
                eventId.get(),
                customerId.get(),
                assigned.get(),
                used.get(),
                uniqueCode.get()
        );
    }

    public IntegerProperty id() {
        return id;
    }

    public IntegerProperty eventId() {
        return eventId;
    }

    public IntegerProperty customerId() {
        return customerId;
    }

    public BooleanProperty assigned() {
        return assigned;
    }

    public BooleanProperty used() {
        return used;
    }

    public StringProperty uniqueCode() {
        return uniqueCode;
    }

}