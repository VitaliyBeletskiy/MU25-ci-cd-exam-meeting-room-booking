package vibe.roombooking.room;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RoomExceptionHandler {

  @ExceptionHandler(RoomNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public String handleRoomNotFound(RoomNotFoundException exception) {
    return exception.getMessage();
  }
}
