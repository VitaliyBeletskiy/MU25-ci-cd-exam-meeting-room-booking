# Meeting Room Booking

A simple web application for booking meeting rooms.

The project was created as part of the **MU25 Continuous integration och test** course and demonstrates a complete CI/CD workflow using GitHub Actions and Render.

## Features

The application allows users to:

- View available meeting rooms
- View bookings for a selected room
- Create a new booking
- Prevent bookings in the past
- Prevent invalid time ranges
- Prevent overlapping bookings for the same room
- Identify a booking using an email address

The application currently uses in-memory storage, so bookings are reset when the application restarts.

## Tech Stack

### Backend

- Java 21
- Spring Boot
- Gradle
- REST API
- Jakarta Validation

### Frontend

- HTML
- CSS
- JavaScript

The frontend is served directly by Spring Boot and communicates with the backend through the REST API.

### Testing

The project contains tests on several levels:

- Unit tests
- Controller tests
- Integration tests
- End-to-End tests using Playwright

### CI/CD

- GitHub
- GitHub Actions
- Render

---

## Running the Application Locally

### Requirements

- Java 21
- Git

Gradle does not need to be installed separately because the project uses the Gradle Wrapper.

### Clone the repository

```bash
git clone https://github.com/VitaliyBeletskiy/MU25-ci-cd-exam-meeting-room-booking.git
cd MU25-ci-cd-exam-meeting-room-booking
```

### Run the application

On Windows:

```powershell
.\gradlew.bat bootRun
```

On Linux/macOS:

```bash
./gradlew bootRun
```

The application will be available at:

```text
http://localhost:8080
```

---

## REST API

### Get all rooms

```http
GET /api/rooms
```

### Get a room by ID

```http
GET /api/rooms/{id}
```

### Get all bookings

```http
GET /api/bookings
```

### Get bookings for a room

```http
GET /api/bookings/room/{roomId}
```

### Create a booking

```http
POST /api/bookings
Content-Type: application/json
```

Example:

```json
{
  "roomId": 1,
  "title": "Team meeting",
  "startTime": "2026-10-05T10:00:00",
  "endTime": "2026-10-05T11:00:00",
  "email": "user@example.com"
}
```

---

## Testing

### Unit and Integration Tests

Run:

```powershell
.\gradlew.bat test
```

or:

```bash
./gradlew test
```

These tests cover backend business logic, controllers and API integration.

### End-to-End Tests

Playwright is used to test the complete application flow through a real browser.

Install the Playwright Chromium browser:

```powershell
.\gradlew.bat playwrightInstall
```

Start the application:

```powershell
.\gradlew.bat bootRun
```

Then, in another terminal:

```powershell
.\gradlew.bat e2eTest
```

The E2E test performs a complete user flow:

1. Opens the application
2. Selects a meeting room
3. Fills in the booking form
4. Creates a booking
5. Verifies that the booking appears in the UI

---

## CI/CD Workflow

The project uses a Git-based workflow with three types of branches:

```text
feature/*
    ↓
Pull Request
    ↓
dev
    ↓
Development deployment

dev
    ↓
Pull Request
    ↓
main
    ↓
Production deployment
```

### Feature Development

New functionality is developed in feature branches, for example:

```text
feature/add-booking
feature/frontend
feature/e2e-ci
```

Changes are merged through Pull Requests.

### Continuous Integration

GitHub Actions runs automatically for pushes and Pull Requests targeting `dev` or `main`.

The CI pipeline performs:

```text
Checkout repository
        ↓
Set up Java 21
        ↓
Gradle build
        ↓
Unit tests
        ↓
Integration tests
        ↓
Start application
        ↓
Install Playwright Chromium
        ↓
Run E2E tests
```

A deployment is only allowed when all tests have passed successfully.

### Development Deployment

A push to:

```text
dev
```

runs the full test pipeline and then automatically deploys the application to the **development environment on Render**.

### Production Deployment

A push to:

```text
main
```

runs the same test pipeline and then automatically deploys the application to the **production environment on Render**.

Render deployment is triggered from GitHub Actions using separate deployment hooks for development and production.

This keeps CI/CD under the control of the GitHub Actions workflow rather than deploying independently from every Git push.

---

## Environments

### Development

[Development application on Render](https://meeting-room-booking-dev.onrender.com/)

### Production

[Production application on Render](https://meeting-room-booking-prod.onrender.com/)

---

## Project Structure

```text
src/
├── main/
│   ├── java/
│   │   └── vibe/roombooking/
│   │       ├── booking/
│   │       └── room/
│   └── resources/
│       └── static/
│           ├── index.html
│           ├── app.js
│           └── styles.css
│
└── test/
    └── java/
        └── vibe/roombooking/
            ├── booking/
            ├── room/
            └── e2e/
```

## Booking Rules

A booking is accepted only if:

- The selected room exists
- The start time is not in the past
- The end time is after the start time
- The booking does not overlap another booking for the same room
- The supplied email address is valid

Two bookings may be directly adjacent. For example:

```text
10:00–11:00
11:00–12:00
```

is allowed.

## Authentication

Authentication is not implemented in the current version.

The booking currently contains an email address supplied by the client. The application is designed so that authentication can be introduced later, for example using a bearer token:

```http
Authorization: Bearer <token>
```

In that case, user identity would be obtained from the authenticated user instead of trusting an email address supplied in the booking request.