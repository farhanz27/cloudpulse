#!/bin/bash

set -euo pipefail

: "${DB_URL:?DB_URL is not set. Run via make db-setup or export it first.}"
: "${DB_USERNAME:?DB_USERNAME is not set. Run via make db-setup or export it first.}"
: "${DB_PASSWORD:?DB_PASSWORD is not set. Run via make db-setup or export it first.}"

DB_HOST=$(echo "$DB_URL" | sed 's|jdbc:postgresql://||' | cut -d: -f1)
DB_NAME=$(echo "$DB_URL" | sed 's|.*:5432/||; s|?.*||')

SCHEMA_PATH="$(dirname "$0")/../backend/src/main/resources/schema.sql"
DATA_PATH="$(dirname "$0")/../backend/src/main/resources/data.sql"

echo "====================================="
echo "Setting up CloudPulse database..."
echo "Host: $DB_HOST / DB: $DB_NAME"
echo "====================================="

export PGPASSWORD="$DB_PASSWORD"

echo "Resetting public schema..."
psql -U "$DB_USERNAME" -h "$DB_HOST" -d "$DB_NAME" -q \
  -c "DROP SCHEMA public CASCADE; CREATE SCHEMA public;"

echo "Running schema.sql..."
psql -q -U "$DB_USERNAME" -h "$DB_HOST" -d "$DB_NAME" -f "$SCHEMA_PATH"

echo "Running data.sql..."
psql -q -U "$DB_USERNAME" -h "$DB_HOST" -d "$DB_NAME" -f "$DATA_PATH"

unset PGPASSWORD

echo "====================================="
echo "Database setup complete"
echo "====================================="
