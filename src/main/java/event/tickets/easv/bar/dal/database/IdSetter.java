package event.tickets.easv.bar.dal.database;

@FunctionalInterface
public interface IdSetter<T> {
    T setId(T entity, int id);
}
