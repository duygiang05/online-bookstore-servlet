# Review: Soft Delete Book + Refactor BookDAO

## Tổng quan

Đã triển khai **soft delete** cho sách (ẩn khỏi cửa hàng thay vì xóa cứng), đồng thời **refactor `BookDAO`** để gom SQL lặp và gom binding tham số.

---

## 1. Database

**File:** `sql/add_book_status.sql`

- Thêm cột `status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE'` vào bảng `books`
- Giá trị: `ACTIVE` (đang bán) | `INACTIVE` (ngừng bán / đã xóa mềm)

**Cần làm trước khi chạy app:** chạy script SQL trên database MySQL của bạn.

---

## 2. Model

**File:** `Book.java`

- Thêm field `private String status`

---

## 3. BookDAO — Refactor chính

**File:** `BookDAO.java`

### 3.1. `BASE_SELECT`

- Gom câu `SELECT ... JOIN ...` dùng chung cho mọi query đọc sách
- Thêm cột `b.status` vào SELECT

### 3.2. `executeQuery(whereClause, setter, activeOnly)`

- Method private gom logic JDBC: connection → prepare → execute → map
- `activeOnly = true`: tự thêm `AND b.status = 'ACTIVE'` (trang khách hàng)
- `activeOnly = false`: không lọc status (trang admin)

### 3.3. `bindBookParams(ps, book, startIndex)`

- Gom 10 dòng `ps.setXxx(...)` dùng chung cho `add()` và `update()`

### 3.4. Phân tách method đọc

| Method | Mục đích | Lọc ACTIVE |
|--------|----------|------------|
| `findAll()` | Home, danh sách khách | Có |
| `findAllForAdmin()` | Admin list | Không |
| `findById()` | Admin edit | Không |
| `findActiveById()` | Chi tiết sách, add cart | Có |
| `findByName/Category/Author/Publisher()` | Tìm kiếm khách | Có |

### 3.5. Soft delete — `delete(int id)`

Không dùng `DELETE FROM books`. Thực hiện trong **transaction**:

1. `UPDATE books SET status = 'INACTIVE' WHERE id = ?`
2. `DELETE FROM cart_items WHERE book_id = ?` — dọn giỏ hàng liên quan

Nếu bước 2 lỗi → rollback, status không đổi.

### 3.6. `add()`

- INSERT thêm cột `status = 'ACTIVE'` mặc định cho sách mới

---

## 4. BookService

**File:** `BookService.java`

- Thêm: `findAllForAdmin()`, `findActiveById()`, `delete(int id)`

---

## 5. Controller / Servlet

| File | Thay đổi |
|------|----------|
| `AdminBookServlet` | List dùng `findAllForAdmin()`; POST `action=delete` gọi `bookService.delete()` |
| `BookServlet` | Dùng `findActiveById()`; redirect `/home` nếu sách inactive |
| `AddToCartServlet` | Kiểm tra sách ACTIVE trước khi thêm giỏ; redirect `/home` nếu không hợp lệ |
| `HomeServlet` | Không đổi code — `findAll()` / `findByName()` / `findByCategory()` đã lọc ACTIVE trong DAO |

---

## 6. JSP

**File:** `book-list.jsp`

- Hiển thị trạng thái: **Đang bán** / **Ngừng bán**
- Nút **Delete** (chỉ với sách ACTIVE), có confirm
- POST `action=delete` + `book_id` tới `/admin/book`

---

## 7. Luồng soft delete

```
Admin bấm Delete
    → AdminBookServlet.doPost (action=delete)
    → BookService.delete(id)
    → BookDAO.delete(id)
        → UPDATE status = INACTIVE
        → DELETE cart_items WHERE book_id = id
    → Redirect /admin/book
```

**Kết quả:**
- Sách biến mất khỏi Home / Search / Chi tiết (khách)
- Admin vẫn thấy sách với trạng thái "Ngừng bán"
- Giỏ hàng không còn item của sách đó
- Dữ liệu sách vẫn còn trong DB → sau này làm Order vẫn JOIN được qua `book_id`

---

## 8. Chưa làm (để sau)

- Snapshot trong `order_items` khi làm Order
- Khôi phục sách (`ACTIVE` lại) — có thể thêm sau
- `bindBookParams` / `executeQuery` chưa áp dụng cho `CartItemDAO`

---

## 9. File đã thay đổi

```
sql/add_book_status.sql                                          (mới)
docs/REVIEW_SOFT_DELETE.md                                       (mới)
src/main/java/.../model/Book.java
src/main/java/.../dao/BookDAO.java
src/main/java/.../service/BookService.java
src/main/java/.../controller/admin/AdminBookServlet.java
src/main/java/.../controller/BookServlet.java
src/main/java/.../controller/AddToCartServlet.java
src/main/webapp/book-list.jsp
```

---

## 10. Checklist test

- [ ] Chạy `sql/add_book_status.sql` trên DB
- [ ] Home chỉ hiện sách ACTIVE
- [ ] Admin list hiện cả sách INACTIVE
- [ ] Delete sách → status đổi, biến mất khỏi Home
- [ ] Sách đã delete không mở được `/book?id=...`
- [ ] Sách đã delete không thêm được vào giỏ
- [ ] Cart item của sách bị xóa sau khi admin delete sách
