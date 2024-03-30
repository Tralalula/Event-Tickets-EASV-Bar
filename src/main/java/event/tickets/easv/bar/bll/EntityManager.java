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
import event.tickets.easv.bar.util.Result;

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
    public <T> Result<Optional<T>> get(Class<T> entity, int id) {
        DAO<T> dao = (DAO<T>) daos.get(entity);
        if (dao == null) throw new IllegalArgumentException("Unexpected entity: " + entity);
        return dao.get(id);
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

    @SuppressWarnings("unchecked")
    public <T extends Entity<T>> Result<List<T>> allWithAssociations(Class<T> entityClass) {
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
        if (associatesResult == null) throw new IllegalArgumentException("associatesResult must not be null");
        associatesResult.ifSuccess(entity::setAssociations);
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
