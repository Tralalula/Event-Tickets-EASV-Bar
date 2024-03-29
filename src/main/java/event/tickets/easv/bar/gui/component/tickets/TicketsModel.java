package event.tickets.easv.bar.gui.component.tickets;

import event.tickets.easv.bar.Main;
import event.tickets.easv.bar.be.Ticket.TicketEvent;
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

    public <T> ObservableList<T> convertToObservableList(List<T> list) {
        return FXCollections.observableList(list);
    }

    public FilteredList<EventModel> getEventModels(int id) {
        FilteredList<EventModel> filtered = new FilteredList<>(model.eventModels());
        filtered.setPredicate(ticketEvent -> ticketEvent.id().get() == id);

        return filtered;
    }

    public FilteredList<TicketEvent> getTickets(int id) {
        FilteredList<TicketEvent> filtered = new FilteredList<>(convertToObservableList(ticketManager.getAllEventTickets()));
        filtered.setPredicate(ticketEvent -> ticketEvent.getTicketId() == id);

        return filtered;
    }

    //TODO lav smartere, hurtig løsning
    public List<TicketEventModel> getTicketsForEvent(int ticketId) {
        List<TicketEventModel> ticketModels = new ArrayList<>();
        FilteredList<TicketEvent> ticketEvents = getTickets(ticketId);

        for (TicketEvent ticketEvent : ticketEvents) {
            FilteredList<EventModel> filteredEvents = getEventModels(ticketEvent.getEventId());

            for (EventModel event : filteredEvents) {
                TicketEventModel ticketModel = TicketEventModel.fromEntity(ticketEvent, event.toEntity());
                ticketModels.add(ticketModel);
            }
        }

        return ticketModels;
    }
}
