package vibe.roombooking.e2e;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("e2e")
class BookingE2ETest {

  @Test
  void userCanCreateBooking() {
    try (Playwright playwright = Playwright.create()) {
      Browser browser =
          playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));

      Page page = browser.newPage();

      page.navigate("http://localhost:8080");

      page.selectOption("#room-select", "1");

      String title = "E2E meeting";
      LocalDateTime start = LocalDateTime.now().plusDays(1).withSecond(0).withNano(0);

      LocalDateTime end = start.plusHours(1);

      page.fill("#title", title);
      page.fill("#start-time", start.toString());
      page.fill("#end-time", end.toString());
      page.fill("#email", "e2e@example.com");

      page.click("button[type='submit']");

      page.waitForSelector("text=Booking created.");

      String bookingsText = page.locator("#bookings-list").innerText();

      assertTrue(bookingsText.contains(title));

      browser.close();
    }
  }
}
