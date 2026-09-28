package vibe.roombooking.booking;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BookingExceptionHandler {

  @ExceptionHandler(BookingConflictException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  public String handleBookingConflict(BookingConflictException exception) {
    return exception.getMessage();
  }

  @ExceptionHandler({InvalidBookingTimeException.class, BookingInPastException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public String handleInvalidBooking(RuntimeException exception) {
    return exception.getMessage();
  }
}
