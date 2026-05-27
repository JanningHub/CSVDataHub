# 📊 CSV Data Hub

The **CSV Data Hub** is a robust platform designed for the ingestion, storage, and analysis of large volumes of data from CSV files. The system processes these files asynchronously and in a distributed manner, inserting the information directly into structured relational database tables, making the data ready for complex operational or analytical queries.

With native support for large files (configured for up to 10GB in the current environment), the project utilizes batch processing and messaging to ensure scalability, resilience, and low memory consumption in the main application.

This project is currently at version 1.0.0, representing the first stable and functional release of the system.

#### 🧭 Upcoming Improvements (Roadmap):

Future versions will focus on significant architectural and functional enhancements, including:

- Implementation of a dedicated user interface (UI) for data management and analytics
- Performance and code structure improvements to increase overall system agility
- Expansion of the analytics engine to support more complex queries, aggregations, and multi-dimensional analysis
- Enhancements in scalability and distributed processing capabilities
- Improved developer experience with better tooling and observability

---

## 🛠️ Technologies Used

- Java 21  
- Spring Boot 3.3.5  
- Spring Batch  
- Spring Data JPA (Hibernate)  
- Spring AMQP (RabbitMQ)  
- PostgreSQL 16  
- Docker (v20.10.0+)  
- Docker Compose (v2.0.0+)  
- Spring Boot Actuator  

---

## 🚀 How to Run the System

The project is fully containerized using Docker Compose. You do not need to install Java, PostgreSQL, or RabbitMQ locally.

### 📋 Prerequisites

- Docker (v20.10.0 or higher)
- Docker Compose (v2.0.0 or higher)

---

### ⚡ Step-by-Step Execution

#### 1. Clone the repository
```bash
git clone https://github.com/JanningHub/CSVDataHub.git
cd CSVDataHub
````

#### 2. Storage volume (optional)

The `./uploads` folder is automatically created by Docker on first run to store incoming files.

#### 3. Build and start the system

```bash
docker compose up --build -d
```

* `--build` compiles the Java application before creating the container
* `-d` runs everything in detached mode

#### 4. Verify running containers

```bash
docker compose ps
```

Expected services:

* `csv-app` → [http://localhost:8080](http://localhost:8080)
* `postgres` → port 5432
* `rabbitmq` → ports 5672 / 15672

---

## 🔌 API Reference

The system exposes asynchronous APIs for table management, ingestion, tracking, and analytics.

---

## 1. 📁 Table Management

### ➕ Create Table

**POST** `http://localhost:8080/table-management`

**Request Body**

```json
{
  "tableName": "users",
  "columns": [
    { "name": "id", "type": "BIGSERIAL" },
    { "name": "name", "type": "VARCHAR" },
    { "name": "email", "type": "VARCHAR" },
    { "name": "created_at", "type": "TIMESTAMP" }
  ]
}
```

**Response**

```text
Create request for table 'users' has been queued for processing.
Request ID: a2b49607-a054-4a99-b38c-ed6e1ff64e1c
```

---

### 📄 Get Table Schema

**GET** `http://localhost:8080/table-management/users`

**Response**

```json
{
  "tableName": "users",
  "columns": [
    { "name": "id", "type": "bigint" },
    { "name": "name", "type": "character varying" },
    { "name": "email", "type": "character varying" },
    { "name": "created_at", "type": "timestamp without time zone" }
  ]
}
```

---

### 🗑️ Delete Table

**DELETE** `http://localhost:8080/table-management/users`

**Response**

```text
Delete request for table 'users' has been queued for processing.
Request ID: d4613713-5f35-44ea-ad93-906cc3dc1234
```

---

## 2. 📌 Request Tracker

### 📋 List All Requests

**GET** `http://localhost:8080/request-tracker`

**Response**

```json
{
  "content": [
    {
      "requestId": "a2b49607-a054-4a99-b38c-ed6e1ff64e1c",
      "queueName": "csv.table.create",
      "status": "DONE",
      "updatedAt": "2026-05-26T16:46:24.705368Z"
    },
    {
      "requestId": "c67ef986-9011-4293-8f7d-9d3126c51f54",
      "queueName": "csv.ingestion",
      "status": "RUNNING",
      "updatedAt": "2026-05-26T17:03:03.538120Z"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 20
  },
  "totalPages": 1,
  "totalElements": 3
}
```

---

### 🔎 Get Request by ID

**GET** `http://localhost:8080/request-tracker/{requestId}`

**Example**

```json
{
  "requestId": "c67ef986-9011-4293-8f7d-9d3126c51f54",
  "queueName": "csv.ingestion",
  "status": "RUNNING",
  "updatedAt": "2026-05-26T17:03:03.538120Z"
}
```

---

## 3. 📤 Data Ingestion

### Upload CSV File

**POST** `http://localhost:8080/data-ingestion/upload`

**Form Data**

* file: `users.csv`
* tableName: `users`

**Response**

```text
File received and queued for processing.
Request ID: c67ef986-9011-4293-8f7d-9d3126c51f54
```

---

## 4. 📊 Data Query

### Basic Query

**GET** `http://localhost:8080/data-query/users`

**Response**

```json
{
  "data": {
    "content": [
      {
        "id": 1,
        "name": "Maria Gonçalves",
        "email": "maria.gonçalves1@outlook.com",
        "created_at": "2023-05-17T13:42:25.000+00:00"
      }
    ],
    "pageable": {
      "pageNumber": 0,
      "pageSize": 20
    },
    "totalPages": 9000000,
    "totalElements": 180000000
  }
}
```

---

## 5. 📈 Analytics Query

### Advanced Analytics

**POST** `http://localhost:8080/data-query/analytics`

### Supported Operators

* EQ, NEQ, GT, GTE, LT, LTE, LIKE

### Supported Metrics

* COUNT, AVG, SUM, MIN, MAX

---

**Request Body**

```json
{
  "tableName": "users",
  "filters": [
    {
      "field": "email",
      "operator": "LIKE",
      "value": "%@gmail.com"
    }
  ],
  "metrics": [
    {
      "type": "COUNT",
      "field": "*",
      "alias": "gmail_users"
    }
  ]
}
```

---

**Response**

```json
{
  "data": [
    {
      "gmail_users": 35997282
    }
  ]
}
```
---

## 🌐 Important URLs

* API: [http://localhost:8080](http://localhost:8080)
* RabbitMQ: [http://localhost:15672](http://localhost:15672) (guest / guest)
* PostgreSQL: localhost:5432
* Actuator Health: [http://localhost:8080/actuator/health](http://localhost:8080/actuator/health)

---

## 💾 Large File Support

Configured for up to **10GB uploads**:

```bash
SPRING_SERVLET_MULTIPART_MAX_FILE_SIZE=10GB
SPRING_SERVLET_MULTIPART_MAX_REQUEST_SIZE=10GB
```

Files are streamed to `./uploads` to avoid memory overload.

---

## 🛑 Stop System

```bash
docker compose down
```

Remove all data:

```bash
docker compose down -v
```
