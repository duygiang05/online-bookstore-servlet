<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="vi_VN"/>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Thanh toán</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="page">

<jsp:include page="/includes/user-header.jsp"/>

<main>
    <div class="container">
        <h1 class="page-title">Thanh toán</h1>
        <p class="page-subtitle">Xác nhận sản phẩm đã chọn và thông tin giao hàng</p>

        <c:if test="${not empty error}">
            <p class="error-msg">${error}</p>
        </c:if>

        <section class="form-card" style="margin-bottom: 1.5rem; max-width: none;">
            <h2 class="order-section-title">Đơn hàng</h2>
            <div class="cart-list">
                <c:forEach items="${checkoutLines}" var="item">
                    <article class="cart-item" style="grid-template-columns: 80px 1fr;">
                        <div class="cart-item__cover">
                            <img src="images/${item.book.coverImage}" alt="${item.book.title}">
                        </div>
                        <div class="cart-item__info">
                            <h2 class="cart-item__title">${item.book.title}</h2>
                            <p class="cart-item__meta">
                                <fmt:formatNumber value="${item.book.price}" type="number" groupingUsed="true" maxFractionDigits="0"/> đ
                                x ${item.quantity}
                            </p>
                            <p class="cart-item__price">
                                <fmt:formatNumber value="${item.subtotal}" type="number" groupingUsed="true" maxFractionDigits="0"/> đ
                            </p>
                        </div>
                    </article>
                </c:forEach>
            </div>
            <p class="cart-item__price" style="margin-top: 1rem; font-size: 1.05rem;">
                Tổng cộng:
                <fmt:formatNumber value="${totalAmount}" type="number" groupingUsed="true" maxFractionDigits="0"/> đ
            </p>
        </section>

        <section class="form-card">
            <h2 class="order-section-title">Thông tin giao hàng</h2>
            <form action="${pageContext.request.contextPath}/checkout" method="post" class="form-grid">
                <div class="form-field">
                    <label for="receiverName">Họ tên người nhận</label>
                    <input type="text" id="receiverName" name="receiverName"
                           value="${not empty receiverName ? receiverName : user.full_name}"
                           required>
                </div>
                <div class="form-field">
                    <label for="phone">Số điện thoại</label>
                    <input type="text" id="phone" name="phone"
                           value="${not empty phone ? phone : user.phone}"
                           required>
                </div>
                <div class="form-field">
                    <label for="address">Địa chỉ giao hàng</label>
                    <textarea id="address" name="address" required>${address}</textarea>
                </div>
                <div class="checkout-actions">
                    <button type="submit" class="btn btn-primary checkout-submit-btn">Đặt hàng ngay</button>
                    <a href="${pageContext.request.contextPath}/cart" class="btn btn-outline">Quay lại giỏ hàng</a>
                </div>
            </form>
        </section>
    </div>
</main>

<footer class="site-footer">
    <div class="container">&copy; BookNest</div>
</footer>

<jsp:include page="/includes/site-scripts.jsp"/>
</body>
</html>
