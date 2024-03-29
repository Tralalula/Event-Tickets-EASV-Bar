package event.tickets.easv.bar.dal.dao;

import event.tickets.easv.bar.be.Event;
import event.tickets.easv.bar.be.User;
import event.tickets.easv.bar.dal.database.AssociationSQLTemplate;
import event.tickets.easv.bar.dal.database.EntityAssociation;
import event.tickets.easv.bar.util.Result;

import java.util.List;

public class EventUserDAO implements EntityAssociation<Event, User> {
    private final DBJunctionDAOHelper<Event, User> daoHelper;

    public EventUserDAO() {
        this.daoHelper = new DBJunctionDAOHelper<>(
                new EventUserAssociationSQLTemplate()
        );
    }

    @Override
    public Result<Boolean> addAssociation(Event entityA, User entityB) {
        return null;
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

class EventUserAssociationSQLTemplate implements AssociationSQLTemplate<Event, User> {
    @Override
    public String insertRelationSQL() {
        return null;
    }
}