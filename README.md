# BookNest — Online Bookstore

Ứng dụng web bán sách trực tuyến (Jakarta EE / Servlet + JSP), phục vụ khách hàng mua sách và quản trị viên quản lý hệ thống.

## Tech stack

| Layer | Công nghệ |
|--------|-----------|
| Backend | Java 17, Jakarta Servlet 6, Maven |
| View | JSP, JSTL |
| Database | MySQL |
| Build | Maven WAR |
| UI | CSS + JS thuần |

## Tính năng chính

### Khách hàng (USER)
- Đăng ký / đăng nhập
- Duyệt, tìm kiếm, lọc sách (tên, tác giả, thể loại, NXB, năm XB, khoảng giá)
- Xem chi tiết sách — thêm giỏ / mua ngay
- Giỏ hàng: chọn sách + số lượng khi thanh toán
- Đặt hàng, theo dõi đơn hàng

### Quản trị (ADMIN)
- Dashboard
- Quản lý sách (thêm / sửa / ẩn)
- Quản lý đơn hàng (cập nhật trạng thái)
- Quản lý tác giả, thể loại, nhà xuất bản
- Quản lý tài khoản USER và ADMIN

## Yêu cầu môi trường

- JDK 17+
- Maven 3.8+
- MySQL 8+
- Tomcat 10+ (Jakarta EE) hoặc IDE hỗ trợ deploy WAR (IntelliJ / Eclipse)

## Cấu hình database

1. Tạo schema MySQL:

```sql
CREATE DATABASE online_bookstore CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. Import / tạo các bảng (`users`, `books`, `authors`, `categories`, `publishers`, `carts`, `cart_items`, `orders`, `order_items`, …) theo cấu trúc dự án.

3. Cập nhật kết nối trong:

`src/main/java/com/bootcamp/bookstore/online_bookstore/util/DBConnection.java`

```java
URL      = jdbc:mysql://localhost:3306/online_bookstore
USER     = <your_user>
PASSWORD = <your_password>
```

> Không commit mật khẩu thật lên Git.

## Chạy dự án

### Build

```bash
mvn clean package
```

File WAR: `target/online_bookstore-1.0-SNAPSHOT.war`

### Deploy Tomcat

1. Copy WAR vào thư mục `webapps/` của Tomcat 10+.
2. Start Tomcat.
3. Mở: `http://localhost:8080/online_bookstore-1.0-SNAPSHOT/`

Hoặc chạy trực tiếp từ IDE (Run on Server / Tomcat).

### Tài khoản mẫu

Sau khi seed database, đăng nhập bằng tài khoản có `role = ADMIN` hoặc `USER`.

- Admin vào dashboard: `/admin/dashboard`
- User vào cửa hàng: `/home`

## Cấu trúc thư mục

```
online_bookstore/
├── pom.xml
├── sql/                          # Script SQL hỗ trợ
├── src/main/java/.../
│   ├── controller/               # Servlet storefront
│   ├── controller/admin/         # Servlet admin
│   ├── dao/                      # JDBC
│   ├── model/                    # Entity
│   ├── service/                  # Business logic
│   ├── filter/                   # AuthFilter
│   └── util/                     # DB, role, search helpers
└── src/main/webapp/
    ├── css/, js/, images/
    ├── includes/                 # header, form tìm kiếm
    └── *.jsp                     # Trang UI
```

## URL hữu ích

| Đường dẫn | Mô tả |
|-----------|--------|
| `/home` | Trang chủ / danh sách sách |
| `/login`, `/register` | Đăng nhập / đăng ký |
| `/cart`, `/checkout` | Giỏ hàng / thanh toán |
| `/order` | Đơn hàng của user |
| `/admin/dashboard` | Admin dashboard |
| `/admin/book` | Quản lý sách |
| `/admin/order` | Quản lý đơn hàng |
| `/admin/user` | Quản lý người dùng |

## Ghi chú

- Password hiện so sánh plain text (phù hợp demo / học tập).
- Upload ảnh bìa lưu vào `webapp/images/`.
- Role: `USER` | `ADMIN` (`UserRole`).

## License

Dự án phục vụ mục đích học tập / bootcamp.
