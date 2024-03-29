package event.tickets.easv.bar.gui.component.tickets;

import event.tickets.easv.bar.Main;
import event.tickets.easv.bar.be.Event;
import event.tickets.easv.bar.be.Ticket.Ticket;
import event.tickets.easv.bar.be.Ticket.TicketEvent;
import event.tickets.easv.bar.be.Ticket.TicketGenerated;
import event.tickets.easv.bar.bll.TicketManager;
import event.tickets.easv.bar.gui.common.EventModel;
import event.tickets.easv.bar.gui.common.TicketModel;
import event.tickets.easv.bar.gui.component.main.MainModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TicketsModel {
    private MainModel model;
    private TicketManager ticketManager;

    public TicketsModel(MainModel model) {
        this.model = model;
        this.ticketManager = new TicketManager();
    }

    public ObservableList<EventModel> getEventModelsForEventTicket(TicketEvent ticket) {
        return model.eventModels().filtered(eventModel -> eventModel.id().get() == ticket.getEventId());
    }

    public List<TicketEvent> getTickets(Ticket ticket) {
        return ticketManager.getAllTicketsForTicket(ticket);
    }

    public List<TicketEventModel> getTicketsForEvent(Ticket ticket) {
        List<TicketEventModel> ticketModels = new ArrayList<>();
        List<TicketEvent> ticketEvents = getTickets(ticket);

        System.out.println(ticketEvents.size());

        for (TicketEvent ticketEvent : ticketEvents) {
            ObservableList<EventModel> filteredEvents = getEventModelsForEventTicket(ticketEvent);

            for (EventModel event : filteredEvents) {
                TicketEventModel ticketModel = TicketEventModel.fromEntity(ticketEvent, event.toEntity());
                ticketModels.add(ticketModel);
            }
        }

        return ticketModels;
    }
}
