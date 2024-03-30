package event.tickets.easv.bar.gui.component.tickets;

import event.tickets.easv.bar.be.Event;
import event.tickets.easv.bar.be.Ticket.TicketEvent;
import javafx.beans.property.*;

public record TicketEventModel(
        IntegerProperty eventId,
        //StringProperty title,
        IntegerProperty total,
        IntegerProperty left,
        IntegerProperty bought,
        FloatProperty price
) {
    public static TicketEventModel fromEntity(TicketEvent ticket) {
        return new TicketEventModel(
                new SimpleIntegerProperty(ticket.getEventId()),
                //new SimpleStringProperty(event.title()),
                new SimpleIntegerProperty(ticket.getQuantity()),
                new SimpleIntegerProperty(ticket.getLeft()),
                new SimpleIntegerProperty(ticket.getBought()),
                new SimpleFloatProperty(ticket.getPrice())
        );
    }

    public static TicketEventModel Empty() {
        return new TicketEventModel(
                new SimpleIntegerProperty(),
                //new SimpleStringProperty(),
                new SimpleIntegerProperty(),
                new SimpleIntegerProperty(),
                new SimpleIntegerProperty(),
                new SimpleFloatProperty()
        );
    }

    public void update(TicketEventModel ticketEventModel) {
        this.eventId.set(ticketEventModel.eventId.get());
       // this.title.set(ticketEventModel.title.get());
        this.total.set(ticketEventModel.total.get());
        this.left.set(ticketEventModel.left.get());
        this.bought.set(ticketEventModel.bought.get());
        this.price.set(ticketEventModel.price.get());
    }
}
