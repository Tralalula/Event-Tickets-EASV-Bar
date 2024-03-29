package event.tickets.easv.bar.dal.dao;

import event.tickets.easv.bar.be.Ticket;
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

public class TicketGeneratedDAO implements DAO<Ticket> {
    private final DBDaoHelper<Ticket> daoHelper;

    public TicketGeneratedDAO() {
        this.daoHelper = new DBDaoHelper<>(
                new TicketGeneratedSQLTemplate(),
                new TicketGeneratedResultSetMapper(),
                new TicketGeneratedPreparedStatementSetter(),
                new TicketGeneratedIdSetter()
        );
    }
    @Override
    public Result<Optional<Ticket>> get(int id) {
        return null;
    }

    @Override
    public Result<List<Ticket>> all() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return daoHelper.all();
    }

    @Override
    public Result<Ticket> add(Ticket entity) {
        return daoHelper.add(entity);
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

class TicketGeneratedSQLTemplate implements SQLTemplate<Ticket> {
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
        INSERT INTO dbo.Ticket (id, title, classification)
        VALUES (?, ?, ?, ?);
        """;
    }
}

class TicketGeneratedResultSetMapper implements ResultSetMapper<Ticket> {
    @Override
    public Ticket map(@NotNull ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String title = rs.getString("title");
        String classification = rs.getString("classification");

        return new Ticket(id, title, classification);
    }
}

class TicketGeneratedPreparedStatementSetter implements PreparedStatementSetter<Ticket> {
    @Override
    public void setParameters(PreparedStatement stmt, Ticket entity) throws SQLException {

    }
}

class TicketGeneratedIdSetter implements IdSetter<Ticket> {
    @Override
    public Ticket setId(Ticket entity, int id) {
        return new Ticket(id, entity);
    }
}