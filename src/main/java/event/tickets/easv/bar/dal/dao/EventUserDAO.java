package event.tickets.easv.bar.dal.dao;

import event.tickets.easv.bar.be.Entity;
import event.tickets.easv.bar.be.Event;
import event.tickets.easv.bar.be.User;
import event.tickets.easv.bar.dal.database.AssociationParameterSetter;
import event.tickets.easv.bar.dal.database.AssociationSQLTemplate;
import event.tickets.easv.bar.dal.database.EntityAssociation;
import event.tickets.easv.bar.util.Result;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class EventUserDAO implements EntityAssociation<Event, User> {
    private final DBJunctionDAOHelper<Event, User> daoHelper;

    public EventUserDAO() {
        this.daoHelper = new DBJunctionDAOHelper<>(
                Event.class, User.class,
                new EventUserSQLTemplate(),
                new EventUserParameterSetter(),
                new EventResultSetMapper(),
                new UserResultSetMapper()
        );
    }

    @Override
    public Result<Boolean> addAssociation(Event entityA, User entityB) {
        return daoHelper.addAssociation(entityA, entityB);
    }

    @Override
    public Result<Boolean> removeAssociation(Event entityA, User entityB) {
        return daoHelper.removeAssociation(entityA, entityB);
    }

    @Override
    public Result<List<User>> findAssociatesOfA(Event entityA) {
        return daoHelper.findAssociatesOfA(entityA);
    }

    @Override
    public Result<List<Event>> findAssociatesOfB(User entityB) {
        return daoHelper.findAssociatesOfB(entityB);
    }

    @Override
    public Result<Boolean> deleteAssociationsFor(Entity<?> entity) {
        return daoHelper.deleteAssociationsFor(entity);
    }
}

class EventUserSQLTemplate implements AssociationSQLTemplate<Event, User> {
    @Override
    public String insertRelationSQL() {
        return "INSERT INTO dbo.EventUser (EventId, UserId) VALUES (?, ?);";
    }

    @Override
    public String deleteRelationSQL() {
        return "DELETE FROM dbo.EventUser WHERE EventId = ? AND UserID = ?";
    }

    @Override
    public String selectAForBSQL() {
        return """
               SELECT id, title, imageName, location, startDate, endDate, startTime, endTime, locationGuidance, extraInfo
               FROM dbo.EventUser eventuser
               JOIN dbo.Event event ON eventuser.EventId = event.id
               WHERE eventuser.UserId = ?;
               """;
    }

    @Override
    public String selectBForASQL() {
        return """
               SELECT id, username
               FROM dbo.EventUser eventuser
               JOIN dbo.Users users ON eventuser.UserId = users.id
               WHERE eventuser.EventId = ?;
               """;
    }

    @Override
    public String deleteAssociationsForASQL() {
        return "DELETE FROM dbo.EventUser WHERE EventId = ?";
    }

    @Override
    public String deleteAssociationsForBSQL() {
        return "DELETE FROM dbo.EventUser WHERE UserId = ?";
    }
}

class EventUserParameterSetter implements AssociationParameterSetter<Event, User> {

    @Override
    public void setParameters(PreparedStatement stmt, Event entityA, User entityB) throws SQLException {
        stmt.setInt(1, entityA.id());
        stmt.setInt(2, entityB.id());
    }
}