# Book Blog Microservices

## 1. Giới thiệu Project

**Book Blog Microservices** là một hệ thống mạng xã hội và blog dành cho những người yêu sách, được xây dựng theo kiến trúc Microservices. Dự án cho phép người dùng tạo hồ sơ cá nhân, kết bạn, đăng bài viết về sách, chat với nhau, nhận thông báo theo thời gian thực và quản lý các cuốn sách. Hệ thống được thiết kế mở rộng, dễ bảo trì và phân tách rõ ràng trách nhiệm của từng service.

## 2. Tài liệu Kỹ thuật và Công nghệ Sử dụng

Dự án áp dụng các công nghệ hiện đại cho cả quá trình phát triển và vận hành:

- **Backend**: Java, Spring Boot, Spring Cloud (API Gateway).
- **Frontend / Mobile**: ReactJS (Web App), ứng dụng Mobile.
- **Cơ sở dữ liệu (Databases)**:
  - **MySQL**: Quản lý dữ liệu người dùng và xác thực (Identity).
  - **Neo4j**: Graph DB lưu trữ hồ sơ và các mối quan hệ người dùng (Profile).
  - **MongoDB**: Cơ sở dữ liệu NoSQL lưu trữ thông báo, bài viết và tin nhắn (Notification, Post, Chat).
  - **PostgreSQL**: Lưu trữ dữ liệu liên quan đến AI (Spring AI).
- **Message Broker**: Apache Kafka (quản lý giao tiếp bất đồng bộ giữa các services).
- **Identity & Access Management (IAM)**: Keycloak.
- **Code Quality**: SonarQube.
- **Infrastructure & Deployment**: Docker, Docker Compose.

## 3. Cấu trúc Dự án

Dự án bao gồm các service chính sau đây:

- `api-gateway`: Cổng giao tiếp duy nhất nhận và định tuyến các API request từ client đến các service tương ứng.
- `identity-service`: Quản lý xác thực, phân quyền người dùng (tích hợp Keycloak).
- `profile-service`: Quản lý hồ sơ người dùng cá nhân (sử dụng Neo4j).
- `post-service`: Quản lý các bài viết, blog của người dùng trên hệ thống.
- `chat-service`: Dịch vụ trò chuyện trực tuyến giữa các người dùng.
- `notification-service`: Xử lý và gửi thông báo theo thời gian thực.
- `file-service`: Dịch vụ xử lý việc upload, lưu trữ và quản lý tài liệu, hình ảnh.
- `spring-ai-service`: Tích hợp các tính năng AI thông minh vào hệ thống (ví dụ: gợi ý sách, phân tích...).
- `web-app`: Ứng dụng giao diện người dùng trên nền tảng Web.
- `book-service` (\*): Dịch vụ quản lý thông tin danh mục sách.
- `search-service` (\*): Dịch vụ tìm kiếm toàn văn (Full-text search) cho hệ thống.
- `mobile-app` (\*): Ứng dụng dành cho thiết bị di động.

_(**Lưu ý**: Các mục có đánh dấu `_`như`book-service`, `search-service`, `mobile-app` hiện tại không chứa mã nguồn đầy đủ trên repository này do đã được triển khai và thực thi tại một môi trường / máy tính khác, nhưng vẫn đóng vai trò là một phần cốt lõi của hệ thống).\*

## 4. Hướng dẫn Cài đặt và Sử dụng

### Yêu cầu hệ thống:

- **Java 17+** (Dành cho Backend)
- **Node.js 18+** (Dành cho Frontend Web)
- **Docker & Docker Compose**
- **Maven** (hoặc sử dụng `mvnw` đi kèm)

### Các bước cài đặt:

1. **Clone repository:**

   ```bash
   git clone <repository_url>
   cd book-blog-microservices
   ```

2. **Khởi chạy hạ tầng qua Docker Compose:**
   Sử dụng Docker Compose để khởi chạy Keycloak, Kafka, MySQL, Neo4j, MongoDB, PostgreSQL và SonarQube.

   ```bash
   docker-compose up -d
   ```

3. **Chạy các Microservices Backend:**
   Mở lần lượt các project Backend bằng IDE (IntelliJ, Eclipse,...) hoặc chạy thông qua Terminal bằng Maven.

   ```bash
   cd api-gateway
   ./mvnw spring-boot:run
   ```

   Lặp lại cho các service: `identity-service`, `profile-service`, `post-service`, `chat-service`, `notification-service`, `file-service`, `spring-ai-service`.

4. **Chạy Frontend (Web App):**

   ```bash
   cd web-app
   npm install
   npm start
   ```

5. **Truy cập hệ thống:**
   - Web Frontend: `http://localhost:3000` (hoặc cổng cấu hình của React).
   - Keycloak Admin: `http://localhost:8180`
   - Neo4j Browser: `http://localhost:7474`
   - SonarQube: `http://localhost:8000`
   - Các dịch vụ (book-service, search-service, mobile-app) sẽ được mapping tới máy chủ đang thực thi chúng thông qua thiết lập API Gateway hoặc biến môi trường.
