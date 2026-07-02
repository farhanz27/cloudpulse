# CloudPulse

[![CI/CD](https://github.com/farhanz27/cloudpulse/actions/workflows/ci-cd.yml/badge.svg)](https://github.com/farhanz27/cloudpulse/actions/workflows/ci-cd.yml)

An uptime monitoring SaaS. Monitor HTTP endpoints, track SSL and domain expiry, and get notified on downtime and recovery via email, Telegram, Discord, Slack, Teams, or webhook — with incident management, per-service metrics, and public status pages.



## Live demo

[cloudpulse.farhann.dev](https://cloudpulse.farhann.dev) — login with `demo@cloudpulse.dev` / `demo123`



## Tech Stack

| Layer | Technology |
|---|---|
| Backend | Java 21, Spring Boot 3, Spring Security, JPA |
| Frontend | Vue 3, TypeScript, PrimeVue 4, Pinia, Chart.js |
| Database | PostgreSQL (Supabase) |
| Email | Resend |
| Deploy | Docker |



## Features

- **HTTP Health Checks** — configurable intervals from 5 minutes to 24 hours (1-minute minimum on Pro)
- **SSL & Domain Expiry Tracking** — alerts before certificates and domains expire
- **Alerting** — downtime, recovery, and high-latency notifications via email (Resend), Telegram, Discord, Slack, Microsoft Teams, or webhook
- **Alert Muting** — snooze notifications per monitor for a set duration without stopping health checks
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
make docker-up
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
│       ├── auth/               # JWT cookie auth — register, login, /me
│       ├── monitor/            # Monitor CRUD, health check scheduler
│       ├── alert/              # Alert state machine (DOWNTIME → RECOVERY → HIGH_LATENCY)
│       ├── incident/           # Incident lifecycle (derived from alerts)
│       ├── integration/        # Integration CRUD, notifiers, Telegram long-polling
│       ├── metrics/            # Latency and uptime time-bucket aggregation
│       ├── statuspage/         # Public status page endpoints
│       ├── org/                # Organisation and membership
│       ├── usage/              # Quota tracking per plan
│       └── shared/             # SecurityConfig, JwtService, JwtAuthFilter, exceptions, plan constants
└── frontend/                   # Vue 3 + TypeScript SPA
    └── src/
        ├── api/                # Axios modules (monitors, incidents, integrations, metrics, statusPages, usage)
        ├── stores/             # Pinia stores (auth, monitors)
        ├── composables/        # usePolling.ts
        ├── views/              # Route-level components
        ├── components/
        │   ├── charts/         # LatencyChart, UptimeChart (vue-chartjs)
        │   ├── common/         # AlertBanner, DataTable, StatusBadge, UptimeBars, StepSlider
        │   ├── dashboard/      # MonitorCard
        │   └── landing/        # LandingNav, LandingFooter, SectionHeader
        ├── theme/              # cloudpulse.ts — PrimeVue custom preset
        ├── types/              # Shared TypeScript interfaces
        └── router/
```
