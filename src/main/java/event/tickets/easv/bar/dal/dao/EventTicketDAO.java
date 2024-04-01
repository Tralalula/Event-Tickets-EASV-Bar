package event.tickets.easv.bar.dal.dao;

import event.tickets.easv.bar.be.Entity;
import event.tickets.easv.bar.be.Event;
import event.tickets.easv.bar.be.Ticket.TicketEvent;
import event.tickets.easv.bar.dal.database.*;
import event.tickets.easv.bar.util.Result;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class EventTicketDAO implements EntityAssociation<Event, TicketEvent> {
    private final DBJunctionDAOHelper<Event, TicketEvent> daoHelper;

    public EventTicketDAO() {
        this.daoHelper = new DBJunctionDAOHelper<>(
                Event.class, TicketEvent.class,
                new EventTicketSQLTemplate(),
                new EventTicketParameterSetter(),
                new EventResultSetMapper(),
                new TicketEventResultSetMapper()
        );
    }

    @Override
    public Result<Boolean> addAssociation(Event entityA, TicketEvent entityB) {
        return daoHelper.addAssociation(entityA, entityB);
    }

    @Override
    public Result<Boolean> removeAssociation(Event entityA, TicketEvent entityB) {
        return daoHelper.removeAssociation(entityA, entityB);
    }

    @Override
    public Result<List<TicketEvent>> findAssociatesOfA(Event entityA) {
        return daoHelper.findAssociatesOfA(entityA);
    }

    @Override
    public Result<List<Event>> findAssociatesOfB(TicketEvent entityB) {
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

class EventTicketSQLTemplate implements AssociationSQLTemplate<Event, TicketEvent> {
    @Override
    public String insertRelationSQL() {
        return "";
    }

    @Override
    public String deleteRelationSQL() {
        return "";
    }

    @Override
    public String selectAForBSQL() {
        return """
                 SELECT event.id, title, imageName, location, startDate, endDate, startTime, endTime, locationGuidance, extraInfo
                 FROM dbo.Event event
                 JOIN dbo.TicketEvent ON event.id = TicketEvent.eventId
                 WHERE event.id = ?;
                """;
    }

    @Override
    public String selectBForASQL() {
        return """
               SELECT ticketevent.id, ticketId, eventId, price, quantity
               FROM dbo.TicketEvent ticketevent
               JOIN dbo.Event event ON ticketevent.eventId = event.id
               WHERE ticketevent.eventId = ?;
               """;
    }

    @Override
    public String deleteAssociationsForASQL() {
        return "";
    }

    @Override
    public String deleteAssociationsForBSQL() {
        return "";
    }
}

class EventTicketParameterSetter implements AssociationParameterSetter<Event, TicketEvent> {

    @Override
    public void setParameters(PreparedStatement stmt, Event entityA, TicketEvent entityB) throws SQLException {
        stmt.setInt(1, entityA.id());
        stmt.setInt(2, entityB.id());
    }
}

// samme som i Ticket.TicketEventResultSetMapper, men skal lige have her pga package-private
// må finde anden løsning senere
class TicketEventResultSetMapper implements ResultSetMapper<TicketEvent> {
    @Override
    public TicketEvent map(@NotNull ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int ticketId = rs.getInt("ticketId");
        int eventId = rs.getInt("eventId");
        float price = rs.getFloat("price");
        int quantity = rs.getInt("quantity");

        return new TicketEvent(id, ticketId, eventId, price, quantity);
    }
}