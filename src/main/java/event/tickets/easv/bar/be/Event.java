package event.tickets.easv.bar.be;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class Event implements Entity<Event> {
    private static final int TITLE_MAX_LENGTH = 255;

    private int id;
    private String title;
    private String imageName;
    private String location;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String locationGuidance;
    private String extraInfo;

    private void checkRep() {
        assert title != null : "title must not be null";
        assert imageName != null : "imageName must not be null";
        assert location != null : "location must not be null";
        assert startDate != null : "startDate must not be null";
        assert startTime != null : "startTime must not be null";

        assert !title.isEmpty() : "title must not be empty";
        assert title.length() <= TITLE_MAX_LENGTH : "title must not exceed " + TITLE_MAX_LENGTH + " characters";
    }

    public Event(String title,
                 String imageName,
                 String location,
                 LocalDate startDate,
                 LocalDate endDate,
                 LocalTime startTime,
                 LocalTime endTime,
                 String locationGuidance,
                 String extraInfo) {
        this(-1, title, imageName, location, startDate, endDate, startTime, endTime, locationGuidance, extraInfo);
    }

    public Event(int id, Event event) {
        this(id, event.title(), event.imageName(), event.location(), event.startDate, event.endDate, event.startTime, event.endTime, event.locationGuidance(), event.extraInfo());
    }

    public Event(int id,
                 String title,
                 String imageName,
                 String location,
                 LocalDate startDate,
                 LocalDate endDate,
                 LocalTime startTime,
                 LocalTime endTime,
                 String locationGuidance,
                 String extraInfo) {
        this.id = id;
        setTitle(title);
        setImageName(imageName);
        setLocation(location);
        setStartDate(startDate);
        setEndDate(endDate);
        setStartTime(startTime);
        setEndTime(endTime);
        setLocationGuidance(locationGuidance);
        setExtraInfo(extraInfo);
        checkRep();
    }

    @Override
    public void update(Event updatedDate) {
        setTitle(updatedDate.title());
        setImageName(updatedDate.imageName());
        setLocation(updatedDate.location());
        setStartDate(updatedDate.startDate());
        setEndDate(updatedDate.endDate());
        setStartTime(updatedDate.startTime());
        setEndTime(updatedDate.endTime());
        setLocationGuidance(updatedDate.locationGuidance());
        setExtraInfo(updatedDate.extraInfo());
    }

    public int id() {
        return id;
    }

    public String title() {
        return title;
    }

    public String imageName() {
        return imageName;
    }


    public String location() {
        return location;
    }


    public LocalDate startDate() {
        return startDate;
    }


    public LocalDate endDate() {
        return endDate;
    }


    public LocalTime startTime() {
        return startTime;
    }


    public LocalTime endTime() {
        return endTime;
    }


    public String locationGuidance() {
        return locationGuidance;
    }


    public String extraInfo() {
        return extraInfo;
    }


    // Setters
    public void setId(int id) {
        this.id = id;
    }

    private void setTitle(String title) {
        if (title == null) throw new IllegalArgumentException("Title cannot be null");
        if (title.isEmpty()) throw new IllegalArgumentException("Title cannot be empty");
        if (title.length() > TITLE_MAX_LENGTH) throw new IllegalArgumentException("Title cannot be longer than " + TITLE_MAX_LENGTH + " characters");

        this.title = title;
    }

    private void setImageName(String imageName) {
        this.imageName = imageName;
    }

    private void setLocation(String location) {
        this.location = location;
    }

    private void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    private void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    private void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    private void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    private void setLocationGuidance(String locationGuidance) {
        this.locationGuidance = locationGuidance;
    }

    private void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
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

        if (id() != event.id()) return false;
        return title().equals(event.title());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.title);
    }
}