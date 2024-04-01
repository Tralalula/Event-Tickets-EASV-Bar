package event.tickets.easv.bar.dal.dao.Ticket;

import event.tickets.easv.bar.be.Entity;
import event.tickets.easv.bar.be.Ticket.Ticket;
import event.tickets.easv.bar.be.Ticket.TicketEvent;
import event.tickets.easv.bar.dal.dao.DBJunctionDAOHelper;
import event.tickets.easv.bar.dal.database.AssociationParameterSetter;
import event.tickets.easv.bar.dal.database.AssociationSQLTemplate;
import event.tickets.easv.bar.dal.database.EntityAssociation;
import event.tickets.easv.bar.util.Result;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class TicketEventAssociationDAO implements EntityAssociation<Ticket, TicketEvent> {
    private final DBJunctionDAOHelper<Ticket, TicketEvent> daoHelper;

    public TicketEventAssociationDAO() {
        this.daoHelper = new DBJunctionDAOHelper<>(
                Ticket.class, TicketEvent.class,
                new TicketEventAssociationSQLTemplate(),
                new TicketEventAssociationParameterSetter(),
                new TicketResultSetMapper(),
                new TicketEventResultSetMapper()
        );
    }

    @Override
    public Result<Boolean> addAssociation(Ticket entityA, TicketEvent entityB) {
        return daoHelper.addAssociation(entityA, entityB);
    }

    @Override
    public Result<Boolean> removeAssociation(Ticket entityA, TicketEvent entityB) {
        return daoHelper.removeAssociation(entityA, entityB);
    }

    @Override
    public Result<List<TicketEvent>> findAssociatesOfA(Ticket entityA) {
        return daoHelper.findAssociatesOfA(entityA);
    }

    @Override
    public Result<List<Ticket>> findAssociatesOfB(TicketEvent entityB) {
        return daoHelper.findAssociatesOfB(entityB);
    }

    @Override
    public Result<List<?>> findAssociatesOf(Entity<?> entity) {
        return daoHelper.findAssociatesOf(entity);
    }

    @Override
    public Result<Boolean> deleteAssociationsFor(Entity<?> entity) {
        return daoHelper.deleteAssociationsFor(entity);
    }
}

class TicketEventAssociationSQLTemplate implements AssociationSQLTemplate<Ticket, TicketEvent> {
    @Override
    public String insertRelationSQL() {
        return "INSERT INTO dbo.TicketEvent (ticketId, eventId, price, quantity) VALUES (?, ?, ?, ?);";
    }

    @Override
    public String deleteRelationSQL() {
        return "DELETE FROM dbo.TicketEvent WHERE ticketId = ?";
    }

    @Override
    public String selectAForBSQL() {
        return """
                SELECT ticket.id, title, classification
                FROM dbo.Ticket ticket
                JOIN dbo.ticketEvent ticketEvent ON ticketEvent.ticketId = ticket.id
                WHERE ticketEvent.ticketId = ?
                """;
    }

    @Override
    public String selectBForASQL() {
        return """
                SELECT ticket.id, ticketId, eventId, price, quantity
                FROM dbo.TicketEvent ticketEvent
                JOIN dbo.Ticket ticket ON ticketEvent.ticketId = ticket.id
                WHERE ticket.id = ?;
                """;
    }

    @Override
    public String deleteAssociationsForASQL() {
        return "DELETE FROM dbo.TicketEvent WHERE ticketId = ?";
    }

    @Override
    public String deleteAssociationsForBSQL() {
        return "DELETE FROM dbo.Ticket WHERE Id = ?";
    }
}

class TicketEventAssociationParameterSetter implements AssociationParameterSetter<Ticket, TicketEvent> {

    @Override
    public void setParameters(PreparedStatement stmt, Ticket entityA, TicketEvent entityB) throws SQLException {
        stmt.setInt(1, entityA.id());
        stmt.setInt(2, entityB.id());
    }
}