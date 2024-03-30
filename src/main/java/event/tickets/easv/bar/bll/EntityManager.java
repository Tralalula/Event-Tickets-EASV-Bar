package event.tickets.easv.bar.bll;

import event.tickets.easv.bar.be.Entity;
import event.tickets.easv.bar.be.Event;
import event.tickets.easv.bar.be.User;
import event.tickets.easv.bar.dal.dao.DAO;
import event.tickets.easv.bar.dal.dao.EventDAO;
import event.tickets.easv.bar.dal.dao.EventUserDAO;
import event.tickets.easv.bar.dal.dao.UserDAO;
import event.tickets.easv.bar.dal.database.EntityAssociation;
import event.tickets.easv.bar.be.Ticket.Ticket;
import event.tickets.easv.bar.be.Ticket.TicketEvent;
import event.tickets.easv.bar.be.Ticket.TicketGenerated;
import event.tickets.easv.bar.dal.dao.*;
import event.tickets.easv.bar.util.FailureType;
import event.tickets.easv.bar.util.Result;
import event.tickets.easv.bar.util.Result.Failure;
import event.tickets.easv.bar.util.Result.Success;


import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
        registerDao(Ticket.class, new TicketDAO());
        registerDao(TicketGenerated.class, new TicketGeneratedDAO());
        registerDao(TicketEvent.class, new TicketEventDAO());

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

    @SuppressWarnings("unchecked")
    public <T> Result<Optional<T>> get(Class<T> entityClass, int id) {
        DAO<T> dao = (DAO<T>) daos.get(entityClass);
        if (dao == null) throw new IllegalArgumentException("Unexpected entity: " + entityClass);
        return dao.get(id);
    }

    @SuppressWarnings("unchecked")
    public <T extends Entity<T>> Result<Optional<T>> getWithAssociations(Class<T> entityClass, int id) {
        DAO<T> dao = (DAO<T>) daos.get(entityClass);
        if (dao == null) throw new IllegalArgumentException("Unexpected entity: " + entityClass);
        Result<Optional<T>> result = dao.get(id);

        result.ifSuccess(entity -> entity.ifPresent(this::processEntityAssociation));

        return result;
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
        if (dao == null) throw new IllegalArgumentException("Unexpected entity: " + entityClass);
        return dao.all();
    }

    @SuppressWarnings("unchecked")
    public <T extends Entity<T>> Result<List<T>> allWithAssociations(Class<T> entityClass) {
        DAO<T> dao = (DAO<T>) daos.get(entityClass);
        if (dao == null) throw new IllegalArgumentException("Unexpected entity: " + entityClass);
        Result<List<T>> result = dao.all();

        result.ifSuccess(entities -> entities.forEach(this::processEntityAssociation));

        return result;
    }

    @SuppressWarnings("unchecked")
    public <T> Result<T> add(T entity) {
        if (entity == null) return Failure.of(FailureType.INVALID_ENTITY_TYPE, "Entity cannot be null");
        DAO<T> dao = (DAO<T>) daos.get(entity.getClass());

        if (dao == null) return Failure.of(FailureType.INVALID_ENTITY_TYPE, "Unexpected entity: " + entity.getClass());

        return dao.add(entity);
    }

    @SuppressWarnings("unchecked")
    public <A extends Entity<A>, B extends Entity<B>> Result<Boolean> addAssociations(A entityA, List<B> entitiesB) {
        for (B entityB : entitiesB) {
            EntityAssociation<A, B> association = getEntityAssociation(entityA.getClass(), entityB.getClass());
            Result<Boolean> result = association.addAssociation(entityA, entityB);
            if (result instanceof Failure) return result; // We return early on failure
        }
        return Success.of(true);
    }

    @SuppressWarnings("unchecked")
    public <A extends Entity<A>, B extends Entity<B>> Result<Boolean> addAssociation(A entityA, B entityB) {
        EntityAssociation<A, B> association = getEntityAssociation(entityA.getClass(), entityB.getClass());
        return association.addAssociation(entityA, entityB);
    }

    @SuppressWarnings("unchecked")
    public <T> Result<Boolean> update(T entity, T updatedData) {
        if (entity == null || updatedData == null) return Failure.of(FailureType.INVALID_ENTITY_TYPE, "Entity cannot be null");
        DAO<T> dao = (DAO<T>) daos.get(entity.getClass());
        if (dao == null) return Failure.of(FailureType.INVALID_ENTITY_TYPE, "Unexpected entity: " + entity.getClass());
        return dao.update(entity, updatedData);
    }

    @SuppressWarnings("unchecked")
    private <A extends Entity<A>, B extends Entity<B>> EntityAssociation<A, B> getEntityAssociation(Class<A> entityAClass, Class<B> entityBClass) {
        for (EntityAssociationDescriptor<?, ?> descriptor : associations) {
            if (descriptor.entityA().equals(entityAClass) && descriptor.entityB().equals(entityBClass)) {
                return (EntityAssociation<A, B>) descriptor.entityAssociation();
            }
        }
        throw new IllegalArgumentException("No association found for given entity types");
    }

    private <T extends Entity<T>> void processEntityAssociation(T entity) {
        Class<?> entityClass = entity.getClass();
        associations.stream()
                    .filter(descriptor -> descriptor.entityA().equals(entityClass) || descriptor.entityB().equals(entityClass))
                    .forEach(descriptor -> findAndSetAssociations(descriptor.entityAssociation(), entity));
    }

    private void findAndSetAssociations(EntityAssociation<?, ?> association, Entity<?> entity) {
        Result<List<?>> associatesResult = association.findAssociatesOf(entity);
        if (associatesResult != null) associatesResult.ifSuccess(entity::setAssociations);
    }

    /**
     * For testing purposes only
     */
    void purge() {
        daos.clear();
        associations.clear();
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
