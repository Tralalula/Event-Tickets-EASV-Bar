package event.tickets.easv.bar.dal.dao;

import event.tickets.easv.bar.be.Entity;
import event.tickets.easv.bar.be.Event;
import event.tickets.easv.bar.be.Ticket.Ticket;
import event.tickets.easv.bar.be.Ticket.TicketEvent;
import event.tickets.easv.bar.dal.database.AssociationParameterSetter;
import event.tickets.easv.bar.dal.database.AssociationSQLTemplate;
import event.tickets.easv.bar.dal.database.EntityAssociation;
import event.tickets.easv.bar.util.Result;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class TicketEventEventAssociationDAO implements EntityAssociation<TicketEvent, Event> {
    private final DBJunctionDAOHelper<TicketEvent, Event> daoHelper;

    public TicketEventEventAssociationDAO() {
        this.daoHelper = new DBJunctionDAOHelper<>(
                TicketEvent.class, Event.class,
                new TicketEventEventAssociationSQLTemplate(),
                new TicketEventEventAssociationParameterSetter(),
                new TicketEventResultSetMapper(),
                new EventResultSetMapper()
        );
    }

    @Override
    public Result<Boolean> addAssociation(TicketEvent entityA, Event entityB) {
        return daoHelper.addAssociation(entityA, entityB);
    }

    @Override
    public Result<Boolean> removeAssociation(TicketEvent entityA, Event entityB) {
        return daoHelper.removeAssociation(entityA, entityB);
    }

    @Override
    public Result<List<Event>> findAssociatesOfA(TicketEvent entityA) {
        return daoHelper.findAssociatesOfA(entityA);
    }

    @Override
    public Result<List<TicketEvent>> findAssociatesOfB(Event entityB) {
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

class TicketEventEventAssociationSQLTemplate implements AssociationSQLTemplate<TicketEvent, Event> {
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

class TicketEventEventAssociationParameterSetter implements AssociationParameterSetter<TicketEvent, Event> {

    @Override
    public void setParameters(PreparedStatement stmt, TicketEvent entityA, Event entityB) throws SQLException {
        stmt.setInt(1, entityA.id());
        // stmt.setInt(2, entityB.id());
    }
}