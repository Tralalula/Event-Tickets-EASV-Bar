package event.tickets.easv.bar.dal.dao;

import event.tickets.easv.bar.be.Event;
import event.tickets.easv.bar.be.User;
import event.tickets.easv.bar.dal.database.AssociationInsertParameterSetter;
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
                new EventUserSQLTemplate(),
                new EventUserInsertParameterSetter()
        );
    }

    @Override
    public Result<Boolean> addAssociation(Event entityA, User entityB) {
        return daoHelper.addAssociation(entityA, entityB);
    }

    @Override
    public Result<Boolean> removeAssociation(Event entityA, User entityB) {
        return null;
    }

    @Override
    public Result<List<User>> findAssociatesOfA(Event entityA) {
        return null;
    }

    @Override
    public Result<List<Event>> findAssociatesOfB(User entityB) {
        return null;
    }

    @Override
    public Result<Boolean> deleteAssociationsFor(Object entity) {
        return null;
    }
}

class EventUserSQLTemplate implements AssociationSQLTemplate<Event, User> {
    @Override
    public String insertRelationSQL() {
        return null;
    }
}

class EventUserInsertParameterSetter implements AssociationInsertParameterSetter<Event, User> {

    @Override
    public void setParameters(PreparedStatement stmt, Event entityA, User entityB) throws SQLException {
        stmt.setInt(1, entityA.id());
        stmt.setInt(2, entityB.id());
    }
}