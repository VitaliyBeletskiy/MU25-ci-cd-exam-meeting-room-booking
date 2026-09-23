package vibe.roombooking.room;

import java.util.List;
import java.util.Optional;

public class InMemoryRoomRepository implements RoomRepository {
  private final List<Room> rooms =
      List.of(new Room(1L, "Alpha", 4), new Room(2L, "Beta", 8), new Room(3L, "Gamma", 12));

  @Override
  public List<Room> findAll() {
    return rooms;
  }

  @Override
  public Optional<Room> findById(long id) {
    return rooms.stream().filter(room -> room.id() == id).findFirst();
  }
}
