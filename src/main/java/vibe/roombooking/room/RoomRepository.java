package vibe.roombooking.room;

import java.util.List;
import java.util.Optional;

public interface RoomRepository {

  List<Room> findAll();

  Optional<Room> findById(long id);
}
