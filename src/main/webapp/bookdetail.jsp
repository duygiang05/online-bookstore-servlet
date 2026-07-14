<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="vi_VN"/>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Book Detail</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="page">

<jsp:include page="/includes/user-header.jsp"/>

<main>
    <div class="container">
        <h1 class="page-title">Chi tiết sách</h1>

        <div class="detail-layout">
            <div class="detail-cover">
                <img src="images/${book.coverImage}" alt="${book.title}">
            </div>
            <div class="detail-info">
                <h1>${book.title}</h1>

                <div class="detail-row">
                    <span class="detail-label">Tác giả</span>
                    <span>${book.author.name}</span>
                </div>
                <div class="detail-row">
                    <span class="detail-label">Thể loại</span>
                    <span>${book.category.name}</span>
                </div>
                <div class="detail-row">
                    <span class="detail-label">NXB</span>
                    <span>${book.publisher.name}</span>
                </div>
                <div class="detail-row">
                    <span class="detail-label">Năm XB</span>
                    <span>${book.publishedYear}</span>
                </div>
                <div class="detail-row">
                    <span class="detail-label">Giá</span>
                    <span style="font-weight:600; color: var(--primary-dark);">
                        <fmt:formatNumber value="${book.price}" type="number" groupingUsed="true" maxFractionDigits="0"/> đ
                    </span>
                </div>

                <div class="detail-desc">
                    <strong>Mô tả:</strong><br>
                    ${book.description}
                </div>

                <div class="cart-form" data-buy-panel>
                    <div class="qty-picker" data-qty-picker>
                        <button type="button" class="qty-btn" data-qty-delta="-1" aria-label="Giảm số lượng">−</button>
                        <input type="number" id="buyQuantity" value="1" min="1" step="1" required>
                        <button type="button" class="qty-btn" data-qty-delta="1" aria-label="Tăng số lượng">+</button>
                    </div>

                    <div class="cart-form__actions">
                        <form action="${pageContext.request.contextPath}/cart/add" method="post" class="inline-form" data-sync-qty>
                            <input type="hidden" name="book_id" value="${book.id}">
                            <input type="hidden" name="quantity" value="1">
                            <button type="submit" class="btn btn-outline">Thêm vào giỏ</button>
                        </form>
                        <form action="${pageContext.request.contextPath}/checkout" method="post" class="inline-form" data-sync-qty>
                            <input type="hidden" name="action" value="prepareBuyNow">
                            <input type="hidden" name="book_id" value="${book.id}">
                            <input type="hidden" name="quantity" value="1">
                            <button type="submit" class="btn btn-primary">Mua luôn</button>
                        </form>
                    </div>
                </div>

                <p style="margin-top: 1rem;">
                    <a href="${pageContext.request.contextPath}/home" class="btn btn-outline btn-sm">Quay lại</a>
                </p>
            </div>
        </div>
    </div>
</main>

<footer class="site-footer">
    <div class="container">&copy; BookNest</div>
</footer>

<jsp:include page="/includes/site-scripts.jsp"/>
</body>
</html>
