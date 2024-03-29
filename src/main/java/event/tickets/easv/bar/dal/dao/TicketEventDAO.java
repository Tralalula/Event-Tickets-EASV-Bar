package event.tickets.easv.bar.dal.dao;

import event.tickets.easv.bar.be.Ticket.Ticket;
import event.tickets.easv.bar.be.Ticket.TicketEvent;
import event.tickets.easv.bar.be.Ticket.TicketGenerated;
import event.tickets.easv.bar.dal.database.IdSetter;
import event.tickets.easv.bar.dal.database.PreparedStatementSetter;
import event.tickets.easv.bar.dal.database.ResultSetMapper;
import event.tickets.easv.bar.dal.database.SQLTemplate;
import event.tickets.easv.bar.util.Result;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class TicketEventDAO implements DAO<TicketEvent> {
    private final DBDaoHelper<TicketEvent> daoHelper;

    public TicketEventDAO() {
        this.daoHelper = new DBDaoHelper<>(
                new TicketEventSQLTemplate(),
                new TicketEventResultSetMapper(),
                new TicketEventPreparedStatementSetter(),
                new TicketEventIdSetter()
        );
    }
    @Override
    public Result<Optional<TicketEvent>> get(int id) {
        return daoHelper.get(id);
    }

    @Override
    public Result<List<TicketEvent>> all() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return daoHelper.all();
    }

    @Override
    public Result<TicketEvent> add(TicketEvent entity) {
        return daoHelper.add(entity);
    }

    @Override
    public Result<Boolean> update(TicketEvent original, TicketEvent updatedData) {
        return daoHelper.update(original, updatedData);
    }

    @Override
    public Result<Boolean> delete(TicketEvent entity) {
        return daoHelper.delete(entity);
    }
}

class TicketEventSQLTemplate implements SQLTemplate<TicketEvent> {
    @Override
    public String getSelectSQL() {
        return "SELECT * FROM dbo.TicketEvent WHERE ticketId = ?";
    }

    @Override
    public String allSelectSQL() {
        return "SELECT * FROM dbo.TicketEvent";
    }

    @Override
    public String insertSQL() {
        return """
        INSERT INTO dbo.TicketEvent (ticketId, eventId, price, quantity)
        VALUES (?, ?, ?, ?);
        """;
    }
}

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

class TicketEventPreparedStatementSetter implements PreparedStatementSetter<TicketEvent> {
    @Override
    public void setParameters(PreparedStatement stmt, TicketEvent entity) throws SQLException {

    }
}

class TicketEventIdSetter implements IdSetter<TicketEvent> {
    @Override
    public TicketEvent setId(TicketEvent entity, int id) {
        return new TicketEvent(id, entity);
    }
}