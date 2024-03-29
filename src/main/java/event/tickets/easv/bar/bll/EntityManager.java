package event.tickets.easv.bar.bll;

import event.tickets.easv.bar.be.Entity;
import event.tickets.easv.bar.be.Event;
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
        registerEntityAssociation(Event.class, User.class, new EventUserDAO());
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

    public <A, B> void registerEntityAssociation(Class<A> entityA, Class<B> entityB,
                                                 EntityAssociation<A, B> entityAssociation) {
        associations.add(new EntityAssociationDescriptor<>(entityA, entityB, entityAssociation));
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
    public <T extends Entity<T>> Result<List<T>> all(Class<T> entityClass) {
        DAO<T> dao = (DAO<T>) daos.get(entityClass);
        if (dao == null) throw new IllegalArgumentException("Unexpected entity: " + entityClass);
        Result<List<T>> result = dao.all();

        result.ifSuccess(entities -> entities.forEach(this::processEntityAssociation));

        return result;
    }

    private <T extends Entity<T>> void processEntityAssociation(T entity) {
        Class<?> entityClass = entity.getClass();
        associations.stream()
                .filter(descriptor -> descriptor.entityA().equals(entityClass) || descriptor.entityB().equals(entityClass))
                .forEach(descriptor -> findAndSetAssociations(descriptor.entityAssociation(), entity));
    }

    private void findAndSetAssociations(EntityAssociation<?, ?> association, Entity<?> entity) {
        Result<List<?>> associatesResult = association.findAssociatesOf(entity);
        System.out.println(associatesResult);
        associatesResult.ifSuccess(entity::setAssociations);
    }

    /**
     * For testing purposes
     * @param args unused
     */
    public static void main(String[] args) {
        System.out.println(new EntityManager().all(User.class));

    }
}
