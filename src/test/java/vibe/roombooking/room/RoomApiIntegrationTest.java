package vibe.roombooking.room;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RoomApiIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @Test
  void getAllRooms_returnsAllRooms() throws Exception {
    mockMvc
        .perform(get("/api/rooms"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(3))
        .andExpect(jsonPath("$[0].name").value("Alpha"))
        .andExpect(jsonPath("$[1].name").value("Beta"))
        .andExpect(jsonPath("$[2].name").value("Gamma"));
  }

  @Test
  void getRoomById_returnsRoom() throws Exception {
    mockMvc
        .perform(get("/api/rooms/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.name").value("Alpha"))
        .andExpect(jsonPath("$.capacity").value(4));
  }

  @Test
  void getRoomById_returnsNotFoundWhenRoomDoesNotExist() throws Exception {
    mockMvc.perform(get("/api/rooms/99")).andExpect(status().isNotFound());
  }
}
