package event.tickets.easv.bar.dal.dao;

import event.tickets.easv.bar.be.Entity;
import event.tickets.easv.bar.dal.database.*;
import event.tickets.easv.bar.util.Result;

import java.util.List;

public class DBJunctionDAOHelper<A extends Entity<A>, B extends Entity<B>> implements EntityAssociation<A, B> {
    private DBConnector dbConnector = null;
    private final AssociationSQLTemplate<A, B> sqlTemplate;

    public DBJunctionDAOHelper(AssociationSQLTemplate<A, B> sqlTemplate) {
        this.sqlTemplate = sqlTemplate;
    }

    @Override
    public Result<Boolean> addAssociation(A entityA, B entityB) {
        return null;
    }

    @Override
    public Result<Boolean> removeAssociation(A entityA, B entityB) {
        return null;
    }

    @Override
    public Result<List<B>> findAssociatesOfA(A entityA) {
        return null;
    }

    @Override
    public Result<List<A>> findAssociatesOfB(B entityB) {
        return null;
    }

    @Override
    public Result<Boolean> deleteAssociationsFor(Object entity) {
        return null;
    }
}
