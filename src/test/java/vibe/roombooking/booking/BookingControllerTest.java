package vibe.roombooking.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
class BookingControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private BookingService bookingService;

  @Test
  void getAllBookings_returnsBookings() throws Exception {
    LocalDateTime start = LocalDateTime.now().plusHours(1);

    when(bookingService.getAllBookings())
        .thenReturn(
            List.of(
                new Booking(
                    1L, 1L, "Team meeting", start, start.plusHours(1), "user@example.com")));

    mockMvc
        .perform(get("/api/bookings"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(1))
        .andExpect(jsonPath("$[0].roomId").value(1))
        .andExpect(jsonPath("$[0].title").value("Team meeting"))
        .andExpect(jsonPath("$[0].email").value("user@example.com"));
  }

  @Test
  void getBookingsForRoom_returnsBookings() throws Exception {
    LocalDateTime start = LocalDateTime.now().plusHours(1);

    when(bookingService.getBookingsForRoom(1L))
        .thenReturn(
            List.of(
                new Booking(
                    1L, 1L, "Team meeting", start, start.plusHours(1), "user@example.com")));

    mockMvc
        .perform(get("/api/bookings/room/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].roomId").value(1))
        .andExpect(jsonPath("$[0].title").value("Team meeting"));
  }

  @Test
  void createBooking_returnsCreatedBooking() throws Exception {
    LocalDateTime start = LocalDateTime.now().plusHours(1);
    LocalDateTime end = start.plusHours(1);

    Booking savedBooking = new Booking(1L, 1L, "Team meeting", start, end, "user@example.com");

    when(bookingService.createBooking(any(Booking.class))).thenReturn(savedBooking);

    String requestBody =
        """
                {
                  "roomId": 1,
                  "title": "Team meeting",
                  "startTime": "%s",
                  "endTime": "%s",
                  "email": "user@example.com"
                }
                """
            .formatted(start, end);

    mockMvc
        .perform(post("/api/bookings").contentType(MediaType.APPLICATION_JSON).content(requestBody))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.roomId").value(1))
        .andExpect(jsonPath("$.title").value("Team meeting"))
        .andExpect(jsonPath("$.email").value("user@example.com"));
  }

  @Test
  void createBooking_returnsBadRequestForInvalidEmail() throws Exception {
    LocalDateTime start = LocalDateTime.now().plusHours(1);

    String requestBody =
        """
                {
                  "roomId": 1,
                  "title": "Team meeting",
                  "startTime": "%s",
                  "endTime": "%s",
                  "email": "not-an-email"
                }
                """
            .formatted(start, start.plusHours(1));

    mockMvc
        .perform(post("/api/bookings").contentType(MediaType.APPLICATION_JSON).content(requestBody))
        .andExpect(status().isBadRequest());
  }

  @Test
  void createBooking_returnsConflictWhenRoomIsAlreadyBooked() throws Exception {
    when(bookingService.createBooking(any(Booking.class)))
        .thenThrow(new BookingConflictException());

    LocalDateTime start = LocalDateTime.now().plusHours(1);

    String requestBody =
        """
                {
                  "roomId": 1,
                  "title": "Team meeting",
                  "startTime": "%s",
                  "endTime": "%s",
                  "email": "user@example.com"
                }
                """
            .formatted(start, start.plusHours(1));

    mockMvc
        .perform(post("/api/bookings").contentType(MediaType.APPLICATION_JSON).content(requestBody))
        .andExpect(status().isConflict());
  }

  @Test
  void createBooking_returnsBadRequestWhenRoomIdIsMissing() throws Exception {
    LocalDateTime start = LocalDateTime.now().plusHours(1);

    String requestBody =
        """
            {
              "title": "Team meeting",
              "startTime": "%s",
              "endTime": "%s",
              "email": "user@example.com"
            }
            """
            .formatted(start, start.plusHours(1));

    mockMvc
        .perform(post("/api/bookings").contentType(MediaType.APPLICATION_JSON).content(requestBody))
        .andExpect(status().isBadRequest());
  }

  @Test
  void createBooking_returnsBadRequestWhenRoomIdIsNotPositive() throws Exception {
    LocalDateTime start = LocalDateTime.now().plusHours(1);

    String requestBody =
        """
            {
              "roomId": 0,
              "title": "Team meeting",
              "startTime": "%s",
              "endTime": "%s",
              "email": "user@example.com"
            }
            """
            .formatted(start, start.plusHours(1));

    mockMvc
        .perform(post("/api/bookings").contentType(MediaType.APPLICATION_JSON).content(requestBody))
        .andExpect(status().isBadRequest());
  }
}
