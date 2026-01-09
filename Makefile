.PHONY: help build test run docker-up docker-down clean format

help:
	@echo "Available commands:"
	@echo "  make build       - Build the application"
	@echo "  make test        - Run tests"
	@echo "  make run         - Run the application locally"
	@echo "  make docker-up   - Start all services with Docker Compose"
	@echo "  make docker-down - Stop all services"
	@echo "  make clean       - Clean build artifacts"
	@echo "  make format      - Format code with Spotless"

build:
	./mvnw clean package -DskipTests

test:
	./mvnw test

run:
	./mvnw spring-boot:run

docker-up:
	docker-compose up -d

docker-down:
	docker-compose down

clean:
	./mvnw clean

format:
	./mvnw spotless:apply
