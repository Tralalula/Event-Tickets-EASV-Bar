package event.tickets.easv.bar.dal.dao;

import event.tickets.easv.bar.be.Event;
import event.tickets.easv.bar.dal.database.*;
import event.tickets.easv.bar.util.Result;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public class EventDAO implements DAO<Event> {
    private final DBDaoHelper<Event> daoHelper;

    public EventDAO() {
        this.daoHelper = new DBDaoHelper<>(
                new EventSQLTemplate(),
                new EventResultSetMapper(),
                new EventInsertParameterSetter(),
                new EventUpdateParameterSetter(),
                new EventIdSetter(),
                List.of(new EventUserDAO())
        );
    }

    @Override
    public Result<Optional<Event>> get(int id) {
        return daoHelper.get(id);
    }

    @Override
    public Result<List<Event>> all() {
        return daoHelper.all();
    }

    @Override
    public Result<Event> add(Event entity) {
        return daoHelper.add(entity);
    }

    @Override
    public Result<Boolean> update(Event original, Event updatedData) {
        return daoHelper.update(original, updatedData);
    }

    @Override
    public Result<Boolean> delete(Event entity) {
        return daoHelper.delete(entity);
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

    @Override
    public String insertSQL() {
        return """
               INSERT INTO dbo.Event (title, imageName, location, startDate, endDate, startTime, endTime, locationGuidance, extraInfo)
               VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);
               """;
    }

    @Override
    public String updateSQL() {
        return """
               UPDATE dbo.Event
               SET title = ?, imageName = ?, location = ?, startDate = ?, endDate = ?, startTime = ?, endTime = ?, locationGuidance = ?, extraInfo = ?
               WHERE id = ?;
               """;
    }

    @Override
    public String deleteSQL() {
        return "DELETE FROM dbo.Event WHERE id = ?;";
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

class EventInsertParameterSetter implements InsertParameterSetter<Event> {
    @Override
    public void setParameters(PreparedStatement stmt, Event entity) throws SQLException {
        stmt.setString(1, entity.title());
        stmt.setString(2, entity.imageName());
        stmt.setString(3, entity.location());

        stmt.setDate(4, Date.valueOf(entity.startDate()));
        if (entity.endDate() != null) {
            stmt.setDate(5, Date.valueOf(entity.endDate()));
        } else {
            stmt.setNull(5, Types.DATE);
        }

        stmt.setTime(6, Time.valueOf(entity.startTime()));
        if (entity.endTime() != null) {
            stmt.setTime(7, Time.valueOf(entity.endTime()));
        } else {
            stmt.setNull(7, Types.TIME);
        }

        stmt.setString(8, entity.locationGuidance());
        stmt.setString(9, entity.extraInfo());
    }
}

class EventUpdateParameterSetter implements UpdateParameterSetter<Event> {
    @Override
    public void setParameters(PreparedStatement stmt, Event original, Event updatedData) throws SQLException {
        // Same as EventInsertParameterSetters.setParameters()
        stmt.setString(1, updatedData.title());
        stmt.setString(2, updatedData.imageName());
        stmt.setString(3, updatedData.location());

        stmt.setDate(4, Date.valueOf(updatedData.startDate()));
        if (updatedData.endDate() != null) {
            stmt.setDate(5, Date.valueOf(updatedData.endDate()));
        } else {
            stmt.setNull(5, Types.DATE);
        }

        stmt.setTime(6, Time.valueOf(updatedData.startTime()));
        if (updatedData.endTime() != null) {
            stmt.setTime(7, Time.valueOf(updatedData.endTime()));
        } else {
            stmt.setNull(7, Types.TIME);
        }

        stmt.setString(8, updatedData.locationGuidance());
        stmt.setString(9, updatedData.extraInfo());

        // Only difference is here:
        stmt.setInt(10, original.id());
    }
}

class EventIdSetter implements IdSetter<Event> {
    @Override
    public Event setId(Event entity, int id) {
        return new Event(id, entity);
    }
}