package event.tickets.easv.bar.be;

import java.util.List;

public interface Entity<T> {
    void update(T updatedData);
    int id();
    void setAssociations(List<?> associations);
}
