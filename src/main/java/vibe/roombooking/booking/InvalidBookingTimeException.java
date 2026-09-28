package vibe.roombooking.booking;

public class InvalidBookingTimeException extends RuntimeException {

  public InvalidBookingTimeException(String message) {
    super(message);
  }
}
