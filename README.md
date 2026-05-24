# CloudPulse

An uptime monitoring SaaS. Monitor HTTP endpoints, track SSL and domain expiry, and get notified on downtime and recovery via email, Telegram, Discord, Slack, Teams, or webhook — with incident management, per-service metrics, and public status pages.



## Live demo

[cloudpulse.farhann.dev](https://cloudpulse.farhann.dev) — login with `demo@cloudpulse.dev` / `demo123`



## Tech Stack

| Layer | Technology |
|---|---|
| Backend | Java 21, Spring Boot 3, Spring Security, Spring Data JPA |
| Frontend | Vue 3, Vite, PrimeVue 4, Pinia, Vue Router, Axios, Chart.js |
| Database | PostgreSQL 16 (Supabase) |
| Email | Resend |
| Deploy | Docker, Docker Compose |



## Features

- **HTTP Health Checks** — configurable intervals from 5 minutes to 24 hours (1-minute minimum on Pro)
- **SSL & Domain Expiry Tracking** — alerts before certificates and domains expire
- **Alerting** — downtime, recovery, and high-latency notifications via email, Telegram, Slack, Microsoft Teams, or webhook
- **Incident Management** — track and resolve incidents tied to monitored services
- **Public Status Pages** — custom slug (`/status/<slug>`), live status, 90-day uptime bars, and an overall operational/degraded/outage banner; auto-refreshes every 60 seconds; no login required
- **Metrics** — latency history and uptime percentages per monitor
- **Subscription & Usage** — free and Pro plan tiers with per-account quota tracking



## Prerequisites

- Java 21
- Node.js 20+
- Docker



## Quick start

Login with `demo@cloudpulse.dev` / `demo123` after setup.

**Environment:**
```bash
# Fill in your values
cp .env.example .env

# Initialize the database — creates tables and inserts demo data
make db-setup
```

**Docker:**
```bash
docker compose up --build
```
Opens at `http://localhost`


**Local development:**
```bash
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
│       ├── org/                # Organisation and membership
│       ├── usage/              # Quota tracking per plan
│       └── shared/             # Config, exceptions, plan constants, utilities
└── frontend/                   # Vue 3 SPA
    └── src/
        ├── api/                # Axios modules (monitors, incidents, integrations, metrics, status pages, usage)
        ├── views/              # Route-level components
        ├── components/         # Shared and layout components
        └── router/
```
