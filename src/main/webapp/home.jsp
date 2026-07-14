<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setLocale value="vi_VN"/>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>BookNest</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="page">

<jsp:include page="/includes/user-header.jsp"/>

<main>
    <div class="container">
        <section class="store-masthead">
            <p class="brand-mark">BookNest</p>
            <h1 class="page-title">Danh sách</h1>
            <p class="page-subtitle">Lọc theo tên, tác giả, thể loại, NXB, năm xuất bản và khoảng giá</p>
        </section>

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
                <article class="book-card">
                    <div class="book-card__cover">
                        <img src="images/${book.coverImage}" alt="${book.title}">
                    </div>
                    <div class="book-card__body">
                        <h2 class="book-card__title">
                            <a href="book?id=${book.id}">${book.title}</a>
                        </h2>
                        <p class="book-card__meta">Tác giả: ${book.author.name}</p>
                        <p class="book-card__meta">Thể loại: ${book.category.name}</p>
                        <p class="book-card__price">
                            <fmt:formatNumber value="${book.price}" type="number" groupingUsed="true" maxFractionDigits="0"/> đ
                        </p>
                    </div>
                </article>
            </c:forEach>
        </div>
            </c:otherwise>
        </c:choose>
    </div>
</main>

<footer class="site-footer">
    <div class="container">&copy; BookNest</div>
</footer>

    <jsp:include page="/includes/site-scripts.jsp"/>
</body>
</html>
