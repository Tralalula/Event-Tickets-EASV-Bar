package event.tickets.easv.bar.bll;

import event.tickets.easv.bar.be.Entity;
import event.tickets.easv.bar.be.Event;
import event.tickets.easv.bar.be.Ticket;
import event.tickets.easv.bar.be.User;
import event.tickets.easv.bar.dal.dao.DAO;
import event.tickets.easv.bar.dal.dao.EventDAO;
import event.tickets.easv.bar.dal.dao.EventUserDAO;
import event.tickets.easv.bar.dal.dao.UserDAO;
import event.tickets.easv.bar.dal.database.EntityAssociation;
import event.tickets.easv.bar.util.Result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Processes and manages entities.
 */
public class EntityManager {
    private final Map<Class<?>, DAO<?>> daos = new HashMap<>();
    private final List<EntityAssociationDescriptor<?, ?>> associations = new ArrayList<>();

    public EntityManager() {
        // DAOs
        registerDao(Event.class, new EventDAO());
        registerDao(User.class, new UserDAO());

        // Associations
        registerAssociation(Event.class, User.class, new EventUserDAO());
    }

    /**
     * Registers an entity with its corresponding DAO
     *
     * @param entity the Class object representing the entity type
     * @param dao the entity's corresponding DAO
     * @param <T> the type of entity
     */
    public <T> void registerDao(Class<T> entity, DAO<T> dao) {
        daos.put(entity, dao);
    }

    public <A, B> void registerAssociation(Class<A> entityA, Class<B> entityB,
                                           EntityAssociation<A, B> entityAssociation) {
        associations.add(new EntityAssociationDescriptor<>(entityA, entityB, entityAssociation));
    }

    public <T extends Entity<T>> QueryBuilder<T> get(Class<T> entityClass) {
        return new QueryBuilder<>(this, entityClass);
    }

    <T extends Entity<T>> Result<List<T>> fetchWithAssociations(Class<T> entityClass, List<Class<?>> associationClasses) {
        return fetchEntities(entityClass, associationClasses);
    }

    /**
     * Retrieves all instances of a specified entity.
     *
     * @param entityClass the Class object representing the entity type to retrieve.
     * @param <T> the type of the entity.
     * @return A result containing the list of all instances if retrieval was successful; otherwise a failure.
     * @throws IllegalArgumentException if there is no DAO registered for the provided entity type.
     */
    @SuppressWarnings("unchecked")
    public <T> Result<List<T>> all(Class<T> entityClass) {
        DAO<T> dao = (DAO<T>) daos.get(entityClass);
        if (dao == null) throw new IllegalArgumentException("Unknown entity type: " + entityClass);
        return dao.all();
    }

    public <T extends Entity<T>> Result<List<T>> allWithAssociations(Class<T> entityClass) {
        return fetchEntities(entityClass, List.of());
    }

    @SuppressWarnings("unchecked")
    private <T extends Entity<T>> Result<List<T>> fetchEntities(Class<T> entityClass, List<Class<?>> associationClasses) {
        DAO<T> dao = (DAO<T>) daos.get(entityClass);
        if (dao == null) throw new IllegalArgumentException("Unexpected entity " + entityClass);

        Result<List<T>> result = dao.all();
        result.ifSuccess(entities -> entities.forEach(entity -> processAssociations(entity, associationClasses)));
        return result;
    }

    private <T extends Entity<T>> void processAssociations(T entity, List<Class<?>> associationClasses) {
        Class<?> entityClass = entity.getClass();

        associations.stream()
                .filter(descriptor -> shouldProcessAssociation(descriptor, entityClass, associationClasses))
                .forEach(descriptor -> processSingleAssociation(entity, descriptor));
    }

    private void processSingleAssociation(Entity<?> entity, EntityAssociationDescriptor<?, ?> descriptor) {
        EntityAssociation<?, ?> association = descriptor.entityAssociation();
        Result<List<?>> associatesResult = association.findAssociatesOf(entity);
        if (associatesResult != null) associatesResult.ifSuccess(entity::setAssociations);
    }

    private boolean shouldProcessAssociation(EntityAssociationDescriptor<?, ?> descriptor, Class<?> entityClass, List<Class<?>> associationClasses) {
        boolean isDirectAssociation = descriptor.entityA().equals(entityClass) || descriptor.entityB().equals(entityClass);
        boolean isRequestedAssociation = associationClasses.isEmpty() || associationClasses.contains(descriptor.entityA()) || associationClasses.contains(descriptor.entityB());
        return isDirectAssociation && isRequestedAssociation;
    }

    /**
     * For testing purposes
     * @param args unused
     */
    public static void main(String[] args) {
/*        Result<List<new EntityManager()
                .get(Event.class)
                .withAssociation(User.class)
                .fetch();
        );*/

    }
}
