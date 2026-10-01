# User Service — University Management System

REST microservice for user identity and access management in an academic university platform. I developed this service from scratch as my responsibility within a team project built with a microservices architecture.

The service manages administrators, teachers, and students; authenticates users with JSON Web Tokens (JWT); and applies role-based authorization to protected operations. It was integrated with the project's API Gateway and the other domain services during the final delivery.

## Main capabilities

- User registration and authentication with JWT.
- Password hashing with Spring Security.
- Role-based access control for `ADMIN`, `TEACHER`, and `STUDENT` users.
- CRUD operations for administrators, teachers, students, and general users.
- Authenticated profile updates.
- User search and account-status management.
- OpenAPI documentation and interactive testing with Swagger UI.
- MySQL persistence and containerized local deployment.
- Optional, secure bootstrap administrator controlled through environment variables.

## Architecture

```text
Client / API Gateway
        |
        v
User Service (Spring Boot + Spring Security)
        |
        v
      MySQL
```

The repository includes a standalone Docker Compose environment with the API, MySQL, and Adminer. In the complete academic system, the API was accessed through the shared Gateway.

## API overview

| Resource | Base path | Main operations |
|---|---|---|
| Authentication | `/auth` | Login and JWT generation |
| Users | `/users` | CRUD and authenticated profile update |
| User search | `/users/search` | Search users |
| Students | `/students` | CRUD |
| Teachers | `/teachers` | CRUD |
| Administrators | `/administrators` | CRUD |

Swagger UI provides the complete request and response schemas once the service is running.

## Technology stack

- Java 17
- Spring Boot 3.5
- Spring Web, Spring Data JPA, Spring Security, and Bean Validation
- JWT (`jjwt`)
- MySQL 8
- OpenAPI / Swagger UI
- MapStruct and Lombok
- Docker and Docker Compose
- Maven

## Run with Docker

### Prerequisites

- Git
- Docker Engine with Docker Compose

### 1. Clone the repository

```bash
git clone https://github.com/JDRodriguez07/user-service-university-project-java.git
cd user-service-university-project-java
```

### 2. Create the local environment file

```bash
cp .env.example .env
```

Replace every placeholder in `.env`. Generate a suitable JWT secret, for example:

```bash
openssl rand -base64 32
```

The `.env` file is ignored by Git and must never be committed.

### 3. Create the shared network

The network only needs to be created once and allows this service to communicate with the Gateway and other microservices:

```bash
docker network create red_microservicios
```

If it already exists, Docker will report that fact and you can continue.

### 4. Start the services

```bash
docker compose up -d --build
```

| Service | Local URL / port |
|---|---|
| User Service API | `http://localhost:1123` |
| Swagger UI | `http://localhost:1123/swagger-ui/index.html` |
| Adminer | `http://localhost:1122` |
| MySQL | `localhost:1121` |

To stop the environment:

```bash
docker compose down
```

## Authentication

Authenticate through:

```http
POST /auth/login
```

Send the returned token on protected requests:

```http
Authorization: Bearer <token>
```

Creating, updating, or deleting students, teachers, and administrators requires the `ADMIN` role. Authenticated users can access the operations permitted to their role.

## Environment variables

| Variable | Purpose |
|---|---|
| `MYSQL_ROOT_PASSWORD` | MySQL root password used by the container |
| `DB_NAME` | Application database name |
| `DB_USERNAME` | Application database user |
| `DB_PASSWORD` | Application database password |
| `JWT_SECRET` | Base64-encoded JWT signing key (at least 256 bits) |
| `JWT_EXPIRATION_MS` | Token lifetime in milliseconds |
| `BOOTSTRAP_ADMIN_ENABLED` | Enables optional administrator creation |
| `BOOTSTRAP_ADMIN_EMAIL` | Optional bootstrap administrator email |
| `BOOTSTRAP_ADMIN_PASSWORD` | Optional bootstrap administrator password |

Bootstrap administrator creation is disabled by default. If enabled, use a password of at least 12 characters and disable the option again after initialization.

## Build and verification

Compile the service with Maven:

```bash
cd user-service
./mvnw clean package -DskipTests
```

The repository currently includes a Spring application-context smoke test. Functional API behavior can also be inspected through Swagger UI against the Dockerized MySQL environment.

## Project context

This repository represents the User Service I owned in a collaborative university project. Other team members developed the remaining microservices, which were connected through an API Gateway and a shared Docker network for the final integration.
