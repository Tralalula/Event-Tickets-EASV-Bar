package event.tickets.easv.bar.gui.common;

import event.tickets.easv.bar.be.Ticket;
import javafx.beans.property.*;


public record TicketModel(
        IntegerProperty id,
        StringProperty title,
        StringProperty type
) {
    public static TicketModel fromEntity(Ticket ticket) {
        return new TicketModel(
                new SimpleIntegerProperty(ticket.getId()),
                new SimpleStringProperty(ticket.getTitle()),
                new SimpleStringProperty(ticket.getType())
                );
    }

    public Ticket toEntity() {
        return new Ticket(
                id.get(),
                title.get(),
                type.get()
        );
    }
}