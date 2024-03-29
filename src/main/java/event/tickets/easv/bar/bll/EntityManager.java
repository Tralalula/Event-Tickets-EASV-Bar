package event.tickets.easv.bar.bll;

import event.tickets.easv.bar.be.Event;
import event.tickets.easv.bar.be.Ticket.Ticket;
import event.tickets.easv.bar.be.Ticket.TicketGenerated;
import event.tickets.easv.bar.dal.dao.DAO;
import event.tickets.easv.bar.dal.dao.EventDAO;
import event.tickets.easv.bar.dal.dao.TicketDAO;
import event.tickets.easv.bar.dal.dao.TicketGeneratedDAO;
import event.tickets.easv.bar.util.Result;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Processes and manages entities.
 */
public class EntityManager {
    private final Map<Class<?>, DAO<?>> daos = new HashMap<>();

    public EntityManager() {
        registerDao(Event.class, new EventDAO());
        registerDao(Ticket.class, new TicketDAO());
        registerDao(TicketGenerated.class, new TicketGeneratedDAO());
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

    /**
     * Retrieves all instances of a specified entity.
     *
     * @param entity the Class object representing the entity type to retrieve.
     * @param <T> the type of the entity.
     * @return A result containing the list of all instances if retrieval was successful; otherwise a failure.
     * @throws IllegalArgumentException if there is no DAO registered for the provided entity type.
     */
    @SuppressWarnings("unchecked")
    public <T> Result<List<T>> all(Class<T> entity) {
        DAO<T> dao = (DAO<T>) daos.get(entity);
        if (dao == null) throw new IllegalArgumentException("Unknown entity type: " + entity);
        return dao.all();
    }

    /**
     * For testing purposes
     * @param args unused
     */
    public static void main(String[] args) {
        System.out.println(new EntityManager().all(Event.class));
    }
}
