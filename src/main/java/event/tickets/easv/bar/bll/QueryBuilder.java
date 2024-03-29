package event.tickets.easv.bar.bll;

import event.tickets.easv.bar.be.Entity;
import event.tickets.easv.bar.util.Result;

import javax.management.Query;
import java.util.ArrayList;
import java.util.List;

public class QueryBuilder<T extends Entity<T>> {
    private final EntityManager entityManager;
    private final Class<T> entityClass;
    private final List<Class<?>> associations = new ArrayList<>();

    QueryBuilder(EntityManager entityManager, Class<T> entityClass) {
        this.entityManager = entityManager;
        this.entityClass = entityClass;
    }

    public QueryBuilder<T> withAssociation(Class<?> associationClass) {
        associations.add(associationClass);
        return this;
    }

    public Result<List<T>> fetch() {
        return entityManager.fetchWithAssociations(entityClass, associations);
    }
}
