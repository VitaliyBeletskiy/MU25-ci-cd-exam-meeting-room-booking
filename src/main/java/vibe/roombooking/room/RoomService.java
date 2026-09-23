package vibe.roombooking.room;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

  private final RoomRepository roomRepository;

  public RoomService(final RoomRepository roomRepository) {
    this.roomRepository = roomRepository;
  }

  public List<Room> getAllRooms() {
    return roomRepository.findAll();
  }

  public Room getRoomById(final long id) {
    return roomRepository.findById(id).orElseThrow(() -> new RoomNotFoundException(id));
  }
}
