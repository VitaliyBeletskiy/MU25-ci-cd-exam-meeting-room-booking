package vibe.roombooking.booking;

import org.springframework.stereotype.Service;
import vibe.roombooking.room.RoomNotFoundException;
import vibe.roombooking.room.RoomRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService {

  private final BookingRepository bookingRepo;
  private final RoomRepository roomRepo;

  public BookingService(
      final BookingRepository bookingRepository, final RoomRepository roomRepository) {
    this.bookingRepo = bookingRepository;
    this.roomRepo = roomRepository;
  }

  public List<Booking> getAllBookings() {
    return bookingRepo.findAll();
  }

  public List<Booking> getBookingsForRoom(long roomId) {
    return bookingRepo.findByRoomId(roomId);
  }

  public Booking createBooking(Booking booking) {
    if (roomRepo.findById(booking.roomId()).isEmpty()) {
      throw new RoomNotFoundException(booking.roomId());
    }

    if (!booking.endTime().isAfter(booking.startTime())) {
      throw new InvalidBookingTimeException("End time must be after start time");
    }

    if (booking.startTime().isBefore(LocalDateTime.now())) {
      throw new BookingInPastException();
    }

    boolean overlaps =
        bookingRepo.findByRoomId(booking.roomId()).stream()
            .anyMatch(
                existing ->
                    booking.startTime().isBefore(existing.endTime())
                        && booking.endTime().isAfter(existing.startTime()));

    if (overlaps) {
      throw new BookingConflictException();
    }

    return bookingRepo.save(booking);
  }
}
