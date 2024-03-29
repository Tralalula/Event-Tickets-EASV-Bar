package event.tickets.easv.bar.dal.database;

import event.tickets.easv.bar.be.Entity;
import event.tickets.easv.bar.util.Result;

import java.util.List;

public interface EntityAssociation<A, B> {
    Result<Boolean> addAssociation(A entityA, B entityB);
    Result<Boolean> removeAssociation(A entityA, B entityB);
    Result<List<B>> findAssociatesOfA(A entityA);
    Result<List<A>> findAssociatesOfB(B entityB);
    Result<Boolean> deleteAssociationsFor(Entity<?> entity);
}
