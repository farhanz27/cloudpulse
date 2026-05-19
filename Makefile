-include .env
export

.PHONY: run test build frontend db-setup docker-up docker-down

run:
	cd backend && ./mvnw spring-boot:run

test:
	cd backend && ./mvnw test

build:
	cd backend && ./mvnw package -DskipTests

frontend:
	cd frontend && npm run dev

db-setup:
	bash scripts/setup_db.sh

docker-up:
	docker compose up --build

docker-down:
	docker compose down
