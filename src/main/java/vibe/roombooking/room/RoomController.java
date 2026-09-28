package vibe.roombooking.room;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

  private final RoomService roomService;

  public RoomController(final RoomService roomService) {
    this.roomService = roomService;
  }

  @GetMapping
  public List<Room> getAllRooms() {
    return roomService.getAllRooms();
  }

  @GetMapping("/{id}")
  public Room getRoomById(@PathVariable long id) {
    return roomService.getRoomById(id);
  }
}
