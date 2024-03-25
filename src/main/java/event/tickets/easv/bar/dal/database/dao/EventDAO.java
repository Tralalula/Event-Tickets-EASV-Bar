package event.tickets.easv.bar.dal.database.dao;

import event.tickets.easv.bar.be.Event;
import event.tickets.easv.bar.dal.database.common.*;
import event.tickets.easv.bar.dal.database.common.ResultSetMapper;
import event.tickets.easv.bar.util.Result;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class EventDAO implements DAO<Event> {
    private final SQLTemplate<Event> sqlTemplate;
    private final ResultSetMapper<Event> resultSetMapper;

    public EventDAO() {
        this.sqlTemplate = new EventSQLTemplate();
        this.resultSetMapper = new EventResultSetMapper();
    }

    @Override
    public Optional<Event> get(int id) throws Exception {
        return Optional.empty();
    }

    @Override
    public Result<List<Event>> all() {
        return DAOHelper.all(sqlTemplate, resultSetMapper);
    }

    @Override
    public Event add(Event event) throws Exception {
        return null;
    }

    @Override
    public boolean update(Event original, Event updatedData) throws Exception {
        return false;
    }

    @Override
    public boolean delete(Event event) throws Exception {
        return false;
    }

    public static void main(String[] args) throws Exception {
        System.out.println(new EventDAO().all());
    }
}

class EventSQLTemplate implements SQLTemplate<Event> {
    @Override
    public String getSelectSQL() {
        return "SELECT * FROM dbo.Event";
    }
}

class EventResultSetMapper implements ResultSetMapper<Event> {
    @Override
    public Event map(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String title = rs.getString("title");
        return new Event(id, title);
    }
}