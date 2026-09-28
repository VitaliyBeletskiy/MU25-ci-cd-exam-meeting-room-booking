package vibe.roombooking.booking;

public class BookingInPastException extends RuntimeException {

  public BookingInPastException() {
    super("Booking cannot start in the past");
  }
}
