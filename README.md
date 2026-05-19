# CloudPulse

A multi-tenant uptime monitoring SaaS. Monitor HTTP endpoints, track SSL and domain expiry, and get notified on downtime and recovery via email, Telegram, Slack, Teams, or webhook — with incident management, per-service metrics, and public status pages.



## Tech Stack

| Layer | Technology |
|---|---|
| Backend | Java 21, Spring Boot 3, Spring Security, Spring Data JPA |
| Frontend | Vue 3, Vite, PrimeVue 4, Vue Router, Axios |
| Database | PostgreSQL 16 (Supabase) |
| Email | Resend |
| Deploy | Docker, Docker Compose |



## Features

- **HTTP Health Checks** — configurable intervals from 30 seconds to 24 hours
- **SSL & Domain Expiry Tracking** — alerts before certificates and domains expire
- **Alerting** — downtime, recovery, and high-latency notifications via email, Telegram, Slack, Teams, or webhook
- **Incident Management** — track and resolve incidents tied to monitored services
- **Public Status Pages** — shareable status pages per workspace
- **Metrics** — latency history and uptime percentages per monitor



## Prerequisites

- Java 21
- Node.js 20+
- Docker



## Quick start

**Environment:**
```bash
# Copy and fill in your environment variables
cp .env.example .env
```

**Docker:**
```bash
# Build and start all services
docker compose up --build
```
Opens at `http://localhost` — login with `demo@cloudpulse.dev` / `demo123`


**Local development:**
```bash
# Seed the database (first time only)
make db-setup

# Backend (port 8081)
make run

# Frontend (port 5174)
make frontend
```



## Project structure

```
cloudpulse/
├── .env                        # Local secrets (git-ignored)
├── Makefile                    # Dev shortcuts (run, test, build, frontend…)
├── docker-compose.yml
├── backend/                    # Spring Boot API (port 8081)
│   └── src/main/java/com/avantdream/cloudpulse/
│       ├── auth/               # Login, register, JWT
│       ├── monitor/            # Monitor CRUD and health check scheduler
│       ├── alert/              # Alert rules and notification dispatch
│       ├── incident/           # Incident lifecycle
│       ├── integration/        # Slack, Teams, Telegram, webhook, email integrations
│       ├── metrics/            # Latency and uptime metrics
│       ├── statuspage/         # Public status page endpoints
│       └── shared/             # Config, exceptions, utilities
└── frontend/                   # Vue 3 SPA
    └── src/
        ├── api/                # Axios modules
        ├── views/
        ├── components/
        └── router/
```
