package vibe.roombooking.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vibe.roombooking.room.Room;
import vibe.roombooking.room.RoomNotFoundException;
import vibe.roombooking.room.RoomRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

  @Mock private BookingRepository bookingRepository;

  @Mock private RoomRepository roomRepository;

  private BookingService bookingService;

  @BeforeEach
  void setUp() {
    bookingService = new BookingService(bookingRepository, roomRepository);
  }

  @Test
  void createBooking_savesBookingWhenValid() {
    LocalDateTime start = LocalDateTime.now().plusHours(1);
    LocalDateTime end = start.plusHours(1);

    Booking booking = new Booking(0L, 1L, "Team meeting", start, end, "user@example.com");

    Booking savedBooking = new Booking(1L, 1L, "Team meeting", start, end, "user@example.com");

    when(roomRepository.findById(1L)).thenReturn(Optional.of(new Room(1L, "Alpha", 4)));

    when(bookingRepository.findByRoomId(1L)).thenReturn(List.of());

    when(bookingRepository.save(booking)).thenReturn(savedBooking);

    Booking result = bookingService.createBooking(booking);

    assertEquals(savedBooking, result);
    verify(bookingRepository).save(booking);
  }

  @Test
  void createBooking_throwsWhenRoomDoesNotExist() {
    LocalDateTime start = LocalDateTime.now().plusHours(1);

    Booking booking =
        new Booking(0L, 99L, "Team meeting", start, start.plusHours(1), "user@example.com");

    when(roomRepository.findById(99L)).thenReturn(Optional.empty());

    assertThrows(RoomNotFoundException.class, () -> bookingService.createBooking(booking));

    verify(bookingRepository, never()).save(any());
  }

  @Test
  void createBooking_throwsWhenEndTimeIsNotAfterStartTime() {
    LocalDateTime start = LocalDateTime.now().plusHours(1);

    Booking booking = new Booking(0L, 1L, "Team meeting", start, start, "user@example.com");

    when(roomRepository.findById(1L)).thenReturn(Optional.of(new Room(1L, "Alpha", 4)));

    assertThrows(InvalidBookingTimeException.class, () -> bookingService.createBooking(booking));

    verify(bookingRepository, never()).save(any());
  }

  @Test
  void createBooking_throwsWhenBookingStartsInPast() {
    LocalDateTime start = LocalDateTime.now().minusHours(1);

    Booking booking =
        new Booking(0L, 1L, "Team meeting", start, start.plusMinutes(30), "user@example.com");

    when(roomRepository.findById(1L)).thenReturn(Optional.of(new Room(1L, "Alpha", 4)));

    assertThrows(BookingInPastException.class, () -> bookingService.createBooking(booking));

    verify(bookingRepository, never()).save(any());
  }

  @Test
  void createBooking_throwsWhenBookingOverlapsExistingBooking() {
    LocalDateTime existingStart = LocalDateTime.now().plusHours(2);
    LocalDateTime existingEnd = existingStart.plusHours(1);

    Booking existingBooking =
        new Booking(1L, 1L, "Existing meeting", existingStart, existingEnd, "existing@example.com");

    Booking newBooking =
        new Booking(
            0L,
            1L,
            "New meeting",
            existingStart.plusMinutes(30),
            existingEnd.plusMinutes(30),
            "user@example.com");

    when(roomRepository.findById(1L)).thenReturn(Optional.of(new Room(1L, "Alpha", 4)));

    when(bookingRepository.findByRoomId(1L)).thenReturn(List.of(existingBooking));

    assertThrows(BookingConflictException.class, () -> bookingService.createBooking(newBooking));

    verify(bookingRepository, never()).save(any());
  }

  @Test
  void createBooking_allowsBookingImmediatelyAfterExistingBooking() {
    LocalDateTime existingStart = LocalDateTime.now().plusHours(2);
    LocalDateTime existingEnd = existingStart.plusHours(1);

    Booking existingBooking =
        new Booking(1L, 1L, "Existing meeting", existingStart, existingEnd, "existing@example.com");

    Booking newBooking =
        new Booking(
            0L, 1L, "Next meeting", existingEnd, existingEnd.plusHours(1), "user@example.com");

    when(roomRepository.findById(1L)).thenReturn(Optional.of(new Room(1L, "Alpha", 4)));

    when(bookingRepository.findByRoomId(1L)).thenReturn(List.of(existingBooking));

    when(bookingRepository.save(newBooking))
        .thenReturn(
            new Booking(
                2L,
                newBooking.roomId(),
                newBooking.title(),
                newBooking.startTime(),
                newBooking.endTime(),
                newBooking.email()));

    Booking result = bookingService.createBooking(newBooking);

    assertEquals(2L, result.id());
    verify(bookingRepository).save(newBooking);
  }
}
