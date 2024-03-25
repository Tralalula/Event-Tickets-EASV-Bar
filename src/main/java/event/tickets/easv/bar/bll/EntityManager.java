package event.tickets.easv.bar.bll;

import event.tickets.easv.bar.be.Event;
import event.tickets.easv.bar.dal.database.common.DAO;
import event.tickets.easv.bar.dal.database.dao.EventDAO;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityManager {
    private final Map<Class<?>, DAO<?>> daos = new HashMap<>();

    public EntityManager() throws IOException {
        daos.put(Event.class, new EventDAO());
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> all(Class<T> entity) throws Exception {
        DAO<T> dao = (DAO<T>) daos.get(entity);
        if (dao == null) throw new IllegalArgumentException("Unknown entity type: " + entity);
        return dao.all();
    }

    public static void main(String[] args) throws Exception {
        System.out.println(new EntityManager().all(Event.class));
    }
}
