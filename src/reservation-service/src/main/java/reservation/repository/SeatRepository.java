package reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import reservation.model.Seat;

import java.util.Optional;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    // Spring generates the SQL automatically from the method name:
    // SELECT * FROM seats WHERE seat_number = ?
    Optional<Seat> findBySeatNumber(int seatNumber);
}
