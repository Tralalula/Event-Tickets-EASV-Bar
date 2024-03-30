package event.tickets.easv.bar.gui.common;

import event.tickets.easv.bar.be.Ticket.Ticket;
import event.tickets.easv.bar.be.Ticket.TicketEvent;
import event.tickets.easv.bar.gui.component.tickets.TicketEventModel;
import javafx.beans.Observable;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;


public record TicketModel(
        IntegerProperty id,
        StringProperty title,
        StringProperty type,
        ObservableList<TicketEventModel> ticketEvents
) {
    public static TicketModel fromEntity(Ticket ticket) {
        ObservableList<TicketEventModel> temp = FXCollections.observableArrayList();

        for (TicketEvent tc : ticket.getTicketEvent())
            temp.add(TicketEventModel.fromEntity(tc));

        return new TicketModel(
                new SimpleIntegerProperty(ticket.getId()),
                new SimpleStringProperty(ticket.getTitle()),
                new SimpleStringProperty(ticket.getType()),
                FXCollections.observableArrayList(temp)
        );
    }

    public static TicketModel Empty() {
        return new TicketModel(
                new SimpleIntegerProperty(),
                new SimpleStringProperty(),
                new SimpleStringProperty(),
                FXCollections.observableArrayList()
        );
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
}