package event.tickets.easv.bar.dal.dao.Ticket;

import event.tickets.easv.bar.be.Entity;
import event.tickets.easv.bar.be.Ticket.Ticket;
import event.tickets.easv.bar.be.Ticket.TicketEvent;
import event.tickets.easv.bar.be.Ticket.TicketGenerated;
import event.tickets.easv.bar.dal.dao.DBJunctionDAOHelper;
import event.tickets.easv.bar.dal.database.AssociationParameterSetter;
import event.tickets.easv.bar.dal.database.AssociationSQLTemplate;
import event.tickets.easv.bar.dal.database.EntityAssociation;
import event.tickets.easv.bar.util.Result;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class TicketEventGeneratedDAO implements EntityAssociation<TicketEvent, TicketGenerated> {
    private final DBJunctionDAOHelper<TicketEvent, TicketGenerated> daoHelper;

    public TicketEventGeneratedDAO() {
        this.daoHelper = new DBJunctionDAOHelper<>(
                TicketEvent.class, TicketGenerated.class,
                new TicketEventGeneratedSQLTemplate(),
                new TicketEventGeneratedParameterSetter(),
                new TicketEventResultSetMapper(),
                new TicketGeneratedResultSetMapper()
        );
    }

    @Override
    public Result<Boolean> addAssociation(TicketEvent entityA, TicketGenerated entityB) {
        return daoHelper.addAssociation(entityA, entityB);
    }

    @Override
    public Result<Boolean> removeAssociation(TicketEvent entityA, TicketGenerated entityB) {
        return daoHelper.removeAssociation(entityA, entityB);
    }

    @Override
    public Result<List<TicketGenerated>> findAssociatesOfA(TicketEvent entityA) {
        return daoHelper.findAssociatesOfA(entityA);
    }

    @Override
    public Result<List<TicketEvent>> findAssociatesOfB(TicketGenerated entityB) {
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

class TicketEventGeneratedSQLTemplate implements AssociationSQLTemplate<TicketEvent, TicketGenerated> {
    @Override
    public String insertRelationSQL() {
        return "INSERT INTO dbo.TicketGenerated (ticketId, eventId, price, quantity) VALUES (?, ?, ?, ?);";
    }

    @Override
    public String deleteRelationSQL() {
        return "DELETE FROM dbo.TicketEvent WHERE ticketId = ?";
    }

    @Override
    public String selectAForBSQL() {
        return """
                SELECT ticket.id, ticketId, ticket.eventId, price, quantity
                FROM dbo.TicketEvent ticketEvent
                JOIN dbo.TicketGenerated ticket ON ticketEvent.id = ticket.id
                WHERE ticket.eventId = ?
                """;
    }

    @Override
    public String selectBForASQL() {
        return """
                SELECT ticketGenerated.id, ticketGenerated.eventId, customerId, assigned, used, barcode, qrcode
                FROM dbo.TicketGenerated ticketGenerated
                JOIN dbo.ticketEvent ticketEvent ON ticketEvent.id = ticketGenerated.eventId
                WHERE ticketEvent.id = ?;
                """;
    }

    @Override
    public String deleteAssociationsForASQL() {
        return "DELETE FROM dbo.TicketEvent WHERE id = ?";
    }

    @Override
    public String deleteAssociationsForBSQL() {
        return "DELETE FROM dbo.TicketGenerated WHERE eventId = ?";
    }
}

class TicketEventGeneratedParameterSetter implements AssociationParameterSetter<TicketEvent, TicketGenerated> {

    @Override
    public void setParameters(PreparedStatement stmt, TicketEvent entityA, TicketGenerated entityB) throws SQLException {
        stmt.setInt(1, entityA.id());
        // stmt.setInt(2, entityB.id());
    }
}