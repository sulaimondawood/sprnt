# Sprnt

Sprnt is a modular Java Spring Boot backend for ride-sharing style functionality. It is organized by domain packages (ride, rider, driver, vehicle, pricing, rating, identity, user, etc.) and is designed for extensibility, testability, and event-driven integration via Kafka. The application enables scheduled background tasks and follows standard Spring Boot conventions.

> Project structure and README created from repository inspection. Main entry point: `src/main/java/com/dawood/sprnt/SprntApplication.java`.

---

## Table of Contents

- Overview
- Key features
- Stack
- Repository layout
- How it fits together
- Quick start (build & run)
- Docker & Compose
- Configuration
- Testing
- Development notes
- Contributing
- Recommended improvements
- License
- Maintainers / Contact

---

## Overview

Sprnt is a Spring Boot application implementing a set of domain modules relevant to ride-sharing and mobility platforms. It provides the scaffolding for services that manage rides, riders, drivers, vehicles, pricing, ratings, and user/identity concerns. Kafka is enabled for asynchronous communication and scheduling is enabled for periodic jobs.

This README documents what was observed in the repository and provides actionable steps to build, run, and contribute to the project.

## Key features

- Spring Boot application entrypoint: `com.dawood.sprnt.SprntApplication` with `@EnableKafka` and `@EnableScheduling`.
- Domain-oriented package layout: ride, rider, driver, vehicle, pricing, rating, identity, user, and more.
- Dockerfile and Compose file for containerized runs.
- Maven build (Maven Wrapper included: `mvnw`, `mvnw.cmd`).

## Stack

- Language(s): Java (primary), small HTML resources, Dockerfile
- Framework / runtime: Spring Boot
- Build tool: Maven (wrapper included)
- Notable libraries (inferred): Spring Boot starters, Spring Kafka, Spring Scheduling

## Repository layout

```
.mvn/                     # Maven wrapper internals
mvnw                      # Maven wrapper (Unix)
mvnw.cmd                  # Maven wrapper (Windows)
pom.xml                   # Maven project file
Dockerfile                # Image build
compose.yml               # Docker Compose configuration
src/
  main/
    java/
      com/dawood/sprnt/
        SprntApplication.java   # Spring Boot entry point
        common/                 # shared utilities, DTOs
        driver/                 # driver domain
        health/                 # health checks
        identity/               # auth/identity code
        infrastructure/         # adapters, persistence, kafka producers/consumers
        pricing/                # pricing logic
        rating/                 # rating logic
        ride/                   # ride lifecycle management
        rider/                  # rider domain
        user/                   # user domain
        vehicle/                # vehicle domain
    resources/                  # Spring resources (config, static, templates)
  test/                         # tests
```

## How it fits together

`SprntApplication` boots the Spring context. The application is organized by domain packages which contain controllers, services, repositories, DTOs, and event handlers. The `infrastructure` package likely contains persistence adapters and Kafka producers/consumers. Kafka provides event-driven integration between modules and external systems; scheduled tasks handle periodic work like billing, cleanup, or notifications.

## Quick start (build & run)

Prerequisites
- Java 17+ (or the version specified in `pom.xml`)
- Docker (optional for container runs)
- Git

Clone and build

```bash
git clone https://github.com/sulaimondawood/sprnt.git
cd sprnt
# Build (Unix)
./mvnw clean package -DskipTests
# or (Windows)
mvnw.cmd clean package -DskipTests
```

Run in development

```bash
./mvnw spring-boot:run
```

Run the packaged jar

```bash
java -jar target/*.jar
```

By default Spring Boot serves on port 8080 (unless overridden by configuration).

## Docker & Compose

Build the Docker image locally

```bash
docker build -t sprnt:latest .
```

Run the container

```bash
docker run --rm -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=local \
  -e KAFKA_BOOTSTRAP_SERVERS=your-kafka:9092 \
  sprnt:latest
```

Start with Docker Compose

```bash
docker compose -f compose.yml up --build
```

(Adjust environment variables and service names as required by `compose.yml`.)

## Configuration

The project uses standard Spring Boot configuration (application.properties / application.yml under `src/main/resources`). Typical configuration keys to provide via environment variables or an external config server:

- SPRING_PROFILES_ACTIVE
- SPRING_DATASOURCE_URL
- SPRING_DATASOURCE_USERNAME
- SPRING_DATASOURCE_PASSWORD
- KAFKA_BOOTSTRAP_SERVERS (or equivalent Kafka properties)

Add a `src/main/resources/application.yml` or override via environment variables in production.

## Testing

Run tests with Maven

```bash
./mvnw test
```

Add unit and integration tests under `src/test/java` following the existing package structure.

## Development notes

- Entry point: `src/main/java/com/dawood/sprnt/SprntApplication.java` — enables Kafka and scheduling.
- Follow the package structure for feature additions: controllers, services, repositories, DTOs per domain package.
- Consider using Spring Profiles for environment-specific configuration (e.g., `local`, `dev`, `prod`).

## Contributing

1. Fork the repository
2. Create a topic branch: `git checkout -b feat/my-feature`
3. Make changes and add tests
4. Run the build and tests: `./mvnw clean package` and `./mvnw test`
5. Push and open a pull request

Please keep PRs focused and include tests and documentation for new behavior.

## Recommended improvements

- Add an `application.yml.example` or `.env.example` documenting the expected environment variables (DB, Kafka, etc.).
- Add API documentation (Swagger/OpenAPI) and a brief API reference in the README.
- Document expected Kafka topics and message schemas in a `docs/` folder.
- Add a CI workflow to run tests and build Docker images on PRs.

## License

Add a LICENSE file to make the project's license explicit. If you want a permissive license, consider the MIT license.

## Maintainers / Contact

- Maintainer: sulaimondawood
- Repository: https://github.com/sulaimondawood/sprnt

---

If you'd like, I can also:
- Add an example `application.yml` and `.env.example` to the repository,
- Generate a short API reference by scanning for controllers and endpoints,
- Create a CONTRIBUTING.md or add basic CI workflows.
