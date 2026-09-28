package vibe.roombooking.booking;

import java.time.LocalDateTime;

public record Booking(
    long id,
    long roomId,
    String title,
    LocalDateTime startTime,
    LocalDateTime endTime,
    String email) {}
