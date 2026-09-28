package vibe.roombooking.room;

public class RoomNotFoundException extends RuntimeException {

  public RoomNotFoundException(long id) {
    super("Room not found: " + id);
  }
}
