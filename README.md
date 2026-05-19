# CloudPulse

Uptime monitoring and incident management for your services.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Backend | Java 21, Spring Boot 3, Spring Security, Spring Data JPA |
| Frontend | Vue 3, Vite, PrimeVue 4, Vue Router, Axios |
| Database | PostgreSQL 16 |
| Email | Resend |
| Deploy | Docker, Docker Compose |

---

## Features

- **HTTP Health Checks** — configurable intervals from 30 seconds to 24 hours
- **SSL & Domain Expiry Tracking** — alerts before certificates and domains expire
- **Alerting** — downtime, recovery, and high-latency notifications via email, Telegram, Discord, Slack, Teams, or webhook
- **Incident Management** — track and resolve incidents tied to monitored services
- **Public Status Pages** — shareable status pages per workspace
- **Metrics** — latency history and uptime percentages per monitor

---

## Environment

All secrets live in `.env`. The Makefile and Docker Compose read it automatically.

| Variable | Description |
|---|---|
| `DB_URL` | JDBC connection URL |
| `DB_USERNAME` | Database username |
| `DB_PASSWORD` | Database password |
| `JWT_SECRET` | JWT signing key (`openssl rand -hex 32`) |
| `RESEND_API_KEY` | Email alerts (get one at resend.com) |
| `ALERT_EMAIL_FROM` | Verified sender address |
| `ALERT_EMAIL_TO` | Default alert recipient |
| `TELEGRAM_BOT_TOKEN` | Telegram bot token (optional) |
| `TELEGRAM_BOT_USERNAME` | Telegram bot username (optional) |

Docker Compose always uses the local `db` container — no `.env` DB vars needed for `docker compose up`.

---

## Quick start

**Docker:**
```bash
docker compose up --build
# http://localhost
```

**Local development:**
```bash
# 1. Start the database container
docker compose up db -d

# 2. Backend (port 8080)
make run

# 3. Frontend (port 5173)
make frontend
```

---

## Notifications

### Telegram

CloudPulse uses a single shared bot — users subscribe from the Integrations page, no BotFather setup required on their end.

**Setup (once, by you):**

1. Message [@BotFather](https://t.me/BotFather) → `/newbot` → copy the token.
2. Set `TELEGRAM_BOT_TOKEN` and `TELEGRAM_BOT_USERNAME` in `.env`.
3. The backend polls Telegram for incoming messages automatically on startup — no webhook or public URL needed.

**User flow:**

1. User opens the Integrations page → clicks **Connect** on Telegram.
2. Dashboard shows a `/start <code>` message to send (or share with a group).
3. User sends it to the bot — link completes automatically within seconds.

> For group notifications, the user invites the bot to the group first, then sends the `/start <code>` message there.

### Discord / Slack / Teams / Webhook

All use incoming webhooks — paste the URL in the Integrations page. No server-side config needed.

---

## Project structure

```
cloudpulse/
├── .env                        # Local secrets (git-ignored)
├── Makefile                    # Dev shortcuts (run, test, build, frontend…)
├── docker-compose.yml
├── backend/                    # Spring Boot API
│   └── src/main/java/com/avantdream/cloudpulse/
│       ├── auth/               # Login, register, JWT
│       ├── monitor/            # Monitor CRUD and health check scheduler
│       ├── alert/              # Alert rules and notification dispatch
│       ├── incident/           # Incident lifecycle
│       ├── integration/        # Discord, Slack, Teams, webhook integrations
│       ├── metrics/            # Latency and uptime metrics
│       ├── statuspage/         # Public status page endpoints
│       ├── telegram/           # Telegram bot polling and linking
│       └── shared/             # Config, exceptions, utilities
└── frontend/                   # Vue 3 SPA
    └── src/
        ├── api/                # Axios modules
        ├── views/
        ├── components/
        └── router/
```
