package event.tickets.easv.bar.be;

import java.util.Objects;

public class Event {
    private static final int TITLE_MAX_LENGTH = 255;

    private int id;
    private String title;

    private void checkRep() {
        assert title != null : "title must not be null";
        assert !title.isEmpty() : "title must not be empty";
        assert title.length() <= TITLE_MAX_LENGTH : "title must not exceed " + TITLE_MAX_LENGTH + " characters";
    }

    public Event(String title) {
        this(-1, title);
    }

    public Event(int id, String title) {
        this.id = id;
        setTitle(title);
        checkRep();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        checkRep();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title == null) throw new IllegalArgumentException("Title cannot be null");
        if (title.isEmpty()) throw new IllegalArgumentException("Title cannot be empty");
        if (title.length() > TITLE_MAX_LENGTH) throw new IllegalArgumentException("Title cannot be longer than " + TITLE_MAX_LENGTH + " characters");

        this.title = title;
        checkRep();
    }

    @Override
    public String toString() {
        return "Event{" +
                "title='" + title + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;

        if (getId() != event.getId()) return false;
        return getTitle().equals(event.getTitle());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.title);
    }
}