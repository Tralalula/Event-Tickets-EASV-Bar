package event.tickets.easv.bar.bll;

import event.tickets.easv.bar.be.Event;
import event.tickets.easv.bar.be.Ticket.Ticket;
import event.tickets.easv.bar.be.Ticket.TicketEvent;
import event.tickets.easv.bar.be.Ticket.TicketGenerated;
import event.tickets.easv.bar.dal.dao.TicketDAO;
import event.tickets.easv.bar.util.Result;
import event.tickets.easv.bar.util.Result.Success;
import event.tickets.easv.bar.util.Result.Failure;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TicketManager {
    private EntityManager entityManager;

    private ArrayList<TicketEvent> allEventTickets = new ArrayList<>();

    private TicketDAO ticketDAO;

    public TicketManager() {
        this.entityManager = new EntityManager();
        this.ticketDAO = new TicketDAO();
    }

    public Ticket add(Ticket ticket) {
        return ticketDAO.add(ticket).get();
    }

    public List<TicketEvent> getAllEventTickets() {
        if (allEventTickets.isEmpty()) {
            Result<List<TicketEvent>> result = entityManager.all(TicketEvent.class);
            switch (result) {
                case Success<List<TicketEvent>> s -> allEventTickets.addAll(s.result());
                case Failure<List<TicketEvent>> f -> System.out.println("Error: " + f.cause());
            }
        }
        return allEventTickets;
    }

    public List<TicketEvent> getAllTicketsForTicket(Ticket ticket) {
        List<TicketEvent> allEventTickets = getAllEventTickets();
        List<TicketEvent> matchingEvents = allEventTickets.stream()
                .filter(ticketEvent -> ticketEvent.getTicketId() == ticket.getId())
                .collect(Collectors.toList());


        return matchingEvents;
    }

    public List<TicketGenerated> getAllTickets() {
        ArrayList<TicketGenerated> generatedTickets = new ArrayList<>();

        Result<List<TicketGenerated>> result = entityManager.all(TicketGenerated.class);
        switch (result) {
            case Success<List<TicketGenerated>> s -> generatedTickets.addAll(s.result());
            case Failure<List<TicketGenerated>> f -> System.out.println("Error: " + f.cause());
        }

        return generatedTickets;
    }
}
