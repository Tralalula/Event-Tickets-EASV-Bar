package event.tickets.easv.bar.bll;

import event.tickets.easv.bar.be.Ticket;
import event.tickets.easv.bar.dal.dao.TicketDAO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class TicketManager {

    private TicketDAO ticketDAO;

    public TicketManager() {
        try {
            this.ticketDAO = new TicketDAO();
        } catch (IOException e) {
            throw new RuntimeException("Error occurred\n" + e);
        }
    }

    public List<Ticket> getAllTickets() throws SQLException {
        return ticketDAO.getAllTickets();
    }

    public Ticket getTicket(int id) throws SQLException {
        return ticketDAO.getTicket(id);
    }
}
