# 🚀 Smart Order Processing System

A production-style **Event-Driven Microservices** project built with **Java 21**, **Spring Boot 3**, **Apache Kafka**, **PostgreSQL**, and **Docker**.

This project demonstrates how modern e-commerce platforms process orders using **Saga Choreography**, **Transactional Outbox Pattern**, **Idempotent Consumers**, and **Event-Driven Architecture** to achieve reliable distributed transactions without Two-Phase Commit (2PC).

---

## 📌 Features

- Event-Driven Microservices
- Saga Choreography Pattern
- Transactional Outbox Pattern
- Apache Kafka (KRaft Mode)
- PostgreSQL
- Docker & Docker Compose
- GitHub Actions CI
- Java 21
- Spring Boot 3
- REST APIs
- MapStruct
- Lombok
- Idempotent Consumers
- State Machine
- Domain Events

---

# 🏗 System Architecture

```text
                        Client
                           │
                           ▼
                  Order Service
                           │
                 OrderCreatedEvent
                           │
                           ▼
                        Kafka
                           │
                           ▼
                Inventory Service
                           │
             InventoryReservedEvent
                           │
                           ▼
                        Kafka
                           │
                           ▼
                 Payment Service
                           │
            PaymentCompletedEvent
                           │
                           ▼
                        Kafka
                           │
                           ▼
                  Order Service
                           │
                           ▼
                  Order Completed
```

---

# 🔄 Compensation Flow

```text
Payment Failed
       │
       ▼
Inventory Release Requested
       │
       ▼
Inventory Released
       │
       ▼
Order Cancelled
```

---

# 🧩 Design Patterns Used

## Event-Driven Architecture

Services communicate asynchronously using Kafka events instead of synchronous REST calls.

---

## Saga Choreography

Distributed transactions are coordinated using domain events without a central orchestrator.

---

## Transactional Outbox

Business data and events are committed atomically within the same database transaction before being published to Kafka.

---

## Idempotent Consumers

Duplicate Kafka messages are safely ignored using processed event tracking.

---

## State Machine

Order lifecycle is managed using explicit order states.

Example:

```text
CREATED

↓

INVENTORY_RESERVED

↓

PAYMENT_PENDING

↓

COMPLETED

OR

FAILED

↓

CANCELLED
```

---

# 📂 Project Structure

```text
smart-order-processing-system

├── common-events
│
├── common-outbox
│
├── order-service
│
├── inventory-service
│
├── payment-service
│
├── docker
│   └── postgres
│
├── docker-compose.yml
│
├── .github
│   └── workflows
│
└── README.md
```

---

# 🛠 Technology Stack

| Category | Technology |
|----------|------------|
| Language | Java 21 |
| Framework | Spring Boot 3 |
| Build Tool | Gradle 8 |
| Messaging | Apache Kafka |
| Database | PostgreSQL |
| Containerization | Docker |
| CI/CD | GitHub Actions |
| Object Mapping | MapStruct |
| Boilerplate Reduction | Lombok |

---

# 📦 Microservices

## Order Service

Responsibilities

- Create Orders
- Publish OrderCreatedEvent
- Receive PaymentCompletedEvent
- Receive PaymentFailedEvent
- Manage Order State Machine

---

## Inventory Service

Responsibilities

- Reserve Inventory
- Release Inventory
- Publish InventoryReservedEvent
- Publish InventoryReleasedEvent

---

## Payment Service

Responsibilities

- Process Payment
- Publish PaymentCompletedEvent
- Publish PaymentFailedEvent

---

# 📡 Kafka Topics

| Topic | Producer | Consumer |
|--------|----------|----------|
| order-created | Order Service | Inventory Service |
| inventory-reserved | Inventory Service | Order Service |
| payment-requested | Order Service | Payment Service |
| payment-completed | Payment Service | Order Service |
| payment-failed | Payment Service | Order Service |
| inventory-release | Order Service | Inventory Service |
| inventory-released | Inventory Service | Order Service |

---

# 🗄 Database Design

Each microservice owns its own database.

```text
PostgreSQL

├── orderdb
│
├── inventorydb
│
└── paymentdb
```

Each service follows the Database per Service pattern.

---

# 🐳 Docker Infrastructure

The project uses Docker Compose to provision:

- PostgreSQL
- Apache Kafka (KRaft)
- Kafka UI
- PgAdmin

Architecture

```text
Docker

├── PostgreSQL

├── Kafka

├── Kafka UI

└── PgAdmin
```

---

# ⚙ GitHub Actions

Continuous Integration pipeline automatically performs:

- Checkout Repository
- Setup Java 21
- Configure Gradle
- Build All Modules
- Run Compilation
- Upload Build Reports

Current Status

✅ Build Passing

---

# 📁 Modules

## common-events

Contains all domain events shared across services.

Examples

- OrderCreatedEvent
- InventoryReservedEvent
- PaymentRequestedEvent
- PaymentCompletedEvent
- PaymentFailedEvent

---

## common-outbox

Implements Transactional Outbox Pattern.

Contains

- OutboxEntity
- OutboxRepository
- ProcessedEventEntity
- ProcessedEventRepository
- Idempotent Consumer Support

---

## order-service

Handles

- Order Creation
- Order Status
- Saga Coordination
- Kafka Producers
- Kafka Consumers

---

## inventory-service

Handles

- Inventory Reservation
- Inventory Release
- Compensation Logic

---

## payment-service

Handles

- Payment Processing
- Payment Success
- Payment Failure

---

# 🚀 Running the Project

Clone repository

```bash
git clone https://github.com/mukesh-kaki/smart-order-processing-system.git
```

Start Infrastructure

```bash
docker compose up -d
```

Start Services

```text
Order Service

Inventory Service

Payment Service
```

---

# 📈 Event Flow

```text
Client

↓

Order Service

↓

OrderCreatedEvent

↓

Kafka

↓

Inventory Service

↓

InventoryReservedEvent

↓

Kafka

↓

Payment Service

↓

PaymentCompletedEvent

↓

Kafka

↓

Order Service

↓

Order Completed
```

---

# 📋 Key Concepts Demonstrated

- Event-Driven Architecture
- Saga Choreography
- Distributed Transactions
- Eventual Consistency
- Transactional Outbox
- Kafka Producers
- Kafka Consumers
- Dead Letter Queue Ready
- Retry Handling
- State Machine
- Idempotent Consumers
- Database per Service
- Dockerized Infrastructure
- GitHub Actions CI

---

# 🔮 Future Enhancements

- Distributed Tracing
- OpenTelemetry
- Prometheus Metrics
- Grafana Dashboards
- Redis Caching
- Kubernetes Deployment
- Helm Charts
- Contract Testing
- Testcontainers
- Chaos Engineering

---

# 👨‍💻 Author

**Mukesh Kaki**

Java Backend Engineer

Tech Stack

- Java
- Spring Boot
- Apache Kafka
- PostgreSQL
- Docker
- Gradle
- GitHub Actions
