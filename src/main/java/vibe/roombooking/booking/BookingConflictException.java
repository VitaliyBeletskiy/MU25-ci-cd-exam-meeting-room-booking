package vibe.roombooking.booking;

public class BookingConflictException extends RuntimeException {

  public BookingConflictException() {
    super("Room is already booked for this time");
  }
}
