package vibe.roombooking.room;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {

  @Mock private RoomRepository roomRepository;

  private RoomService roomService;

  @BeforeEach
  void setUp() {
    roomService = new RoomService(roomRepository);
  }

  @Test
  void getAllRooms_returnsAllRooms() {
    List<Room> rooms = List.of(new Room(1L, "Alpha", 4), new Room(2L, "Beta", 8));

    when(roomRepository.findAll()).thenReturn(rooms);

    List<Room> result = roomService.getAllRooms();

    assertEquals(rooms, result);
  }

  @Test
  void getRoomById_returnsRoomWhenFound() {
    Room room = new Room(1L, "Alpha", 4);

    when(roomRepository.findById(1L)).thenReturn(Optional.of(room));

    Room result = roomService.getRoomById(1L);

    assertEquals(room, result);
  }

  @Test
  void getRoomById_throwsExceptionWhenRoomDoesNotExist() {
    when(roomRepository.findById(99L)).thenReturn(Optional.empty());

    assertThrows(RoomNotFoundException.class, () -> roomService.getRoomById(99L));
  }
}
