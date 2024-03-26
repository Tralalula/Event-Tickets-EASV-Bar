package event.tickets.easv.bar.be;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class Event {
    private static final int TITLE_MAX_LENGTH = 255;

    private int id;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String imageName;

    private void checkRep() {
        assert title != null : "title must not be null";
        assert startDate != null : "startDate must not be null";
        assert startTime != null : "startTime must not be null";
        assert imageName != null : "imageName must not be null";

        assert !title.isEmpty() : "title must not be empty";
        assert title.length() <= TITLE_MAX_LENGTH : "title must not exceed " + TITLE_MAX_LENGTH + " characters";
    }

    public Event(String title, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime, String imageName) {
        this(-1, title, startDate, endDate, startTime, endTime, imageName);
    }

    public Event(int id, String title, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime, String imageName) {
        this.id = id;
        setTitle(title);
        setStartDate(startDate);
        setEndDate(endDate);
        setStartTime(startTime);
        setEndTime(endTime);
        setImageName(imageName);
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
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
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