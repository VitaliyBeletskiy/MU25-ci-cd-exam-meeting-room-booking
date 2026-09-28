package vibe.roombooking.booking;

import java.util.List;
import java.util.Optional;

public interface BookingRepository {

  List<Booking> findAll();

  List<Booking> findByRoomId(long roomId);

  Optional<Booking> findById(long id);

  Booking save(Booking booking);

  void deleteById(long id);
}
