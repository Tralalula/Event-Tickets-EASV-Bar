package event.tickets.easv.bar.dal.dao;

import event.tickets.easv.bar.be.Event;
import event.tickets.easv.bar.dal.database.DBDaoHelper;
import event.tickets.easv.bar.dal.database.SQLTemplate;
import event.tickets.easv.bar.dal.database.ResultSetMapper;
import event.tickets.easv.bar.util.Result;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class EventDAO implements DAO<Event> {
    private final DBDaoHelper<Event> daoHelper;

    public EventDAO() {
        this.daoHelper = new DBDaoHelper<>(new EventSQLTemplate(), new EventResultSetMapper());
    }

    @Override
    public Result<Optional<Event>> get(int id) {
        return new Result.Failure<>(null);
    }

    @Override
    public Result<List<Event>> all() {
        return daoHelper.all();
    }

    @Override
    public Result<Event> add(Event event) {
        return new Result.Failure<>(null);
    }

    @Override
    public Result<Boolean> update(Event original, Event updatedData) {
        return new Result.Failure<>(null);
    }

    @Override
    public Result<Boolean> delete(Event event) {
        return new Result.Failure<>(null);
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