package event.tickets.easv.bar.bll;

import event.tickets.easv.bar.be.Entity;
import event.tickets.easv.bar.be.Event;
import event.tickets.easv.bar.be.User;
import event.tickets.easv.bar.dal.dao.DAO;
import event.tickets.easv.bar.dal.dao.EventDAO;
import event.tickets.easv.bar.dal.dao.EventUserDAO;
import event.tickets.easv.bar.dal.dao.UserDAO;
import event.tickets.easv.bar.dal.database.EntityAssociation;
import event.tickets.easv.bar.util.FailureType;
import event.tickets.easv.bar.util.Result;
import event.tickets.easv.bar.util.Result.Success;
import event.tickets.easv.bar.util.Result.Failure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class EntityManagerTest {
    private EntityManager entityManager;
    private DAO<Event> mockEventDAO;
    private DAO<User> mockUserDAO;
    private EntityAssociation<Event, User> mockEventUserAssociation;

    static class UnregisteredEntity implements Entity<UnregisteredEntity> {
        @Override
        public void update(UnregisteredEntity updatedData) {}

        @Override
        public int id() {
            return 0;
        }

        @Override
        public void setAssociations(List<?> associations) {}
    }

    @BeforeEach
    void setup() {
        entityManager = new EntityManager();
        entityManager.purge();

        mockEventDAO = mock(EventDAO.class);
        mockUserDAO = mock(UserDAO.class);
        mockEventUserAssociation = mock(EventUserDAO.class);


        entityManager.registerDao(Event.class, mockEventDAO);
        entityManager.registerDao(User.class, mockUserDAO);
        entityManager.registerAssociation(Event.class, User.class, mockEventUserAssociation);
    }

    @Test
    void getEventSuccess() {
        // Setup
        var imageName = "sample.png";
        var location = "6700, Esbjerg";
        var startDate = LocalDate.now();
        LocalDate endDate = null;
        var startTime = LocalTime.now();
        LocalTime endTime = null;

        var event = new Event(1, "a", imageName, location, startDate, endDate, startTime, endTime, "", "");
        when(mockEventDAO.get(1)).thenReturn(Success.of(Optional.of(event)));

        // Call
        Result<Optional<Event>> result = entityManager.get(Event.class, 1);

        // Check
        assertThat(result).isInstanceOf(Success.class);
        var success = (Success<Optional<Event>>) result;
        assertThat(success.result()).isEqualTo(Optional.of(event));
        verify(mockEventDAO).get(1);
    }

    @Test
    void getEventSuccessEmpty() {
        // Setup
        when(mockEventDAO.get(1)).thenReturn(Success.of(Optional.empty()));

        // Call
        Result<Optional<Event>> result = entityManager.get(Event.class, 1);

        // Check
        assertThat(result).isInstanceOf(Success.class);
        var success = (Success<Optional<Event>>) result;
        assertThat(success.result()).isEqualTo(Optional.empty());
        verify(mockEventDAO).get(1);
    }

    @Test
    void getEventWithAssociationsSuccess() {
        // Setup
        var imageName = "sample.png";
        var location = "6700, Esbjerg";
        var startDate = LocalDate.now();
        LocalDate endDate = null;
        var startTime = LocalTime.now();
        LocalTime endTime = null;

        var event = new Event(1, "a", imageName, location, startDate, endDate, startTime, endTime, "", "");
        when(mockEventDAO.get(1)).thenReturn(Success.of(Optional.of(event)));
        when(mockEventUserAssociation.findAssociatesOf(event)).thenReturn(Success.of(List.of()));

        // Call
        Result<Optional<Event>> result = entityManager.getWithAssociations(Event.class, 1);

        // Check
        assertThat(result).isInstanceOf(Success.class);
        var success = (Success<Optional<Event>>) result;
        assertThat(success.result()).isEqualTo(Optional.of(event));
        verify(mockEventDAO).get(1);
        verify(mockEventUserAssociation).findAssociatesOf(event);
    }

    @Test
    void getEventWithAssociationsUsers() {
        // Setup
        var imageName = "sample.png";
        var location = "6700, Esbjerg";
        var startDate = LocalDate.now();
        LocalDate endDate = null;
        var startTime = LocalTime.now();
        LocalTime endTime = null;

        var event = new Event(1, "a", imageName, location, startDate, endDate, startTime, endTime, "", "");
        when(mockEventDAO.get(1)).thenReturn(Success.of(Optional.of(event)));
        List<User> usersForEvent = List.of(new User(1, "User1"), new User(2, "User2"));
        when(mockEventUserAssociation.findAssociatesOf(event)).thenReturn(Success.of(usersForEvent));

        // Call
        Result<Optional<Event>> result = entityManager.getWithAssociations(Event.class, 1);

        // Check
        assertThat(result).isInstanceOf(Success.class);
        var success = (Success<Optional<Event>>) result;
        assertThat(success.result()).isEqualTo(Optional.of(event));
        assertThat(success.result().get().users()).isEqualTo(usersForEvent);
        verify(mockEventDAO).get(1);
        verify(mockEventUserAssociation).findAssociatesOf(event);
    }

    @Test
    void allEventSuccess() {
        // Setup
        var imageName = "sample.png";
        var location = "6700, Esbjerg";
        var startDate = LocalDate.now();
        LocalDate endDate = null;
        var startTime = LocalTime.now();
        LocalTime endTime = null;

        List<Event> expected = List.of(
                new Event(1, "a", imageName, location, startDate, endDate, startTime, endTime, "", ""),
                new Event(2, "b", imageName, location, startDate, endDate, startTime, endTime, "", "")
        );
        when(mockEventDAO.all()).thenReturn(Success.of(expected));

        // Call
        Result<List<Event>> result = entityManager.all(Event.class);

        // Check
        assertThat(result).isInstanceOf(Success.class);
        var success = (Success<List<Event>>) result;
        assertThat(success.result()).isEqualTo(expected);
        verify(mockEventDAO).all();
    }

    @Test
    void allUserSuccess() {
        // Setup
        List<User> expected = List.of(
                new User(1, "test"),
                new User(2, "Kakao")
        );
        when(mockUserDAO.all()).thenReturn(Success.of(expected));

        // Call
        Result<List<User>> result = entityManager.all(User.class);

        assertThat(result).isInstanceOf(Success.class);
        var success = (Success<List<User>>) result;
        assertThat(success.result()).isEqualTo(expected);
        verify(mockUserDAO).all();
    }

    @Test
    void allWrongEntity() {
        assertThrows(IllegalArgumentException.class, () -> entityManager.allWithAssociations(UnregisteredEntity.class));
    }

    @Test
    void allWithAssociationsEventSuccess() {
        // Setup
        var imageName = "sample.png";
        var location = "6700, Esbjerg";
        var startDate = LocalDate.now();
        LocalDate endDate = null;
        var startTime = LocalTime.now();
        LocalTime endTime = null;

        List<Event> expected = List.of(
                new Event(1, "a", imageName, location, startDate, endDate, startTime, endTime, "", ""),
                new Event(2, "b", imageName, location, startDate, endDate, startTime, endTime, "", "")
        );
        when(mockEventDAO.all()).thenReturn(Success.of(expected));
        when(mockEventUserAssociation.findAssociatesOf(any(Event.class))).thenReturn(Success.of(List.of()));

        // Call
        Result<List<Event>> result = entityManager.allWithAssociations(Event.class);

        assertThat(result).isInstanceOf(Success.class);
        var success = (Success<List<Event>>) result;
        assertThat(success.result()).isEqualTo(expected);
        verify(mockEventDAO).all();
        expected.forEach(event -> verify(mockEventUserAssociation).findAssociatesOf(event));
    }

    @Test
    void allWithAssociationsUserSuccess() {
        // Setup
        List<User> expected = List.of(
                new User(1, "test"),
                new User(2, "Kakao")
        );
        when(mockUserDAO.all()).thenReturn(Success.of(expected));
        when(mockEventUserAssociation.findAssociatesOf(any(User.class))).thenReturn(Success.of(List.of()));

        // Call
        Result<List<User>> result = entityManager.allWithAssociations(User.class);

        assertThat(result).isInstanceOf(Success.class);
        var success = (Success<List<User>>) result;
        assertThat(success.result()).isEqualTo(expected);
        verify(mockUserDAO).all();
        expected.forEach(user -> verify(mockEventUserAssociation).findAssociatesOf(user));
    }

    @Test
    void allWithAssociationsEventsForUser() {
        // Setup
        var imageName = "sample.png";
        var location = "6700, Esbjerg";
        var startDate = LocalDate.now();
        LocalDate endDate = null;
        var startTime = LocalTime.now();
        LocalTime endTime = null;

        List<Event> expected = List.of(
                new Event(1, "a", imageName, location, startDate, endDate, startTime, endTime, "", "")
        );
        when(mockEventDAO.all()).thenReturn(Success.of(expected));
        List<User> usersForEvent = List.of(new User(1, "User1"), new User(2, "User2"));
        when(mockEventUserAssociation.findAssociatesOf(any(Event.class))).thenReturn(Success.of(usersForEvent));

        // Call
        Result<List<Event>> result = entityManager.allWithAssociations(Event.class);

        // Check
        assertThat(result).isInstanceOf(Success.class);
        var success = (Success<List<Event>>) result;
        List<Event> events = success.result();

        assertThat(events).hasSize(expected.size());
        events.forEach(event -> assertThat(event.users()).isEqualTo(usersForEvent));

        verify(mockEventDAO).all();
        events.forEach(event -> verify(mockEventUserAssociation).findAssociatesOf(event));
    }

    @Test
    void allWithAssociationsUsersForEvent() {
        // Setup
        var imageName = "sample.png";
        var location = "6700, Esbjerg";
        var startDate = LocalDate.now();
        LocalDate endDate = null;
        var startTime = LocalTime.now();
        LocalTime endTime = null;

        List<Event> expected = List.of(
                new Event(1, "a", imageName, location, startDate, endDate, startTime, endTime, "", "")
        );
        when(mockEventDAO.all()).thenReturn(Success.of(expected));
        List<User> usersForEvent = List.of(new User(1, "User1"), new User(2, "User2"));
        when(mockEventUserAssociation.findAssociatesOf(any(Event.class))).thenReturn(Success.of(usersForEvent));

        // Call
        Result<List<Event>> result = entityManager.allWithAssociations(Event.class);

        // Check
        assertThat(result).isInstanceOf(Success.class);
        var success = (Success<List<Event>>) result;
        List<Event> events = success.result();

        assertThat(events).hasSize(expected.size());
        events.forEach(event -> assertThat(event.users()).isEqualTo(usersForEvent));

        verify(mockEventDAO).all();
        events.forEach(event -> verify(mockEventUserAssociation).findAssociatesOf(event));
    }

    @Test
    void addEntityIsNull() {
        // Call
        Result<Event> result = entityManager.add(null);

        // Check
        assertThat(result).isInstanceOf(Failure.class);
        Failure<Event> failure = (Failure<Event>) result;
        assertThat(failure.type()).isEqualTo(FailureType.INVALID_ENTITY_TYPE);
        assertThat(failure.message()).isEqualTo("Entity cannot be null");
    }

    @Test
    void addEntityUnexpectedEntity() {
        // Setup
        var entity = new UnregisteredEntity();

        // Call
        Result<UnregisteredEntity> result = entityManager.add(entity);

        // Check
        assertThat(result).isInstanceOf(Failure.class);
        Failure<UnregisteredEntity> failure = (Failure<UnregisteredEntity>) result;
        assertThat(failure.type()).isEqualTo(FailureType.INVALID_ENTITY_TYPE);
        assertThat(failure.message()).contains("Unexpected entity");
    }

    @Test
    void addEntitySuccess() {
        // Setup
        var event = new Event(1, "Test Event", "sample.png", "6700, Esbjerg", LocalDate.now(), null, LocalTime.now(), null, "", "");
        when(mockEventDAO.add(event)).thenReturn(Success.of(event));

        // Call
        Result<Event> result = entityManager.add(event);

        // Check
        assertThat(result).isInstanceOf(Success.class);
        Success<Event> success = (Success<Event>) result;
        assertThat(success.result()).isEqualTo(event);
        verify(mockEventDAO).add(event);
    }

    @Test
    void addAssociationsSuccess() {
        // Setup
        Event event = new Event(1, "Test Event", "sample.png", "6700, Esbjerg", LocalDate.now(), null, LocalTime.now(), null, "", "");
        List<User> users = List.of(new User(1, "User1"), new User(2, "User2"));
        when(mockEventUserAssociation.addAssociation(eq(event), any(User.class))).thenReturn(Success.of(true));

        // Call
        Result<Boolean> result = entityManager.addAssociations(event, users);

        // Check
        assertThat(result).isInstanceOf(Success.class);
        Success<Boolean> success = (Success<Boolean>) result;
        assertThat(success.result()).isTrue();
        users.forEach(user -> verify(mockEventUserAssociation).addAssociation(event, user));
    }

    @Test
    void addAssociationsFailure() {
        // Setup
        Event event = new Event(1, "Test Event", "sample.png", "6700, Esbjerg", LocalDate.now(), null, LocalTime.now(), null, "", "");
        List<User> users = List.of(new User(1, "User1"));
        when(mockEventUserAssociation.addAssociation(eq(event), any(User.class)))
                                     .thenReturn(Failure.of(FailureType.DB_INSERTION_FAILURE, "Failed to add association"));

        // Call
        Result<Boolean> result = entityManager.addAssociations(event, users);

        // Check
        assertThat(result).isInstanceOf(Failure.class);
        Failure<Boolean> failure = (Failure<Boolean>) result;
        assertThat(failure.type()).isEqualTo(FailureType.DB_INSERTION_FAILURE);
        assertThat(failure.message()).contains("Failed to add association");
        verify(mockEventUserAssociation).addAssociation(eq(event), any(User.class));
    }

    @Test
    void addSingleAssociationSuccess() {
        //Setup
        Event event = new Event(1, "Event", "image.png", "Some Location", LocalDate.now(), null, LocalTime.now(), null, "", "");
        User user = new User(1, "User1");
        when(mockEventUserAssociation.addAssociation(event, user)).thenReturn(Success.of(true));

        // Call
        Result<Boolean> result = entityManager.addAssociation(event, user);

        // Check
        assertThat(result).isInstanceOf(Success.class);
        verify(mockEventUserAssociation).addAssociation(event, user);
    }

    @Test
    void addSingleAssociationFailure() {
        // Setup
        Event event = new Event(1, "Event", "image.png", "Some Location", LocalDate.now(), null, LocalTime.now(), null, "", "");
        User user = new User(1, "User1");
        when(mockEventUserAssociation.addAssociation(event, user)).thenReturn(Failure.of(FailureType.DB_INSERTION_FAILURE, "Error"));

        // Call
        Result<Boolean> result = entityManager.addAssociation(event, user);

        // Check
        assertThat(result).isInstanceOf(Failure.class);
        verify(mockEventUserAssociation).addAssociation(event, user);
    }

    @Test
    void updateEntitySuccess() {
        // Setup
        Event event = new Event(1, "Original Event", "original.png", "Original Location", LocalDate.now(), null, LocalTime.now(), null, "", "");
        Event updatedEvent = new Event(1, "Updated Event", "updated.png", "Updated Location", LocalDate.now(), null, LocalTime.now(), null, "", "");
        when(mockEventDAO.update(event, updatedEvent)).thenReturn(Success.of(true));

        // Call
        Result<Boolean> result = entityManager.update(event, updatedEvent);

        // Check
        assertThat(result).isInstanceOf(Success.class);
        Success<Boolean> success = (Success<Boolean>) result;
        assertThat(success.result()).isTrue();
        verify(mockEventDAO).update(event, updatedEvent);
    }

    @Test
    void updateEntityWithNulls() {
        // Setup
        Event event = new Event(1, "Original Event", "original.png", "Original Location", LocalDate.now(), null, LocalTime.now(), null, "", "");

        // Call & check
        Result<Boolean> resultWithNullOriginal = entityManager.update(null, event);
        assertThat(resultWithNullOriginal).isInstanceOf(Failure.class);

        // Call & check
        Result<Boolean> resultWithNullUpdated = entityManager.update(event, null);
        assertThat(resultWithNullUpdated).isInstanceOf(Failure.class);
    }

    @Test
    void updateUnsupportedEntityType() {
        // Setup
        UnregisteredEntity originalEntity = new UnregisteredEntity();
        UnregisteredEntity updatedEntity = new UnregisteredEntity();

        // Call
        Result<Boolean> result = entityManager.update(originalEntity, updatedEntity);

        // Check
        assertThat(result).isInstanceOf(Failure.class);
        Failure<Boolean> failure = (Failure<Boolean>) result;
        assertThat(failure.message()).contains("Unexpected entity");
    }
}