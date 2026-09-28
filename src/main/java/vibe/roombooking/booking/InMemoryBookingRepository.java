package vibe.roombooking.booking;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryBookingRepository implements BookingRepository {

  private final Map<Long, Booking> bookings = new ConcurrentHashMap<>();
  private Long nextId = 1L;

  @Override
  public List<Booking> findAll() {
    return new ArrayList<>(bookings.values());
  }

  @Override
  public List<Booking> findByRoomId(long roomId) {
    return bookings.values().stream().filter(booking -> booking.roomId() == roomId).toList();
  }

  @Override
  public Optional<Booking> findById(long id) {
    return Optional.ofNullable(bookings.get(id));
  }

  @Override
  public Booking save(Booking booking) {
    long id = booking.id() == 0 ? nextId++ : booking.id();

    Booking savedBooking =
        new Booking(
            id,
            booking.roomId(),
            booking.title(),
            booking.startTime(),
            booking.endTime(),
            booking.email());

    bookings.put(id, savedBooking);

    return savedBooking;
  }

  @Override
  public void deleteById(long id) {
    bookings.remove(id);
  }
}
