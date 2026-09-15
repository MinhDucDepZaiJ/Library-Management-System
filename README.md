# Library Management System

A backend REST API for managing a library, built with **Java 17 + Spring Boot 3**, **Spring Security (JWT)**, **Spring Data JPA/Hibernate**, and **MySQL**. The full API can be tested with **Postman**.

## 1. Key Features

- **Authentication & Authorization**
  - Register / Login with stateless JWT.
  - 3 roles: `ADMIN`, `LIBRARIAN`, `USER` (reader).
  - Route-based authorization (`SecurityConfig`) and method-level security (`@PreAuthorize`).
- **Book management**: create/update/delete/search books, filter by author or category, pagination.
- **Author & category management**: full CRUD.
- **Borrow / return books**: checks available quantity, automatically updates stock, default loan period of 14 days.
- **User management** (ADMIN): change role, enable/disable account, delete user.
- **Centralized error handling**: unified JSON response format (`ApiResponse`).

## 2. Tech Stack

| Component            | Technology                          |
|-----------------------|--------------------------------------|
| Language               | Java 17                             |
| Framework              | Spring Boot 3.3.4                   |
| Security               | Spring Security + JWT (jjwt 0.12.6) |
| Data access             | Spring Data JPA / Hibernate         |
| Database                | MySQL 8                             |
| Build tool               | Maven                               |
| API testing               | Postman                             |

## 3. Project Structure

```
library-management-system/
├── pom.xml
├── schema.sql                     # Reference database schema
├── sample_data.sql                # Sample data: 5 categories, 7 authors, 10 books
├── postman_collection.json        # Import into Postman for quick testing
└── src/main/
    ├── java/com/library/lms/
    │   ├── LibraryManagementSystemApplication.java
    │   ├── config/                # SecurityConfig, DataInitializer
    │   ├── controller/             # REST Controllers
    │   ├── dto/request|response/  # DTOs
    │   ├── exception/              # Custom exceptions + GlobalExceptionHandler
    │   ├── model/                  # Entities: User, Book, Author, Category, BorrowRecord
    │   ├── repository/             # Spring Data JPA repositories
    │   ├── security/               # JwtUtil, JwtAuthenticationFilter, UserDetailsService
    │   └── service/ + service/impl/
    └── resources/
        └── application.properties
```

## 4. Setup & Run

### 4.1. Prerequisites
- JDK 17+
- Maven 3.8+
- MySQL Server 8+ running

### 4.2. Create the database
Manual creation is not required, since `createDatabaseIfNotExist=true` and `ddl-auto=update` will automatically create the database and tables. If you prefer to create it manually, use the `schema.sql` file.

### Load sample data (10 books)

After running the application for the first time (so Hibernate creates the tables), load the sample data — 5 categories, 7 authors, and 10 books:

```bash
mysql -u root -p library_db < sample_data.sql
```

Then call `GET /api/books` (with an auth token) to see the 10 loaded books.

### 4.3. Configure the connection
Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/library_db?useSSL=false&serverTimezone=UTC&createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=your_mysql_password
```

Recommended: change `jwt.secret` to your own secret value before deploying to production.

### 4.4. Build & run

```bash
# Build
mvn clean install

# Run the application
mvn spring-boot:run
```

The application runs at: `http://localhost:8080`

On first startup, the system automatically creates a default admin account:

```
username: admin
password: admin123
```

**Note:** change the admin password immediately after deploying to a production environment.

## 5. API Authorization

| Endpoint                              | Method | Access                      |
|----------------------------------------|--------|------------------------------|
| `/api/auth/register`, `/api/auth/login`| POST   | Public                       |
| `/api/books/**`                        | GET    | Authenticated (any role)     |
| `/api/books/**`                        | POST/PUT/DELETE | `ADMIN`, `LIBRARIAN` |
| `/api/authors/**`, `/api/categories/**`| GET    | Authenticated                |
| `/api/authors/**`, `/api/categories/**`| POST/PUT/DELETE | `ADMIN`, `LIBRARIAN` |
| `/api/borrow/{bookId}` (borrow a book) | POST   | Authenticated                |
| `/api/borrow/{id}/return` (return a book) | PUT | Owner of the borrow record, or `ADMIN`/`LIBRARIAN` |
| `/api/borrow/my-records`               | GET    | Authenticated                |
| `/api/borrow/all`                      | GET    | `ADMIN`, `LIBRARIAN`         |
| `/api/users/**`                        | ALL    | `ADMIN`                      |

## 6. Calling Authenticated Endpoints

After logging in, add the following header to any request that requires authentication:

```
Authorization: Bearer <token>
```

## 7. Testing with Postman

1. Open Postman → **Import** → select the `postman_collection.json` file.
2. Run the **Auth → Login (Admin)** request first (using `admin` / `admin123`) — the token is automatically saved to the `adminToken` variable.
3. Run **Auth → Register (User)** then **Login (User)** to get a `token` for the reader role.
4. The remaining requests are already configured with the `Authorization: Bearer {{token}}` or `{{adminToken}}` header, depending on the required permission.
5. You can change the `bookId`, `authorId`, `categoryId`, `userId`, `borrowRecordId` variables in the collection's **Variables** tab to match your actual data.

### Example end-to-end test flow
1. Log in as admin → create a `Category` → create an `Author` → create a `Book` (using the newly created `authorId`, `categoryId`).
2. Register and log in as a regular user (`reader1`).
3. The user calls `POST /api/borrow/{bookId}` to borrow a book.
4. The user calls `GET /api/borrow/my-records` to view their borrowing history.
5. The user calls `PUT /api/borrow/{recordId}/return` to return the book.
6. The admin calls `GET /api/borrow/all` to view all borrow records in the system.

## 8. Standard Response Format

```json
{
  "success": true,
  "message": "Books retrieved successfully",
  "data": { "...": "..." },
  "timestamp": "2026-09-15T10:00:00"
}
```

On error:

```json
{
  "success": false,
  "message": "Book not found with id: 99",
  "data": null,
  "timestamp": "2026-09-15T10:00:00"
}
```

## 9. Suggested Future Enhancements

- Add a loan renewal feature.
- Send email reminders when a book is due soon / overdue (`OVERDUE`).
- Add refresh tokens and logout (token blacklist).
- Write unit / integration tests (JUnit 5 + Mockito + Testcontainers for MySQL).
- Add Swagger/OpenAPI (springdoc-openapi) for auto-generated API documentation.

## Author
**Student Name:** Nguyen Duc Minh
**Major:** Computer Science / Information Technology
**University:** Hanoi University of Science and Technology
**Academic Year:** 2022–2023

## License
This project is developed for academic and research purposes only.