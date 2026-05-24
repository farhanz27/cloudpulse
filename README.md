# CloudPulse

A multi-tenant uptime monitoring SaaS. Monitor HTTP endpoints, track SSL and domain expiry, and get notified on downtime and recovery via email, Telegram, Slack, Teams, or webhook — with incident management, per-service metrics, and public status pages.



## Tech Stack

| Layer | Technology |
|---|---|
| Backend | Java 21, Spring Boot 3, Spring Security, Spring Data JPA |
| App frontend | Vue 3, Vite, PrimeVue 4, Pinia, Vue Router, Axios, Chart.js |
| Marketing pages | Vue 3, @vueuse/motion (no PrimeVue) |
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

**Environment:**
```bash
cp .env.example .env
# Fill in your values
```

**Docker:**
```bash
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
