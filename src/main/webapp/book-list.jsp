<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="vi_VN"/>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Book</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="page">

<jsp:include page="/includes/admin-header.jsp"/>

<main>
    <div class="container">
        <span class="admin-badge">Quản lý sách</span>
        <div class="admin-header">
            <div>
                <h1 class="page-title">Danh sách sách</h1>
                <p class="page-subtitle" style="margin-bottom:0;">Thêm, sửa, xóa thông tin sách</p>
            </div>
            <a class="btn btn-primary" href="${pageContext.request.contextPath}/admin/book?action=add">Add Book</a>
        </div>

        <p class="page-subtitle">Lọc theo tên, tác giả, thể loại, NXB, năm XB và khoảng giá</p>
        <jsp:include page="/includes/book-search-form.jsp"/>

        <c:choose>
            <c:when test="${empty books}">
                <section class="form-card empty-state" style="margin-top:1.5rem;">
                    <p>Không tìm thấy sách phù hợp.</p>
                </section>
            </c:when>
            <c:otherwise>
        <div class="book-grid">
            <c:forEach items="${books}" var="book">
                <article class="book-card admin-book-card">
                    <div class="book-card__cover">
                        <img src="${pageContext.request.contextPath}/images/${book.coverImage}" alt="${book.title}">
                    </div>
                    <div class="book-card__body">
                        <h2 class="book-card__title">${book.title}</h2>
                        <p class="book-card__meta">Tác giả: ${book.author.name}</p>
                        <p class="book-card__meta">Thể loại: ${book.category.name}</p>
                        <p class="book-card__price">
                            <fmt:formatNumber value="${book.price}" type="number" groupingUsed="true" maxFractionDigits="0"/> đ
                        </p>
                        <p class="book-card__meta">Năm XB: ${book.publishedYear}</p>
                        <p class="book-card__meta">Mô tả: ${book.description}</p>
                        <p class="book-card__meta">NXB: ${book.publisher.name}</p>
                        <p class="book-card__meta">
                            Trạng thái:
                            <c:choose>
                                <c:when test="${book.status == 'ACTIVE'}">Đang bán</c:when>
                                <c:otherwise>Ngừng bán</c:otherwise>
                            </c:choose>
                        </p>
                        <div class="book-card__actions">
                            <form action="${pageContext.request.contextPath}/admin/book" method="get">
                                <input type="hidden" name="action" value="edit">
                                <input type="hidden" name="book_id" value="${book.id}">
                                <button type="submit" class="btn-outline btn-sm">Edit</button>
                            </form>
                            <c:if test="${book.status == 'ACTIVE'}">
                                <form action="${pageContext.request.contextPath}/admin/book" method="post"
                                      onsubmit="return confirm('Ẩn sách này khỏi cửa hàng?');">
                                    <input type="hidden" name="action" value="delete">
                                    <input type="hidden" name="book_id" value="${book.id}">
                                    <button type="submit" class="btn-danger btn-sm">Delete</button>
                                </form>
                            </c:if>
                        </div>
                    </div>
                </article>
            </c:forEach>
        </div>
            </c:otherwise>
        </c:choose>
    </div>
</main>

<footer class="site-footer">
    <div class="container">&copy; BookNest Admin</div>
</footer>

    <jsp:include page="/includes/site-scripts.jsp"/>
</body>
</html>
