const roomSelect = document.getElementById("room-select");
const bookingsList = document.getElementById("bookings-list");
const bookingForm = document.getElementById("booking-form");
const bookingMessage = document.getElementById("booking-message");

document.addEventListener("DOMContentLoaded", loadRooms);

roomSelect.addEventListener("change", async () => {
  if (!roomSelect.value) {
    bookingsList.innerHTML = "<p>Select a room to see bookings.</p>";
    return;
  }

  await loadBookings(roomSelect.value);
});

bookingForm.addEventListener("submit", async (event) => {
  event.preventDefault();

  if (!roomSelect.value) {
    showMessage("Please select a room.");
    return;
  }

  const booking = {
    roomId: Number(roomSelect.value),
    title: document.getElementById("title").value,
    startTime: document.getElementById("start-time").value,
    endTime: document.getElementById("end-time").value,
    email: document.getElementById("email").value,
  };

  try {
    const response = await fetch("/api/bookings", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(booking),
    });

    if (!response.ok) {
      const message = await response.text();
      showMessage(message || "Could not create booking.");
      return;
    }

    showMessage("Booking created.");

    bookingForm.reset();

    await loadBookings(roomSelect.value);
  } catch (error) {
    showMessage("Could not connect to the server.");
  }
});

async function loadRooms() {
  try {
    const response = await fetch("/api/rooms");

    if (!response.ok) {
      throw new Error("Could not load rooms");
    }

    const rooms = await response.json();

    for (const room of rooms) {
      const option = document.createElement("option");

      option.value = room.id;
      option.textContent = `${room.name} (${room.capacity} people)`;

      roomSelect.appendChild(option);
    }
  } catch (error) {
    showMessage("Could not load rooms.");
  }
}

async function loadBookings(roomId) {
  try {
    const response = await fetch(`/api/bookings/room/${roomId}`);

    if (!response.ok) {
      throw new Error("Could not load bookings");
    }

    const bookings = await response.json();

    renderBookings(bookings);
  } catch (error) {
    bookingsList.innerHTML = "<p>Could not load bookings.</p>";
  }
}

function renderBookings(bookings) {
  bookingsList.innerHTML = "";

  if (bookings.length === 0) {
    bookingsList.innerHTML = "<p>No bookings for this room.</p>";
    return;
  }

  const list = document.createElement("ul");

  for (const booking of bookings) {
    const item = document.createElement("li");

    item.textContent = `${booking.title}: ${formatDateTime(booking.startTime)} - ${formatDateTime(booking.endTime)}`;

    list.appendChild(item);
  }

  bookingsList.appendChild(list);
}

function formatDateTime(value) {
  return new Date(value).toLocaleString();
}

function showMessage(message) {
  bookingMessage.textContent = message;
}
