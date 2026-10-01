# University Project – User Service (Spring Boot + Docker + MySQL)

This repository contains the **User Service** module for a university microservices project.  
It includes:

- Spring Boot 3  
- Java 17  
- MySQL 8  
- Docker & Docker Compose  
- JWT authentication  
- Adminer (database viewer)  
- Environment variable configuration  
- Clean Git structure with `.env`, `.env.example`, `.gitignore`

---

## 🚀 How to Run the Project (Using Docker)

### 1️⃣ **Clone the repository**
```bash
git clone <YOUR_REPO_URL>
cd university-project
```

---

### 2️⃣ **Create your local `.env` file**

Copy the example file:

```bash
cp .env.example .env
```

Then open `.env` and replace the placeholder values:

- `MYSQL_ROOT_PASSWORD`
- `DB_PASSWORD`
- `JWT_SECRET`

This file **must NOT be committed** (it's ignored by `.gitignore`).

---

### 3️⃣ **Start all services with Docker**

```bash
docker compose up -d --build
```

This will start:

| Service          | Port | Description |
|------------------|------|-------------|
| MySQL            | 1121 | Database    |
| Adminer          | 1122 | DB UI       |
| User Service API | 1123 | Spring Boot |

---

### 4️⃣ **Verify that the containers are running**

```bash
docker ps
```

You should see:

```
mysql_user_service      Up (healthy)
adminer_user_service    Up
backend_user_service    Up
```

If MySQL becomes `(unhealthy)`, delete local DB volume:

```bash
docker compose down -v
docker compose up -d
```

---

## 📘 Access Points

### ✔ Swagger API (User Service)
```
http://localhost:1123/swagger-ui/index.html
```

### ✔ Adminer (Database Viewer)
```
http://localhost:1122
```

**Adminer login:**

- System: MySQL  
- Server: `user_db`  
- Username: value of `${DB_USERNAME}`  
- Password: value of `${DB_PASSWORD}`  
- Database: `${DB_NAME}`  

---

## 🔐 Authentication (JWT)

Users must authenticate through:

```
POST /auth/login
```

This returns a JWT token.

The token is required for:

- Creating users (ADMIN only)
- Updating profile data (Admins, Students, Teachers)
- Accessing restricted endpoints

Environment variables controlling JWT:

```env
JWT_SECRET=your-secret-here
JWT_EXPIRATION_MS=86400000
```

---

## 🛠 Docker Compose Overview

Services:

- **user_db**: MySQL with volume mounted at `config/mysql/`
- **adminer**: Web UI to manage MySQL
- **user_service**: Spring Boot API built using multi-stage Dockerfile

A correct healthcheck ensures MySQL is fully ready before Spring Boot starts.

---

## 🧹 Git Structure & Ignored Files

The project uses a clean `.gitignore` that excludes:

- `.env`
- `config/mysql/` (MySQL raw data)
- `user-service/target/`
- IDE files (`.idea/`, `.vscode/`)
- Logs (`*.log`)

This prevents sensitive files and large DB binaries from polluting the repository.

---

## 📄 Environment Variables

Example values:

```env
MYSQL_ROOT_PASSWORD=CHANGE_ME
DB_NAME=user-microservice-db
DB_USERNAME=user_service_user
DB_PASSWORD=CHANGE_ME
JWT_SECRET=CHANGE_ME
JWT_EXPIRATION_MS=86400000
```

The **actual `.env` file is never committed**.

---

## 📦 Technologies Used

- Java 17  
- Spring Boot 3.2  
- Spring Data JPA  
- MySQL 8  
- Docker & Docker Compose  
- Adminer  
- Lombok  
- MapStruct  
- Spring Security + JWT  

---

## ✨ Summary

This service provides user management for the university microservices system, including:

- Authentication  
- Authorization  
- CRUD for Admin, Student, and Teacher  
- Role-based permissions  
- Profile updates  
- Persistence via MySQL  
- Deployment with Docker  

This project is ready for academic submission or extension into a full multi-service architecture.

