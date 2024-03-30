package event.tickets.easv.bar.dal.dao;

import event.tickets.easv.bar.be.Ticket.TicketEvent;
import event.tickets.easv.bar.dal.database.*;
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
                new TicketEventInsertParameterSetter(),
                new TicketEventUpdateParameterSetter(),
                new TicketEventIdSetter(),
                List.of()
        );
    }
    @Override
    public Result<Optional<TicketEvent>> get(int id) {
        return daoHelper.get(id);
    }

    @Override
    public Result<List<TicketEvent>> all() {
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

    @Override
    public String updateSQL() {
        throw new UnsupportedOperationException("Er ikke implementeret. TicketEventDAO: TicketEventSQLTemplate.updateSQL()");

    }

    @Override
    public String deleteSQL() {
        throw new UnsupportedOperationException("Er ikke implementeret. TicketEventDAO: TicketEventSQLTemplate.deleteSQL()");
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

class TicketEventInsertParameterSetter implements InsertParameterSetter<TicketEvent> {
    @Override
    public void setParameters(PreparedStatement stmt, TicketEvent entity) throws SQLException {
        throw new UnsupportedOperationException("Er ikke implementeret. TicketEventDAO: TicketEventInsertParameterSetter.setParameters()");
    }
}

class TicketEventUpdateParameterSetter implements UpdateParameterSetter<TicketEvent> {

    @Override
    public void setParameters(PreparedStatement stmt, TicketEvent original, TicketEvent updatedData) throws SQLException {
        throw new UnsupportedOperationException("Er ikke implementeret. TicketEventDAO: TicketEventUpdateParameterSetter.setParameters()");
    }
}

class TicketEventIdSetter implements IdSetter<TicketEvent> {
    @Override
    public TicketEvent setId(TicketEvent entity, int id) {
        return new TicketEvent(id, entity);
    }
}