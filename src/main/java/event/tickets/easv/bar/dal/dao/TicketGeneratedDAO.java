package event.tickets.easv.bar.dal.dao;

import event.tickets.easv.bar.be.Ticket.TicketGenerated;
import event.tickets.easv.bar.dal.database.*;
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
                new TicketGeneratedInsertParameterSetter(),
                new TicketGeneratedUpdateParameterSetter(),
                new TicketGeneratedIdSetter(),
                List.of()
        );
    }
    @Override
    public Result<Optional<TicketGenerated>> get(int id) {
        return daoHelper.get(id);
    }

    @Override
    public Result<List<TicketGenerated>> all() {
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

    @Override
    public String updateSQL() {
        throw new UnsupportedOperationException("Er ikke implementeret. TicketGeneratedDAO: TicketGeneratedSQLTemplate.updateSQL()");
    }

    @Override
    public String deleteSQL() {
        throw new UnsupportedOperationException("Er ikke implementeret. TicketGeneratedDAO: TicketGeneratedSQLTemplate.deleteSQL()");
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

class TicketGeneratedInsertParameterSetter implements InsertParameterSetter<TicketGenerated> {
    @Override
    public void setParameters(PreparedStatement stmt, TicketGenerated entity) throws SQLException {
        throw new UnsupportedOperationException("Er ikke implementeret. TicketGeneratedDAO: TicketGeneratedInsertParameterSetter.setParameters()");

    }
}

class TicketGeneratedUpdateParameterSetter implements UpdateParameterSetter<TicketGenerated> {

    @Override
    public void setParameters(PreparedStatement stmt, TicketGenerated original, TicketGenerated updatedData) throws SQLException {
        throw new UnsupportedOperationException("Er ikke implementeret. TicketGeneratedDAO: TicketGeneratedUpdateParameterSetter.setParameters()");
    }
}

class TicketGeneratedIdSetter implements IdSetter<TicketGenerated> {
    @Override
    public TicketGenerated setId(TicketGenerated entity, int id) {
        return new TicketGenerated(id, entity);
    }
}