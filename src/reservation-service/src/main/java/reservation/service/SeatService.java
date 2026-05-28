// src/main/java/reservation/service/SeatService.java

package reservation.service;

import org.springframework.stereotype.Service;
import reservation.model.Seat;

import java.util.ArrayList;
import java.util.List;

// @Service tells Spring to manage this class as a bean —
// Spring creates one instance and injects it wherever it's needed
@Service
public class SeatService {

    // in-memory storage — will be replaced by PostgreSQL later
    private final List<Seat> seats = new ArrayList<>();

    // constructor runs once when Spring creates the service
    // initializes 100 seats, all available by default
    public SeatService() {
        for (int i = 1; i <= 100; i++) {
            seats.add(new Seat(i, "AVAILABLE"));
        }
    }

    // returns all seats with their current status
    public List<Seat> getAllSeats() {
        return seats;
    }

    // reserves a seat if it exists and is available
    // throws exception if seat doesn't exist or is already reserved
    public Seat reserveSeat(int seatNumber) {
        Seat seat = findSeat(seatNumber);
        if (seat == null) {
            throw new IllegalArgumentException("Seat " + seatNumber + " does not exist");
        }
        if (seat.getStatus().equals("RESERVED")) {
            throw new IllegalStateException("Seat " + seatNumber + " is already reserved");
        }
        seat.setStatus("RESERVED");
        return seat;
    }

    // cancels a reservation if the seat exists and is currently reserved
    public Seat cancelReservation(int seatNumber) {
        Seat seat = findSeat(seatNumber);
        if (seat == null) {
            throw new IllegalArgumentException("Seat " + seatNumber + " does not exist");
        }
        if (seat.getStatus().equals("AVAILABLE")) {
            throw new IllegalStateException("Seat " + seatNumber + " is not reserved");
        }
        seat.setStatus("AVAILABLE");
        return seat;
    }

    // helper method — finds a seat by number using Java streams
    // returns null if no seat with the given number exists
    private Seat findSeat(int seatNumber) {
        for (Seat s : seats) {
            if (s.getSeatNumber() == seatNumber) {
                return s;
            }
        }
        return null;
    }

}