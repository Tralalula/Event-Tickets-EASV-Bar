package event.tickets.easv.bar.bll;

import event.tickets.easv.bar.be.Event;
import event.tickets.easv.bar.dal.database.common.DAO;
import event.tickets.easv.bar.dal.database.dao.EventDAO;
import event.tickets.easv.bar.util.Result;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityManager {
    private final Map<Class<?>, DAO<?>> daos = new HashMap<>();

    public EntityManager() {
        daos.put(Event.class, new EventDAO());
    }

    @SuppressWarnings("unchecked")
    public <T> Result<List<T>> all(Class<T> entity) {
        DAO<T> dao = (DAO<T>) daos.get(entity);
        if (dao == null) throw new IllegalArgumentException("Unknown entity type: " + entity);
        return dao.all();
    }

    public static void main(String[] args) {
        System.out.println(new EntityManager().all(Event.class));
    }
}
