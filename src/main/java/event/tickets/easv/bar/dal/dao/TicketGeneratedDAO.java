package event.tickets.easv.bar.dal.dao;

import event.tickets.easv.bar.be.Ticket.Ticket;
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

public class TicketGeneratedDAO implements DAO<TicketGenerated> {
    private final DBDaoHelper<TicketGenerated> daoHelper;

    public TicketGeneratedDAO() {
        this.daoHelper = new DBDaoHelper<>(
                new TicketGeneratedSQLTemplate(),
                new TicketGeneratedResultSetMapper(),
                new TicketGeneratedPreparedStatementSetter(),
                new TicketGeneratedIdSetter()
        );
    }
    @Override
    public Result<Optional<TicketGenerated>> get(int id) {
        return null;
    }

    @Override
    public Result<List<TicketGenerated>> all() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return daoHelper.all();
    }

    @Override
    public Result<TicketGenerated> add(TicketGenerated entity) {
        return daoHelper.add(entity);
    }

    @Override
    public Result<Boolean> update(TicketGenerated original, TicketGenerated updatedData) {
        return daoHelper.update(original, updatedData);
    }

    @Override
    public Result<Boolean> delete(TicketGenerated entity) {
        return daoHelper.delete(entity);
    }
}

class TicketGeneratedSQLTemplate implements SQLTemplate<TicketGenerated> {
    @Override
    public String getSelectSQL() {
        return "SELECT * FROM dbo.TicketGenerated WHERE eventId = ?";
    }

    @Override
    public String allSelectSQL() {
        return "SELECT * FROM dbo.TicketGenerated";
    }

    @Override
    public String insertSQL() {
        return """
        INSERT INTO dbo.TicketGenerated (eventId, customerId, assigned, used, barcode, qrcode)
        VALUES (?, ?, ?, ?, ?, ?);
        """;
    }
}

class TicketGeneratedResultSetMapper implements ResultSetMapper<TicketGenerated> {
    @Override
    public TicketGenerated map(@NotNull ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int eventId = rs.getInt("eventId");
        int customerId = rs.getInt("customerId");
        boolean assigned = rs.getBoolean("assigned");
        boolean used = rs.getBoolean("used");
        String barcode = rs.getString("barcode");
        String qrcode = rs.getString("qrcode");

        return new TicketGenerated(id, eventId, customerId, assigned, used, barcode, qrcode);
    }
}

class TicketGeneratedPreparedStatementSetter implements PreparedStatementSetter<TicketGenerated> {
    @Override
    public void setParameters(PreparedStatement stmt, TicketGenerated entity) throws SQLException {

    }
}

class TicketGeneratedIdSetter implements IdSetter<TicketGenerated> {
    @Override
    public TicketGenerated setId(TicketGenerated entity, int id) {
        return new TicketGenerated(id, entity);
    }
}