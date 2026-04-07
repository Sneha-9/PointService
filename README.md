# PointService

## Summary
`PointService` is a Spring Boot-based microservice that manages user points in the ranking system. It provides functionality to aggregate points for users and retrieve lists of users based on their point balances. The service ensures data integrity by validating user IDs against the `UserService` before any point aggregation occurs. It uses **Google Protocol Buffers (Protobuf)** for efficient API communication and **PostgreSQL** for data persistence.

## Project Flow
The project is built with a layered architecture and inter-service dependencies:

1.  **Request Handling Layer (`PointController`):** Manages incoming REST requests. It receives JSON payloads, which are internally mapped to **Protobuf** messages.
2.  **Service Layer (`PointService`):** Contains the core business logic, including:
    *   **External Validation:** Before aggregating points, it makes an HTTP call to the `UserService` (using **OkHttp**) to verify that the provided user ID exists.
    *   **Aggregation Logic:** Increments the user's total points in the database. If a user record doesn't exist in the point table yet, it creates one with an initial balance before updating.
    *   **Retrieval Logic:** Filters user point data from the database based on a minimum point threshold.
3.  **Data Persistence Layer (`PointRepository`):** A Spring Data JPA repository that interacts with **PostgreSQL**. It includes custom JPQL queries for filtering users by points and atomic updates for point aggregation.
4.  **Configuration Layer:** Uses `ConfigProperties` to manage external service URLs (like the `UserService` host and path) which are injectable via environment variables.

## API Endpoints
All APIs use `application/json` as the media type and return **Protobuf**-based structures.

| Endpoint | Method | Description | Input | Output |
| :--- | :--- | :--- | :--- | :--- |
| `/point/aggregator/user` | `POST` | Aggregates (adds) points to a specific user. | `UserPointAggregationRequest` | `UserPointAggregationResponse` |
| `/point/users` | `POST` | Retrieves users who have at least a minimum number of points. | `GetUserPointRequest` | `GetUserPointResponse` |

### API Details

#### 1. Aggregate User Points
*   **Path:** `/point/aggregator/user`
*   **Method:** `POST`
*   **Input (`UserPointAggregationRequest`):**
    ```json
    {
      "id": "string (User UUID)",
      "point": integer
    }
    ```
*   **Output (`UserPointAggregationResponse`):**
    ```json
    {
      "aggregatedPoint": integer (New Total)
    }
    ```
*   **Process:** Validates user with `UserService` -> Checks/Creates point record -> Atomically increments points.

#### 2. Get Users by Minimum Points
*   **Path:** `/point/users`
*   **Method:** `POST`
*   **Input (`GetUserPointRequest`):**
    ```json
    {
      "minPoint": integer
    }
    ```
*   **Output (`GetUserPointResponse`):**
    ```json
    {
      "points": [
        {
          "id": "string",
          "point": { "value": integer }
        }
      ]
    }
    ```

## Technology Stack
*   **Framework:** Spring Boot 4.0.3
*   **Serialization:** Google Protocol Buffers (Protobuf)
*   **Database:** PostgreSQL
*   **Data Access:** Spring Data JPA / Hibernate
*   **HTTP Client:** OkHttp 5
*   **Build Tool:** Gradle (Kotlin DSL)
*   **Testing:** JUnit 5, WireMock (for mocking UserService), Testcontainers (PostgreSQL)

## Configuration
The service relies on the following configurations (configurable via `application.properties` or environment variables):

*   **Database:**
    *   `DB_HOST` (default: `localhost`)
    *   `DB_PORT` (default: `5432`)
    *   `DB_NAME` (default: `pointservice`)
    *   `DB_USERNAME` (default: `sneha`)
    *   `DB_PASSWORD` (default: `password`)
*   **External Services:**
    *   `USER_SERVICE_HOST`: Hostname for the `UserService` (default: `userservice.userservicenamespace`)
    *   `userservice.path`: Path for user validation (default: `/user/validation`)

## Database Schema
The `points` table includes:
*   `id`: Primary key (UUID).
*   `recordid`: The user's ID (correlated with `UserService`).
*   `recordtype`: Type of record (optional metadata).
*   `aggregatedpoints`: Total points for the user.
*   `createdat` / `updatedat`: Audit timestamps.
