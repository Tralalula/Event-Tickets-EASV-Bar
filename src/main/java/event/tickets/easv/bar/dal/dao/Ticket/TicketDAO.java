package event.tickets.easv.bar.dal.dao.Ticket;

import event.tickets.easv.bar.be.Ticket.Ticket;
import event.tickets.easv.bar.dal.dao.DAO;
import event.tickets.easv.bar.dal.dao.DBDaoHelper;
import event.tickets.easv.bar.dal.database.*;
import event.tickets.easv.bar.util.Result;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class TicketDAO implements DAO<Ticket> {
    private final DBDaoHelper<Ticket> daoHelper;

    public TicketDAO() {
        this.daoHelper = new DBDaoHelper<>(
                new TicketSQLTemplate(),
                new TicketResultSetMapper(),
                new TicketInsertParameterSetter(),
                new TicketUpdateParameterSetter(),
                new TicketIdSetter(),
                List.of()
        );
    }
    @Override
    public Result<Optional<Ticket>> get(Object id) {
        return daoHelper.get(id);
    }

    @Override
    public Result<List<Ticket>> all() {
        return daoHelper.all();
    }

    @Override
    public Result<Ticket> add(Ticket entity) {
        return daoHelper.add(entity);
    }

    @Override
    public Result<List<Ticket>> addAll(List<Ticket> entities) {
        return daoHelper.addAll(entities);
    }

    @Override
    public Result<Integer> batchAdd(List<Ticket> entities) {
        return daoHelper.batchAdd(entities);
    }

    @Override
    public Result<Boolean> update(Ticket original, Ticket updatedData) {
        return daoHelper.update(original, updatedData);
    }

    @Override
    public Result<Boolean> delete(Ticket entity) {
        return daoHelper.delete(entity);
    }
}

class TicketSQLTemplate implements SQLTemplate<Ticket> {
    @Override
    public String getSelectSQL() {
        return "SELECT * FROM dbo.Ticket WHERE id = ?";
    }

    @Override
    public String allSelectSQL() {
        return "SELECT * FROM dbo.Ticket";
    }

    @Override
    public String insertSQL() {
        return """
               INSERT INTO dbo.Ticket (title, classification)
               VALUES (?, ?);
               """;
    }

    @Override
    public String updateSQL() {
        throw new UnsupportedOperationException("Er ikke implementeret. TicketDAO: TicketSQLTemplate.updateSQL()");
    }

    @Override
    public String deleteSQL() {
        throw new UnsupportedOperationException("Er ikke implementeret. TicketDAO: TicketSQLTemplate.deleteSQL()");
    }
}

class TicketResultSetMapper implements ResultSetMapper<Ticket> {
    @Override
    public Ticket map(@NotNull ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String title = rs.getString("title");
        String classification = rs.getString("classification");

        return new Ticket(id, title, classification);
    }
}

class TicketInsertParameterSetter implements InsertParameterSetter<Ticket> {
    @Override
    public void setParameters(PreparedStatement stmt, Ticket entity) throws SQLException {
        stmt.setString(1, entity.getTitle());
        stmt.setString(2, entity.getType());
    }
}

class TicketUpdateParameterSetter implements UpdateParameterSetter<Ticket> {
    @Override
    public void setParameters(PreparedStatement stmt, Ticket original, Ticket updatedData) throws SQLException {
        throw new UnsupportedOperationException("Er ikke implementeret. TicketDAO: TicketUpdateParameterSetter.setParameters()");
    }
}

class TicketIdSetter implements IdSetter<Ticket> {
    @Override
    public Ticket setId(Ticket entity, int id) {
        return new Ticket(id, entity);
    }
}