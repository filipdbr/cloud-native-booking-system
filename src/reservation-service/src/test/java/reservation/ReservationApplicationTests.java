package reservation;

import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import reservation.repository.SeatRepository;

@SpringBootTest
class ReservationApplicationTests {

    // mock SeatRepository so Spring doesn't need a real database to start
    @MockBean
    private SeatRepository seatRepository;

    @Test
    void contextLoads() {
        // verifies that the Spring context starts without errors
    }
}