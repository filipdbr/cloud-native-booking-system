// src/test/java/reservation/service/SeatServiceTest.java

package reservation.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reservation.model.Seat;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SeatServiceTest {

    // we instantiate SeatService directly — no Spring context needed
    private SeatService seatService;

    // runs before each test — gives us a fresh service with 10 available seats
    @BeforeEach
    void setUp() {
        seatService = new SeatService();
    }

    @Test
    void getAllSeats_shouldReturn100Seats() {
        List<Seat> seats = seatService.getAllSeats();
        assertEquals(100, seats.size());
    }

    @Test
    void getAllSeats_allSeatsShouldBeAvailableOnStart() {
        List<Seat> seats = seatService.getAllSeats();
        for (Seat seat : seats) {
            assertEquals("AVAILABLE", seat.getStatus());
        }
    }

    @Test
    void reserveSeat_shouldChangeStatusToReserved() {
        Seat seat = seatService.reserveSeat(1);
        assertEquals("RESERVED", seat.getStatus());
    }

    @Test
    void reserveSeat_shouldThrowException_whenSeatAlreadyReserved() {
        seatService.reserveSeat(1);

        // second reservation on the same seat should fail
        assertThrows(IllegalStateException.class, () -> {
            seatService.reserveSeat(1);
        });
    }

    @Test
    void reserveSeat_shouldThrowException_whenSeatDoesNotExist() {
        assertThrows(IllegalArgumentException.class, () -> {
            seatService.reserveSeat(101);
        });
    }

    @Test
    void cancelReservation_shouldChangeStatusToAvailable() {
        seatService.reserveSeat(1);
        Seat seat = seatService.cancelReservation(1);
        assertEquals("AVAILABLE", seat.getStatus());
    }

    @Test
    void cancelReservation_shouldThrowException_whenSeatNotReserved() {
        assertThrows(IllegalStateException.class, () -> {
            seatService.cancelReservation(1);
        });
    }

    @Test
    void cancelReservation_shouldThrowException_whenSeatDoesNotExist() {
        assertThrows(IllegalArgumentException.class, () -> {
            seatService.cancelReservation(101);
        });
    }
}