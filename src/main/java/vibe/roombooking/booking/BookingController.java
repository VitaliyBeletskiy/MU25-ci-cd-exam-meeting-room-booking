package vibe.roombooking.booking;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

  private final BookingService bookingService;

  public BookingController(BookingService bookingService) {
    this.bookingService = bookingService;
  }

  @GetMapping
  public List<Booking> getAllBookings() {
    return bookingService.getAllBookings();
  }

  @GetMapping("/room/{roomId}")
  public List<Booking> getBookingsForRoom(@PathVariable long roomId) {
    return bookingService.getBookingsForRoom(roomId);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Booking createBooking(@Valid @RequestBody CreateBookingRequest request) {
    Booking booking =
        new Booking(
            0L,
            request.roomId(),
            request.title(),
            request.startTime(),
            request.endTime(),
            request.email());

    return bookingService.createBooking(booking);
  }
}
