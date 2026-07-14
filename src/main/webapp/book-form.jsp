<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="isNewBook" value="${empty book.id}"/>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add Book</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="page">

<jsp:include page="/includes/admin-header.jsp"/>

<main>
    <div class="container">
        <span class="admin-badge">Quản lý sách</span>
        <h1 class="page-title">Thêm / Sửa sách</h1>
        <p class="page-subtitle">Điền đầy đủ thông tin bắt buộc (*)</p>

        <div class="form-card">
            <c:if test="${not empty error}">
                <div class="error-banner" style="margin-bottom:1rem;">${error}</div>
            </c:if>
            <form action="${pageContext.request.contextPath}/admin/book"
                  method="post"
                  enctype="multipart/form-data"
                  class="form-grid admin-form"
                  data-book-form
                  data-book-is-new="${isNewBook}">

                <div class="form-field">
                    <label for="title">Tên sách *</label>
                    <input type="text" id="title" name="title" value="${book.title}"
                           placeholder="Nhập tên sách" required maxlength="255">
                </div>

                <div class="form-field">
                    <label for="isbn">ISBN *</label>
                    <input type="text" id="isbn" name="isbn" value="${book.isbn}"
                           placeholder="ISBN" required maxlength="50">
                </div>

                <div class="form-field">
                    <label for="price">Giá (đ) *</label>
                    <input type="number" id="price" step="0.01" min="0" name="price"
                           value="${book.price}" placeholder="0" required>
                </div>

                <div class="form-field">
                    <label for="stock">Tồn kho *</label>
                    <input type="number" id="stock" min="0" step="1" name="stock"
                           value="${book.stock}" placeholder="0" required>
                </div>

                <div class="form-field">
                    <label for="publishedYear">Năm XB *</label>
                    <input type="number" id="publishedYear" name="publishedYear"
                           value="${book.publishedYear}" min="1000" max="9999"
                           placeholder="VD: 2024" required>
                </div>

                <div class="form-field">
                    <label for="description">Mô tả</label>
                    <textarea id="description" name="description" rows="5"
                              placeholder="Mô tả sách">${book.description}</textarea>
                </div>

                <div class="form-field">
                    <label for="coverImage">
                        Ảnh bìa
                        <c:if test="${isNewBook}"> *</c:if>
                    </label>
                    <input type="file" id="coverImage" name="coverImage" accept="image/*"
                           <c:if test="${isNewBook}">required</c:if>>
                    <c:if test="${not isNewBook and not empty book.coverImage}">
                        <p class="form-hint">Ảnh hiện tại: ${book.coverImage}</p>
                    </c:if>
                </div>

                <div class="form-field">
                    <label for="authorId">Tác giả *</label>
                    <select id="authorId" name="authorId" required>
                        <option value="" disabled ${empty book.author.id ? 'selected' : ''}>-- Chọn tác giả --</option>
                        <c:forEach items="${authors}" var="author">
                            <option value="${author.id}"
                                ${not empty book.author.id && author.id == book.author.id ? 'selected' : ''}>
                                ${author.name}
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <div class="form-field">
                    <label for="categoryId">Thể loại *</label>
                    <select id="categoryId" name="categoryId" required>
                        <option value="" disabled ${empty book.category.id ? 'selected' : ''}>-- Chọn thể loại --</option>
                        <c:forEach items="${categories}" var="category">
                            <option value="${category.id}"
                                ${not empty book.category.id && category.id == book.category.id ? 'selected' : ''}>
                                ${category.name}
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <div class="form-field">
                    <label for="publisherId">Nhà xuất bản *</label>
                    <select id="publisherId" name="publisherId" required>
                        <option value="" disabled ${empty book.publisher.id ? 'selected' : ''}>-- Chọn NXB --</option>
                        <c:forEach items="${publishers}" var="publisher">
                            <option value="${publisher.id}"
                                ${not empty book.publisher.id && publisher.id == book.publisher.id ? 'selected' : ''}>
                                ${publisher.name}
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <c:if test="${not empty book.id}">
                    <input type="hidden" name="book_id" value="${book.id}">
                </c:if>
                <div class="btn-group">
                    <button type="submit" class="btn btn-primary">Lưu</button>
                    <a class="btn btn-outline" href="${pageContext.request.contextPath}/admin/book">Hủy</a>
                </div>
            </form>
        </div>
    </div>
</main>

<footer class="site-footer">
    <div class="container">&copy; BookNest Admin</div>
</footer>

<jsp:include page="/includes/site-scripts.jsp"/>
</body>
</html>
