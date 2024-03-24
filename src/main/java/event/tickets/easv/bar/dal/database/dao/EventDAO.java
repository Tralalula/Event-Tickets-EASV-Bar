package event.tickets.easv.bar.dal.database.dao;

import event.tickets.easv.bar.be.Event;
import event.tickets.easv.bar.dal.database.common.DAO;

import java.util.List;
import java.util.Optional;

public class EventDAO implements DAO<Event> {
    @Override
    public Optional<Event> get(int id) throws Exception {
        return Optional.empty();
    }

    @Override
    public List<Event> getAll() throws Exception {
        return null;
    }

    @Override
    public Event add(Event event) throws Exception {
        return null;
    }

    @Override
    public boolean update(Event original, Event updatedData) throws Exception {
        return false;
    }

    @Override
    public boolean delete(Event event) throws Exception {
        return false;
    }
}
