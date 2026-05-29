package reservation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reservation.model.Seat;
import reservation.service.SeatService;

import java.util.List;

// @RestController = @Controller + @ResponseBody
// tells Spring this class handles HTTP requests and returns JSON automatically
@RestController
// all endpoints in this class are prefixed with /api/reservation
@RequestMapping("/api/reservation")
public class SeatController {

    // Spring injects SeatService automatically via constructor injection
    private final SeatService seatService;

    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }

    // GET /api/reservation/seats — returns all seats with their status
    @GetMapping("/seats")
    public ResponseEntity<List<Seat>> getAllSeats() {
        return ResponseEntity.ok(seatService.getAllSeats());
    }

    // POST /api/reservation/seats/{seatNumber} — reserves a seat
    @PostMapping("/seats/{seatNumber}")
    public ResponseEntity<Seat> reserveSeat(@PathVariable int seatNumber) {
        Seat seat = seatService.reserveSeat(seatNumber);
        return ResponseEntity.ok(seat);
    }

    // DELETE /api/reservation/seats/{seatNumber} — cancels a reservation
    @DeleteMapping("/seats/{seatNumber}")
    public ResponseEntity<Seat> cancelReservation(@PathVariable int seatNumber) {
        Seat seat = seatService.cancelReservation(seatNumber);
        return ResponseEntity.ok(seat);
    }

    // na końcu klasy, przed ostatnim }
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFound(IllegalArgumentException e) {
        return e.getMessage();
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleConflict(IllegalStateException e) {
        return e.getMessage();
    }
}