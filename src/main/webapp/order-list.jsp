<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="vi_VN"/>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đơn hàng của tôi</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="page">

<jsp:include page="/includes/user-header.jsp"/>

<main>
    <div class="container">
        <h1 class="page-title">Đơn hàng của tôi</h1>
        <p class="page-subtitle">Theo dõi trạng thái các đơn hàng đã đặt</p>

        <c:choose>
            <c:when test="${empty orders}">
                <section class="form-card empty-state">
                    <p>Bạn chưa có đơn hàng nào.</p>
                    <a href="${pageContext.request.contextPath}/home" class="btn btn-primary">Mua sách ngay</a>
                </section>
            </c:when>
            <c:otherwise>
                <div class="order-list">
                    <c:forEach items="${orders}" var="order">
                        <article class="order-card">
                            <div class="order-card__header">
                                <div>
                                    <h2 class="order-card__title">Đơn hàng #${order.id}</h2>
                                    <p class="order-card__meta">
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
                            <div class="order-card__body">
                                <p class="order-card__meta">Người nhận: ${order.receiverName}</p>
                                <p class="order-card__meta">SĐT: ${order.phone}</p>
                                <p class="order-card__meta">Địa chỉ: ${order.address}</p>
                                <p class="order-card__total">
                                    Tổng tiền:
                                    <fmt:formatNumber value="${order.totalAmount}" type="number" groupingUsed="true" maxFractionDigits="0"/> đ
                                </p>
                            </div>
                            <div class="order-card__actions">
                                <a href="order?id=${order.id}" class="btn-outline btn-sm">Xem chi tiết</a>
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
