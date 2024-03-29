package event.tickets.easv.bar.bll;

import event.tickets.easv.bar.be.Entity;
import event.tickets.easv.bar.be.Event;
import event.tickets.easv.bar.be.User;
import event.tickets.easv.bar.dal.dao.DAO;
import event.tickets.easv.bar.dal.dao.EventDAO;
import event.tickets.easv.bar.dal.dao.EventUserDAO;
import event.tickets.easv.bar.dal.dao.UserDAO;
import event.tickets.easv.bar.dal.database.EntityAssociation;
import event.tickets.easv.bar.util.Result;
import event.tickets.easv.bar.util.Result.Success;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

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
        public void update(UnregisteredEntity updatedDate) {}

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
        mockEventDAO = mock(EventDAO.class);
        mockUserDAO = mock(UserDAO.class);
        mockEventUserAssociation = mock(EventUserDAO.class);

        entityManager.registerDao(Event.class, mockEventDAO);
        entityManager.registerDao(User.class, mockUserDAO);
        entityManager.registerAssociation(Event.class, User.class, mockEventUserAssociation);
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
}