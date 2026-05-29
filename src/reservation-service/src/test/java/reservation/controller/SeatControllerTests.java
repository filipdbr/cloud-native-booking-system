package reservation.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import reservation.model.Seat;
import reservation.service.SeatService;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// loads only the web layer — no full Spring context, much faster than @SpringBootTest
@WebMvcTest(SeatController.class)
class SeatControllerTest {

    // MockMvc simulates HTTP requests without starting a real server
    @Autowired
    private MockMvc mockMvc;

    // @MockBean replaces the real SeatService with a mock —
    // we test the controller in isolation, not the business logic
    @MockBean
    private SeatService seatService;

    @Test
    void getAllSeats_shouldReturn200AndListOfSeats() throws Exception {
        when(seatService.getAllSeats()).thenReturn(List.of(
                new Seat(1, "AVAILABLE"),
                new Seat(2, "RESERVED")
        ));

        mockMvc.perform(get("/api/reservation/seats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].seatNumber").value(1))
                .andExpect(jsonPath("$[0].status").value("AVAILABLE"))
                .andExpect(jsonPath("$[1].seatNumber").value(2))
                .andExpect(jsonPath("$[1].status").value("RESERVED"));
    }

    @Test
    void reserveSeat_shouldReturn200AndReservedSeat() throws Exception {
        when(seatService.reserveSeat(1)).thenReturn(new Seat(1, "RESERVED"));

        mockMvc.perform(post("/api/reservation/seats/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.seatNumber").value(1))
                .andExpect(jsonPath("$.status").value("RESERVED"));
    }

    @Test
    void reserveSeat_shouldReturn500_whenSeatAlreadyReserved() throws Exception {
        when(seatService.reserveSeat(1)).thenThrow(new IllegalStateException("Seat already reserved"));

        mockMvc.perform(post("/api/reservation/seats/1"))
                .andExpect(status().isConflict());
    }

    @Test
    void reserveSeat_shouldReturn500_whenSeatDoesNotExist() throws Exception {
        when(seatService.reserveSeat(99)).thenThrow(new IllegalArgumentException("Seat does not exist"));

        mockMvc.perform(post("/api/reservation/seats/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void cancelReservation_shouldReturn200AndAvailableSeat() throws Exception {
        when(seatService.cancelReservation(1)).thenReturn(new Seat(1, "AVAILABLE"));

        mockMvc.perform(delete("/api/reservation/seats/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.seatNumber").value(1))
                .andExpect(jsonPath("$.status").value("AVAILABLE"));
    }

    @Test
    void cancelReservation_shouldReturn500_whenSeatNotReserved() throws Exception {
        when(seatService.cancelReservation(1)).thenThrow(new IllegalStateException("Seat not reserved"));

        mockMvc.perform(delete("/api/reservation/seats/1"))
                .andExpect(status().isConflict());
    }
}