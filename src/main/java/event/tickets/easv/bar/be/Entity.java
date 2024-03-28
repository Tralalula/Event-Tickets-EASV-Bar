package event.tickets.easv.bar.be;

public interface Entity<T> {
    void update(T updatedDate);
    int id();
}
