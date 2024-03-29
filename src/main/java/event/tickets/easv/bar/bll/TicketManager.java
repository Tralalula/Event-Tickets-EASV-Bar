package event.tickets.easv.bar.bll;

import event.tickets.easv.bar.be.Ticket.TicketEvent;
import event.tickets.easv.bar.be.Ticket.TicketGenerated;
import event.tickets.easv.bar.gui.common.TicketModel;
import event.tickets.easv.bar.util.Result;
import event.tickets.easv.bar.util.Result.Success;
import event.tickets.easv.bar.util.Result.Failure;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TicketManager {
    private EntityManager entityManager;

    private ArrayList<TicketEvent> allEventTickets = new ArrayList<>();

    public TicketManager() {
        this.entityManager = new EntityManager();
    }

    public List<TicketGenerated> getAllTickets(int id) {
        ArrayList<TicketGenerated> generatedTickets = new ArrayList<>();

        Result<List<TicketGenerated>> result = entityManager.all(TicketGenerated.class);
        switch (result) {
            case Success<List<TicketGenerated>> s -> generatedTickets.addAll(s.result());
            case Failure<List<TicketGenerated>> f -> System.out.println("Error: " + f.cause());
        }

        return generatedTickets;
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

    public TicketEvent getEventTicket(int id) {
        return null;
    }
}
