package event.tickets.easv.bar.bll;

import event.tickets.easv.bar.dal.dao.TicketDAO;
import event.tickets.easv.bar.dal.dao.TicketGeneratedDAO;

import javax.swing.text.html.parser.Entity;

public class TicketManager {
    private EntityManager entityManager;
    public TicketManager() {
        this.entityManager = new EntityManager();
    }

}
