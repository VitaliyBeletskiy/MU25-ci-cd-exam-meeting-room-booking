package vibe.roombooking.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BookingApiIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @Test
  void createBooking_createsBooking() throws Exception {
    LocalDateTime start = LocalDateTime.now().plusHours(1);
    LocalDateTime end = start.plusHours(1);

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
  void getAllBookings_returnsCreatedBooking() throws Exception {
    LocalDateTime start = LocalDateTime.now().plusHours(1);
    LocalDateTime end = start.plusHours(1);

    createBooking(start, end);

    mockMvc
        .perform(get("/api/bookings"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(1))
        .andExpect(jsonPath("$[0].title").value("Team meeting"));
  }

  @Test
  void getBookingsForRoom_returnsOnlyBookingsForRoom() throws Exception {
    LocalDateTime start = LocalDateTime.now().plusHours(1);

    createBooking(start, start.plusHours(1));

    mockMvc
        .perform(get("/api/bookings/room/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(1))
        .andExpect(jsonPath("$[0].roomId").value(1));
  }

  @Test
  void createBooking_returnsConflictWhenBookingsOverlap() throws Exception {
    LocalDateTime start = LocalDateTime.now().plusHours(2);

    createBooking(start, start.plusHours(1));

    String overlappingRequest =
        """
                {
                  "roomId": 1,
                  "title": "Overlapping meeting",
                  "startTime": "%s",
                  "endTime": "%s",
                  "email": "other@example.com"
                }
                """
            .formatted(start.plusMinutes(30), start.plusHours(1).plusMinutes(30));

    mockMvc
        .perform(
            post("/api/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(overlappingRequest))
        .andExpect(status().isConflict());
  }

  @Test
  void createBooking_returnsNotFoundWhenRoomDoesNotExist() throws Exception {
    LocalDateTime start = LocalDateTime.now().plusHours(1);

    String requestBody =
        """
                {
                  "roomId": 99,
                  "title": "Team meeting",
                  "startTime": "%s",
                  "endTime": "%s",
                  "email": "user@example.com"
                }
                """
            .formatted(start, start.plusHours(1));

    mockMvc
        .perform(post("/api/bookings").contentType(MediaType.APPLICATION_JSON).content(requestBody))
        .andExpect(status().isNotFound());
  }

  private void createBooking(LocalDateTime start, LocalDateTime end) throws Exception {
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
        .andExpect(status().isCreated());
  }
}
