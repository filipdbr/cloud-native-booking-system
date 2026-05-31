package reservation.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reservation.model.Seat;
import reservation.repository.SeatRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// @ExtendWith loads Mockito without Spring context — fast, lightweight
@ExtendWith(MockitoExtension.class)
class SeatServiceTest {

    // @Mock creates a fake SeatRepository — no real database needed
    @Mock
    private SeatRepository seatRepository;

    // @InjectMocks creates SeatService and injects the mock repository into it
    @InjectMocks
    private SeatService seatService;

    @Test
    void getAllSeats_shouldReturnAllSeats() {
        when(seatRepository.findAll()).thenReturn(List.of(
                new Seat(1, "AVAILABLE"),
                new Seat(2, "RESERVED")
        ));

        List<Seat> seats = seatService.getAllSeats();
        assertEquals(2, seats.size());
    }

    @Test
    void reserveSeat_shouldChangeStatusToReserved() {
        when(seatRepository.findBySeatNumber(1))
                .thenReturn(Optional.of(new Seat(1, "AVAILABLE")));
        when(seatRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Seat seat = seatService.reserveSeat(1);
        assertEquals("RESERVED", seat.getStatus());
    }

    @Test
    void reserveSeat_shouldThrowException_whenSeatAlreadyReserved() {
        when(seatRepository.findBySeatNumber(1))
                .thenReturn(Optional.of(new Seat(1, "RESERVED")));

        assertThrows(IllegalStateException.class, () -> seatService.reserveSeat(1));
    }

    @Test
    void reserveSeat_shouldThrowException_whenSeatDoesNotExist() {
        when(seatRepository.findBySeatNumber(99))
                .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> seatService.reserveSeat(99));
    }

    @Test
    void cancelReservation_shouldChangeStatusToAvailable() {
        when(seatRepository.findBySeatNumber(1))
                .thenReturn(Optional.of(new Seat(1, "RESERVED")));
        when(seatRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Seat seat = seatService.cancelReservation(1);
        assertEquals("AVAILABLE", seat.getStatus());
    }

    @Test
    void cancelReservation_shouldThrowException_whenSeatNotReserved() {
        when(seatRepository.findBySeatNumber(1))
                .thenReturn(Optional.of(new Seat(1, "AVAILABLE")));

        assertThrows(IllegalStateException.class, () -> seatService.cancelReservation(1));
    }

    @Test
    void cancelReservation_shouldThrowException_whenSeatDoesNotExist() {
        when(seatRepository.findBySeatNumber(99))
                .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> seatService.cancelReservation(99));
    }
}