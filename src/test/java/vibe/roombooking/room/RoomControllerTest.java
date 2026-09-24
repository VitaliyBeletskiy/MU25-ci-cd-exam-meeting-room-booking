package vibe.roombooking.room;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RoomController.class)
public class RoomControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private RoomService roomService;

  @Test
  void getAllRooms_returnsRooms() throws Exception {
    when(roomService.getAllRooms())
        .thenReturn(List.of(new Room(1L, "Alpha", 4), new Room(2L, "Beta", 8)));

    mockMvc
        .perform(get("/api/rooms"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(1))
        .andExpect(jsonPath("$[0].name").value("Alpha"))
        .andExpect(jsonPath("$[0].capacity").value(4))
        .andExpect(jsonPath("$[1].id").value(2))
        .andExpect(jsonPath("$[1].name").value("Beta"))
        .andExpect(jsonPath("$[1].capacity").value(8));
  }

  @Test
  void getRoomById_returnsRoom() throws Exception {
    when(roomService.getRoomById(1L)).thenReturn(new Room(1L, "Alpha", 4));

    mockMvc
        .perform(get("/api/rooms/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.name").value("Alpha"))
        .andExpect(jsonPath("$.capacity").value(4));
  }

  @Test
  void getRoomById_returnsNotFoundWhenRoomDoesNotExist() throws Exception {
    when(roomService.getRoomById(99L)).thenThrow(new RoomNotFoundException(99L));

    mockMvc.perform(get("/api/rooms/99")).andExpect(status().isNotFound());
  }
}
