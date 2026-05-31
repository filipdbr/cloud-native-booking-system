package reservation.service;

import org.springframework.stereotype.Service;
import reservation.model.Seat;
import reservation.repository.SeatRepository;

import java.util.List;

@Service
public class SeatService {

    // Spring injects SeatRepository automatically via constructor injection
    private final SeatRepository seatRepository;

    public SeatService(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    // on first startup, populate the database with 10 available seats if empty
    @jakarta.annotation.PostConstruct
    public void initSeats() {
        if (seatRepository.count() == 0) {
            for (int i = 1; i <= 10; i++) {
                seatRepository.save(new Seat(i, "AVAILABLE"));
            }
        }
    }

    // returns all seats with their current status from the database
    public List<Seat> getAllSeats() {
        return seatRepository.findAll();
    }

    // reserves a seat if it exists and is available
    public Seat reserveSeat(int seatNumber) {
        Seat seat = seatRepository.findBySeatNumber(seatNumber)
                .orElseThrow(() -> new IllegalArgumentException("Seat " + seatNumber + " does not exist"));

        if (seat.getStatus().equals("RESERVED")) {
            throw new IllegalStateException("Seat " + seatNumber + " is already reserved");
        }

        seat.setStatus("RESERVED");
        return seatRepository.save(seat);  // save updated seat back to database
    }

    // cancels a reservation if the seat exists and is currently reserved
    public Seat cancelReservation(int seatNumber) {
        Seat seat = seatRepository.findBySeatNumber(seatNumber)
                .orElseThrow(() -> new IllegalArgumentException("Seat " + seatNumber + " does not exist"));

        if (seat.getStatus().equals("AVAILABLE")) {
            throw new IllegalStateException("Seat " + seatNumber + " is not reserved");
        }

        seat.setStatus("AVAILABLE");
        return seatRepository.save(seat);
    }
}