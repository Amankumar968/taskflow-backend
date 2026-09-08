# TaskFlow

A full-stack productivity app that combines task management, a Pomodoro-style focus timer, and analytics — with an AI assistant that can answer questions about your tasks in plain language.

Built with **React + Vite + Tailwind CSS** on the frontend and **Spring Boot + MySQL** on the backend.

---

## Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
    - [Backend Setup](#backend-setup)
    - [Frontend Setup](#frontend-setup)
- [Environment Configuration](#environment-configuration)
- [Core Concepts & Design Notes](#core-concepts--design-notes)
- [Known Limitations](#known-limitations)
- [Roadmap Ideas](#roadmap-ideas)

---

## Features

### Authentication
- Register / Login with username or email + password
- Google Sign-In (OAuth)
- Phone-based authentication
- Forgot Password / Reset Password flow
- JWT-based session handling
- Protected routes — unauthenticated users are redirected to `/login` automatically

### Tasks
- Create, edit, delete, and search tasks
- Long descriptions supported (stored as `TEXT` in the database, not limited to 255 characters)
- Priority levels: **High / Medium / Low**
- Custom categories (e.g. Work, Study, Personal)
- Due dates
- Mark complete / incomplete
- Deleting a task also cleans up any focus sessions linked to it (no orphaned data)

### Focus Timer
- Start a timed focus session (15 / 25 / 30 / 45 / 60 minutes) against a specific task
- Pause / Resume / Cancel a session
- **Accurate across tab switches and backgrounding** — the countdown is anchored to a real timestamp rather than a simple interval counter, so it stays correct even when the browser throttles background tabs
- **Session recovery** — if the page reloads or the browser discards the tab mid-session, the timer picks up exactly where it left off using the session's real start time from the backend
- Live remaining-time countdown shown per session in "Today's Sessions"
- Cancelled sessions record exactly how much time elapsed before cancellation
- Stale/abandoned `RUNNING` sessions (e.g. from a closed browser) are automatically resolved to `COMPLETED` once their scheduled time has passed — nothing is left "running" forever

### Dashboard
- Today's total focus time and completed sessions
- Pending vs. completed task counts
- Rolling 7-day focus time chart
- Task completion progress ring
- Recent sessions list

### Analytics
All charts are powered by real data from the database (via [Recharts](https://recharts.org/)):
- Task completion trend (last 30 days) — area chart
- Focus time trend (last 8 weeks) — bar chart
- Category breakdown — horizontal bar chart
- Priority breakdown — donut chart
- Productivity heatmap (day of week × time of day)
- Current streak & longest streak (consecutive days with a completed task)

### AI Assistant
- Floating chat widget powered by Google's Gemini API
- Understands natural-language questions about your tasks (status, priority, category, due dates)
- Only ever sees the logged-in user's own tasks
- Responds in clear, structured, professional English regardless of the input language

### Tools
A set of everyday utility calculators bundled into the app: Age Calculator, BMI Calculator, Salary Calculator, Date Calculator, Percentage Calculator, Unit Converter, Basic Calculator, EMI Calculator, GST Calculator, CGPA Calculator, Discount Calculator, Profit/Loss Calculator, and Currency Converter.

### Landing Page
- Hero section, feature highlights, "How it works" walkthrough, and footer
- Serves as the public entry point at `/` before login

### Theming
- Light and dark mode
- Custom warm coffee-toned color palette (defined in `tailwind.config.js`)

---

## Tech Stack

**Frontend**
- React 18 + Vite
- Tailwind CSS (custom theme)
- React Router
- Axios
- Recharts (analytics charts)
- Font Awesome (icons)
- `@react-oauth/google` (Google Sign-In)

**Backend**
- Spring Boot
- Spring Security + JWT
- Spring Data JPA / Hibernate
- MySQL
- Google Gemini API (AI assistant)

---

## Project Structure

```
task-manager/                      # Frontend (React + Vite)
├── src/
│   ├── components/                # Reusable UI: TaskForm, TaskList, FocusTimer,
│   │                               #   TimerCircle, TaskSelector, Chatbot,
│   │                               #   Navbar, Sidebar, ProtectedRoute, etc.
│   ├── pages/                     # Route-level pages: Landing, Login, Register,
│   │                               #   Dashboard, AnalyticsPage, tools/*
│   ├── layouts/                   # DashboardLayout (sidebar + navbar shell)
│   ├── hooks/                     # useTimer (timestamp-anchored countdown hook)
│   ├── services/                  # api.js, focusSessionService.js, analyticsService.js
│   ├── context/                   # ToastContext
│   └── App.jsx                    # Route definitions

taskmanager/                        # Backend (Spring Boot)
├── src/main/java/com/example/taskmanager/
│   ├── controller/                # REST controllers (Task, FocusSession, Dashboard,
│   │                               #   Analytics, Auth, AI)
│   ├── service/                   # Business logic (TaskService, FocusSessionService,
│   │                               #   DashboardService, AnalyticsService, GeminiService)
│   ├── model/                     # JPA entities (Task, FocusSession, User, enums)
│   ├── repository/                # Spring Data JPA repositories
│   └── dto/                       # Request/response DTOs
└── src/main/resources/
    └── application.properties     # DB, JWT, and Gemini API configuration
```

---

## Getting Started

### Prerequisites
- Java 17+
- Node.js 18+
- MySQL (running locally or accessible remotely)
- A Google Gemini API key (for the AI assistant feature)

### Backend Setup

1. Create a MySQL database for the project.
2. Configure `src/main/resources/application.properties` with your own values:

   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/your_db_name
   spring.datasource.username=your_db_username
   spring.datasource.password=your_db_password
   spring.jpa.hibernate.ddl-auto=update

   jwt.secret=your_jwt_secret

   gemini.api.key=your_gemini_api_key
   ```

3. From the backend project root, run:

   ```bash
   mvn spring-boot:run
   ```

   The API will start on `http://localhost:8080` by default.

### Frontend Setup

1. From the frontend project root:

   ```bash
   npm install
   npm run dev
   ```

2. The app will be available at `http://localhost:5173`.

> **CORS note:** The backend must explicitly allow whichever frontend origin you're using (`http://localhost:5173` for dev, or your deployed domain in production). Update the CORS configuration in the backend accordingly if you change ports or deploy.

---

## Environment Configuration

| Key | Where | Purpose |
|---|---|---|
| `spring.datasource.*` | `application.properties` | MySQL connection |
| `jwt.secret` | `application.properties` | Signs and verifies login tokens |
| `gemini.api.key` | `application.properties` | Authenticates requests to the Gemini API for the AI assistant |
| CORS allowed origins | Backend CORS config | Must match the frontend's actual URL |

Never commit real secrets (API keys, DB passwords, JWT secret) to version control — use environment variables or a `.gitignore`-d properties file in a real deployment.

---

## Core Concepts & Design Notes

A few implementation details worth knowing if you're extending this project:

- **Timer accuracy:** The focus timer does not rely on `setInterval` decrementing a counter. Instead, it stores the real end timestamp (`Date.now() + remainingSeconds * 1000`) and recalculates remaining time from that on every tick and on `visibilitychange`. This avoids drift caused by browsers throttling timers in background tabs.
- **Session recovery:** On mount, the Focus Timer checks the backend for any session still marked `RUNNING` and resumes the countdown from the real elapsed time (`now - startedAt`), rather than resetting. This covers page reloads, browser tab discarding, and dev-server hot reloads.
- **No `PAUSED` status on the backend:** Pausing is a frontend-only concept. The backend session stays `RUNNING` the whole time, so a session that's recovered after being locally paused will resume based on real elapsed time, not the paused snapshot. Add a backend `PAUSED` status if you want pause state to persist across reloads.
- **Stale session cleanup:** Any `RUNNING` session whose scheduled end time (`startedAt + duration`) has already passed is automatically marked `COMPLETED` the next time sessions are queried (Dashboard, Focus Timer, or Analytics) — so nothing stays "running" forever if a browser is closed mid-session.

---

## Known Limitations

- Focus session pausing is not persisted server-side (see above).
- The AI assistant's knowledge of a task is limited to what's included in its context (title, description, date, status, priority, category) — it can't reason about anything not explicitly passed to it.
- No automated test suite is currently included.

## Roadmap Ideas

- Persist pause state on the backend (`PAUSED` status)
- Recurring tasks
- Subtasks / checklists within a task
- Email notifications / reminders for due dates
- Deployment guide for a production environment (HTTPS, custom domain)

---

## License

Add your preferred license here (MIT, Apache 2.0, etc.) before publishing publicly.