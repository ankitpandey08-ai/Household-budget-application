# Household Budget API

A standalone Java 17 + Spring Boot REST API for tracking household expenses. It can be called from Postman.

## Requirements
- JDK 17 or newer
- Maven 3.8+ (`mvn -version` should work)

## Run as a standalone server
1. Unzip this project.
2. Open a terminal in the `household-budget-api` directory (the folder containing `pom.xml`).
3. Start the server:
   ```bash
   mvn clean package
   java -jar target/household-budget-api-1.0.0.jar
   ```
   Or use `mvn spring-boot:run`.
4. The server listens on `http://localhost:8080`.
5. Check it is running with `GET http://localhost:8080/api/health`.

Stop the server with `Ctrl+C`.

## Postman setup
For each request, select the HTTP method, enter the URL, and for JSON requests choose **Body → raw → JSON**. No authentication is configured for this local demo.

### 1. Create an expense
`POST http://localhost:8080/api/expenses`
```json
{
  "item": "Groceries",
  "category": "Food",
  "amount": 1850.50,
  "expenseDate": "2026-10-30",
  "notes": "Weekly supermarket shopping"
}
```
Returns `201 Created` and the created expense including its generated `id`.

### 2. List all expenses
`GET http://localhost:8080/api/expenses`

Optional filters:
- `GET http://localhost:8080/api/expenses?category=Food`
- `GET http://localhost:8080/api/expenses?month=2026-10`
- `GET http://localhost:8080/api/expenses?category=Food&month=2026-10`

### 3. Get one expense
`GET http://localhost:8080/api/expenses/1`

### 4. Update an expense
`PUT http://localhost:8080/api/expenses/1`
```json
{
  "item": "Groceries and vegetables",
  "category": "Food",
  "amount": 2100.00,
  "expenseDate": "2026-10-30",
  "notes": "Updated amount"
}
```

### 5. Delete an expense
`DELETE http://localhost:8080/api/expenses/1`
Returns `204 No Content`.

### 6. Monthly budget summary
`GET http://localhost:8080/api/budget/summary?month=2026-10`

Example response:
```json
{
  "period": "2026-10",
  "expenseCount": 2,
  "totalAmount": 3950.50,
  "totalsByCategory": {
    "Food": 2100.00,
    "Utilities": 1850.50
  }
}
```
Use `GET /api/budget/summary` without `month` for all recorded expenses.

## API overview
| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/api/health` | Health check |
| POST | `/api/expenses` | Add an expense |
| GET | `/api/expenses` | List/filter expenses |
| GET | `/api/expenses/{id}` | Get an expense |
| PUT | `/api/expenses/{id}` | Update an expense |
| DELETE | `/api/expenses/{id}` | Delete an expense |
| GET | `/api/budget/summary?month=YYYY-MM` | Totals overall or for a month |

Required fields: `item`, `category`, `amount`, `expenseDate`. `amount` must be greater than zero. Dates use `yyyy-MM-dd`.

## Important limitations
This starter version stores data in memory, so **all expenses are lost when the server stops or restarts**. It is intended for local development/testing with Postman. It has no authentication and should not be exposed to the public internet. A persistent database such as H2 or PostgreSQL can be added next.
