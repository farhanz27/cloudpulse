-include .env
export

.PHONY: run test build frontend docker-up docker-down

run:
	cd backend && ./mvnw spring-boot:run

test:
	cd backend && ./mvnw test

build:
	cd backend && ./mvnw package -DskipTests

frontend:
	cd frontend && npm run dev

docker-up:
	docker compose up --build

docker-down:
	docker compose down
