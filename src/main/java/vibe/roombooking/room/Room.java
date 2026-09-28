package vibe.roombooking.room;

public record Room(long id, String name, int capacity) {
  public Room {
    if (id <= 0) {
      throw new IllegalArgumentException("Room id must be positive");
    }
    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException("Room name must not be blank");
    }
    if (capacity <= 0) {
      throw new IllegalArgumentException("Room capacity must be positive");
    }
  }
}
