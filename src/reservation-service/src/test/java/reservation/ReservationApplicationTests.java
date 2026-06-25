package reservation;

import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import reservation.repository.SeatRepository;

@SpringBootTest
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=none",
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect"
})
class ReservationApplicationTests {

    // mock SeatRepository so Spring doesn't need a real database to start
    @MockBean
    private SeatRepository seatRepository;

    @Test
    void contextLoads() {
        // verifies that the Spring context starts without errors
    }
}