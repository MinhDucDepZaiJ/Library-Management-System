# Library Management System (Hệ thống Quản lý Thư viện)

Backend REST API quản lý thư viện, xây dựng bằng **Java 17 + Spring Boot 3**, **Spring Security (JWT)**, **Spring Data JPA/Hibernate** và **MySQL**. Có thể kiểm thử toàn bộ API bằng **Postman**.

## 1. Tính năng chính

- **Xác thực & Phân quyền (Authentication & Authorization)**
  - Đăng ký / Đăng nhập bằng JWT (stateless).
  - 3 vai trò: `ADMIN`, `LIBRARIAN` (thủ thư), `USER` (độc giả).
  - Phân quyền theo route (`SecurityConfig`) và theo method (`@PreAuthorize`).
- **Quản lý sách**: thêm/sửa/xóa/tìm kiếm sách, theo tác giả, theo thể loại, phân trang.
- **Quản lý tác giả & thể loại**: CRUD đầy đủ.
- **Mượn / trả sách**: kiểm tra số lượng còn lại, tự động cập nhật tồn kho, hạn trả mặc định 14 ngày.
- **Quản lý người dùng** (ADMIN): đổi vai trò, khóa/mở tài khoản, xóa người dùng.
- **Xử lý lỗi tập trung**: định dạng phản hồi JSON thống nhất (`ApiResponse`).

## 2. Công nghệ sử dụng

| Thành phần        | Công nghệ                          |
|--------------------|-------------------------------------|
| Ngôn ngữ            | Java 17                            |
| Framework           | Spring Boot 3.3.4                  |
| Bảo mật             | Spring Security + JWT (jjwt 0.12.6)|
| Truy xuất dữ liệu   | Spring Data JPA / Hibernate        |
| Cơ sở dữ liệu       | MySQL 8                            |
| Build tool          | Maven                              |
| Kiểm thử API        | Postman                            |

## 3. Cấu trúc thư mục

```
library-management-system/
├── pom.xml
├── schema.sql                     # Tham khảo cấu trúc CSDL
├── sample_data.sql                # Dữ liệu mẫu: 5 thể loại, 7 tác giả, 10 cuốn sách
├── postman_collection.json        # Import vào Postman để test nhanh
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

## 4. Cài đặt & chạy dự án

### 4.1. Yêu cầu
- JDK 17+
- Maven 3.8+
- MySQL Server 8+ đang chạy

### 4.2. Tạo cơ sở dữ liệu
Không bắt buộc tạo thủ công vì `createDatabaseIfNotExist=true` và `ddl-auto=update` sẽ tự tạo database + bảng. Nếu muốn tạo thủ công, dùng file `schema.sql`.

### Nạp dữ liệu mẫu (10 cuốn sách)

Sau khi chạy ứng dụng lần đầu (để Hibernate tạo bảng), nạp dữ liệu mẫu gồm 5 thể loại, 7 tác giả và 10 cuốn sách:

```bash
mysql -u root -p library_db < sample_data.sql
```

Sau đó gọi `GET /api/books` (kèm token đăng nhập) để xem danh sách 10 cuốn sách vừa nạp.

### 4.3. Cấu hình kết nối
Sửa file `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/library_db?useSSL=false&serverTimezone=UTC&createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=your_mysql_password
```

Khuyến nghị: đổi `jwt.secret` sang giá trị bí mật riêng trước khi triển khai thật.

### 4.4. Build & chạy

```bash
# Build
mvn clean install

# Chạy ứng dụng
mvn spring-boot:run
```

Ứng dụng chạy tại: `http://localhost:8080`

Khi khởi động lần đầu, hệ thống tự tạo tài khoản quản trị mặc định:

```
username: admin
password: admin123
```

**Lưu ý:** đổi mật khẩu admin ngay sau khi triển khai lên môi trường thật.

## 5. Phân quyền API

| Endpoint                              | Method | Quyền truy cập              |
|----------------------------------------|--------|------------------------------|
| `/api/auth/register`, `/api/auth/login`| POST   | Công khai (public)           |
| `/api/books/**`                        | GET    | Đã đăng nhập (mọi vai trò)   |
| `/api/books/**`                        | POST/PUT/DELETE | `ADMIN`, `LIBRARIAN` |
| `/api/authors/**`, `/api/categories/**`| GET    | Đã đăng nhập                 |
| `/api/authors/**`, `/api/categories/**`| POST/PUT/DELETE | `ADMIN`, `LIBRARIAN` |
| `/api/borrow/{bookId}` (mượn sách)     | POST   | Đã đăng nhập                 |
| `/api/borrow/{id}/return` (trả sách)   | PUT    | Chủ sở hữu phiếu mượn hoặc `ADMIN`/`LIBRARIAN` |
| `/api/borrow/my-records`               | GET    | Đã đăng nhập                 |
| `/api/borrow/all`                      | GET    | `ADMIN`, `LIBRARIAN`         |
| `/api/users/**`                        | ALL    | `ADMIN`                      |

## 6. Cách gọi API có xác thực

Sau khi đăng nhập, thêm header vào các request cần xác thực:

```
Authorization: Bearer <token>
```

## 7. Test với Postman

1. Mở Postman → **Import** → chọn file `postman_collection.json`.
2. Chạy request **Auth → Login (Admin)** trước (dùng `admin` / `admin123`) — token sẽ tự lưu vào biến `adminToken`.
3. Chạy **Auth → Register (User)** rồi **Login (User)** để lấy `token` cho vai trò độc giả.
4. Các request còn lại đã cấu hình sẵn header `Authorization: Bearer {{token}}` hoặc `{{adminToken}}` tùy theo quyền yêu cầu.
5. Có thể đổi biến `bookId`, `authorId`, `categoryId`, `userId`, `borrowRecordId` trong tab **Variables** của collection để khớp với dữ liệu thực tế.

### Ví dụ luồng kiểm thử đầy đủ
1. Login admin → tạo `Category` → tạo `Author` → tạo `Book` (dùng `authorId`, `categoryId` vừa tạo).
2. Đăng ký + đăng nhập user thường (`reader1`).
3. User gọi `POST /api/borrow/{bookId}` để mượn sách.
4. User gọi `GET /api/borrow/my-records` để xem lịch sử mượn.
5. User gọi `PUT /api/borrow/{recordId}/return` để trả sách.
6. Admin gọi `GET /api/borrow/all` để xem toàn bộ phiếu mượn trong hệ thống.

## 8. Định dạng phản hồi chuẩn

```json
{
  "success": true,
  "message": "Lấy danh sách sách thành công",
  "data": { "...": "..." },
  "timestamp": "2026-09-15T10:00:00"
}
```

Khi có lỗi:

```json
{
  "success": false,
  "message": "Không tìm thấy sách với id: 99",
  "data": null,
  "timestamp": "2026-09-15T10:00:00"
}
```

## 9. Ghi chú mở rộng (gợi ý phát triển thêm)

- Thêm chức năng gia hạn mượn sách (renew).
- Gửi email nhắc nhở khi sách sắp/đã quá hạn (`OVERDUE`).
- Thêm refresh token, đăng xuất (blacklist token).
- Viết unit test / integration test (JUnit 5 + Mockito + Testcontainers cho MySQL).
- Thêm Swagger/OpenAPI (springdoc-openapi) để tự sinh tài liệu API.
