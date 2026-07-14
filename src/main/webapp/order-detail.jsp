<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="vi_VN"/>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chi tiết đơn hàng #${order.id}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="page">

<jsp:include page="/includes/user-header.jsp"/>

<main>
    <div class="container">
        <c:if test="${success}">
            <div class="success-banner">
                Đặt hàng thành công! Cảm ơn bạn đã mua sách tại BookNest.
            </div>
        </c:if>

        <div class="order-detail-header">
            <div>
                <h1 class="page-title">Đơn hàng #${order.id}</h1>
                <p class="page-subtitle">
                    Ngày đặt:
                    <fmt:formatDate value="${order.createdAt}" pattern="dd/MM/yyyy HH:mm"/>
                </p>
            </div>
            <span class="order-status order-status--${order.status}">
                <c:choose>
                    <c:when test="${order.status == 'PENDING'}">Chờ xử lý</c:when>
                    <c:when test="${order.status == 'CONFIRMED'}">Đã xác nhận</c:when>
                    <c:when test="${order.status == 'SHIPPED'}">Đang giao</c:when>
                    <c:when test="${order.status == 'COMPLETED'}">Hoàn thành</c:when>
                    <c:when test="${order.status == 'CANCELLED'}">Đã hủy</c:when>
                    <c:otherwise>${order.status}</c:otherwise>
                </c:choose>
            </span>
        </div>

        <section class="form-card" style="margin-bottom: 1.5rem;">
            <h2 class="order-section-title">Thông tin giao hàng</h2>
            <div class="profile-card" style="max-width: none; box-shadow: none; border: none; padding: 0;">
                <div class="profile-row">
                    <span class="profile-label">Người nhận</span>
                    <span>${order.receiverName}</span>
                </div>
                <div class="profile-row">
                    <span class="profile-label">Số điện thoại</span>
                    <span>${order.phone}</span>
                </div>
                <div class="profile-row">
                    <span class="profile-label">Địa chỉ</span>
                    <span>${order.address}</span>
                </div>
            </div>
        </section>

        <section class="form-card">
            <h2 class="order-section-title">Sản phẩm</h2>
            <div class="cart-list">
                <c:forEach items="${order.items}" var="item">
                    <article class="cart-item" style="grid-template-columns: 1fr auto;">
                        <div class="cart-item__info">
                            <h2 class="cart-item__title">
                                <a href="book?id=${item.book.id}">${item.bookTitle}</a>
                            </h2>
                            <p class="cart-item__meta">
                                <fmt:formatNumber value="${item.bookPrice}" type="number" groupingUsed="true" maxFractionDigits="0"/> đ
                                x ${item.quantity}
                            </p>
                        </div>
                        <p class="cart-item__price">
                            <fmt:formatNumber value="${item.subtotal}" type="number" groupingUsed="true" maxFractionDigits="0"/> đ
                        </p>
                    </article>
                </c:forEach>
            </div>
            <p class="order-total">
                Tổng cộng:
                <fmt:formatNumber value="${order.totalAmount}" type="number" groupingUsed="true" maxFractionDigits="0"/> đ
            </p>
        </section>

        <p class="order-actions">
            <a href="${pageContext.request.contextPath}/order" class="btn btn-outline">Quay lại danh sách</a>
            <a href="${pageContext.request.contextPath}/home" class="btn btn-primary">Tiếp tục mua sách</a>
        </p>
    </div>
</main>

<footer class="site-footer">
    <div class="container">&copy; BookNest</div>
</footer>

    <jsp:include page="/includes/site-scripts.jsp"/>
</body>
</html>
