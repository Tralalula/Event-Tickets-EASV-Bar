package event.tickets.easv.bar.be;

import java.util.List;

public interface Entity<T> {
    void update(T updatedDate);
    int id();
    void setAssociations(List<?> associations);
}
