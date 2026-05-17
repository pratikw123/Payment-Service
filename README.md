# Payment Transaction Service

A production-style REST API for payment processing built with **Java 17 + Spring Boot 3**.

Simulates core banking operations: user registration, JWT authentication, account balance management, and atomic payment transactions with lifecycle states (PENDING → SUCCESS/FAILED).

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Java 17 |
| Framework | Spring Boot 3.2 |
| Security | Spring Security + JWT |
| ORM | Spring Data JPA + Hibernate |
| Database | PostgreSQL 15 |
| Testing | JUnit 5 + Mockito |
| DevOps | Docker + Docker Compose |
| Build | Maven |

## Architecture

```
Client → AuthFilter (JWT) → Controller → Service → Repository → PostgreSQL
```

## Getting Started

### Option 1: Docker Compose (Recommended)
```bash
git clone https://github.com/pratikw123/payment-service.git
cd payment-service
docker compose up --build
```
API available at `http://localhost:8080`

### Option 2: Run Locally
```bash
# Start PostgreSQL (or update application.yml with your DB credentials)
mvn clean install
mvn spring-boot:run
```

## API Endpoints

### Auth (Public)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/register` | Register new user |
| POST | `/api/auth/login` | Login, receive JWT |

### Transactions (JWT Required)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/transactions` | Initiate payment |
| GET | `/api/transactions` | My transaction history |
| GET | `/api/transactions/{id}` | Transaction by ID |

### Account (JWT Required)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/accounts/me` | My profile + balance |

## Sample API Usage

### 1. Register
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"pratik","password":"pass123","email":"pratik@test.com","initialBalance":10000}'
```

### 2. Login → Get JWT
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"pratik","password":"pass123"}'
```

### 3. Send Payment
```bash
curl -X POST http://localhost:8080/api/transactions \
  -H "Authorization: Bearer <your_jwt_token>" \
  -H "Content-Type: application/json" \
  -d '{"receiverId":2,"amount":500.00,"description":"Rent payment"}'
```

## Transaction Lifecycle

```
PENDING → SUCCESS  (sufficient balance)
PENDING → FAILED   (insufficient balance)
```

## Running Tests
```bash
mvn test
```

## Key Design Decisions

- **@Transactional** on payment processing ensures atomicity — if credit fails, debit is rolled back
- **BCrypt** password hashing (never store plain text)
- **DTO pattern** — entities never exposed directly via API
- **Global Exception Handler** — consistent error responses across all endpoints
- **Stateless JWT auth** — no server-side sessions, scales horizontally
