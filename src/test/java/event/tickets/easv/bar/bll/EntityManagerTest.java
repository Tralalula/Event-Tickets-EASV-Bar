package event.tickets.easv.bar.bll;

import event.tickets.easv.bar.be.Event;
import event.tickets.easv.bar.dal.dao.DAO;
import event.tickets.easv.bar.dal.dao.EventDAO;
import event.tickets.easv.bar.util.Result;
import event.tickets.easv.bar.util.Result.Success;
import event.tickets.easv.bar.util.Result.Failure;
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

    static class UnregisteredEntity {

    }

    @BeforeEach
    void setup() {
        entityManager = new EntityManager();
        mockEventDAO = mock(EventDAO.class);
        entityManager.registerDao(Event.class, mockEventDAO);
    }

    @Test
    void allSuccess() {
        // Setup
        var startDate = LocalDate.now();
        LocalDate endDate = null;
        var startTime = LocalTime.now();
        LocalTime endTime = null;
        var imageName = "sample.png";

        List<Event> expected = List.of(
                new Event(1, "a", startDate, endDate, startTime, endTime, imageName),
                new Event(2, "b", startDate, endDate, startTime, endTime, imageName)
        );
        when(mockEventDAO.all()).thenReturn(new Success<>(expected));

        // Call
        Result<List<Event>> result = entityManager.all(Event.class);

        assertThat(result).isInstanceOf(Success.class);
        var success = (Success<List<Event>>) result;
        assertThat(success.result()).isEqualTo(expected);
        verify(mockEventDAO).all();
    }

    @Test
    void allWrongEntity() {
        assertThrows(IllegalArgumentException.class, () -> entityManager.all(UnregisteredEntity.class));
    }
}