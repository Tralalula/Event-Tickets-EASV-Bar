package event.tickets.easv.bar.dal.dao;

import event.tickets.easv.bar.be.Event;
import event.tickets.easv.bar.dal.database.SQLTemplate;
import event.tickets.easv.bar.dal.database.ResultSetMapper;
import event.tickets.easv.bar.util.Result;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public class EventDAO implements DAO<Event> {
    private final DBDaoHelper<Event> daoHelper;

    public EventDAO() {
        this.daoHelper = new DBDaoHelper<>(new EventSQLTemplate(), new EventResultSetMapper());
    }

    @Override
    public Result<Optional<Event>> get(int id) {
        return null;
    }

    @Override
    public Result<List<Event>> all() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return daoHelper.all();
    }

    @Override
    public Result<Event> add(Event event) {
        return null;
    }

    @Override
    public Result<Boolean> update(Event original, Event updatedData) {
        return null;
    }

    @Override
    public Result<Boolean> delete(Event event) {
        return null;
    }


    public static void main(String[] args) {
        System.out.println(new EventDAO().all());
    }
}

class EventSQLTemplate implements SQLTemplate<Event> {
    @Override
    public String getSelectSQL() {
        return "SELECT * FROM dbo.Event WHERE id = ?";
    }

    @Override
    public String allSelectSQL() {
        return "SELECT * FROM dbo.Event";
    }
}

class EventResultSetMapper implements ResultSetMapper<Event> {
    @Override
    public Event map(@NotNull ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String title = rs.getString("title");
        String imageName = rs.getString("imageName");
        String location = rs.getString("location");
        LocalDate startDate = rs.getDate("startDate").toLocalDate();
        LocalDate endDate = rs.getDate("endDate") != null ? rs.getDate("endDate").toLocalDate() : null;
        LocalTime startTime = rs.getTime("startTime").toLocalTime();
        LocalTime endTime = rs.getTime("endTime") != null ? rs.getTime("endTime").toLocalTime() : null;
        String locationGuidance = rs.getString("locationGuidance");
        String extraInfo = rs.getString("extraInfo");

        return new Event(id, title, imageName, location, startDate, endDate, startTime, endTime, locationGuidance, extraInfo);
    }
}