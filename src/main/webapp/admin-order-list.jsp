<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="vi_VN"/>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý đơn hàng</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="page">

<jsp:include page="/includes/admin-header.jsp"/>

<main>
    <div class="container">
        <span class="admin-badge">Quản lý đơn hàng</span>
        <div class="admin-header">
            <div>
                <h1 class="page-title">Danh sách đơn hàng</h1>
                <p class="page-subtitle" style="margin-bottom:0;">Theo dõi và xử lý đơn hàng</p>
            </div>
        </div>

        <c:choose>
            <c:when test="${empty orders}">
                <section class="form-card empty-state">
                    <p>Chưa có đơn hàng nào.</p>
                </section>
            </c:when>
            <c:otherwise>
                <div class="admin-list">
                    <c:forEach items="${orders}" var="order">
                        <article class="admin-item">
                            <div class="admin-item__info">
                                <h2 class="admin-item__title">Đơn #${order.id}</h2>
                                <p class="admin-item__meta">
                                    Khách: ${order.user.full_name} (${order.user.email})
                                </p>
                                <p class="admin-item__meta">
                                    <fmt:formatDate value="${order.createdAt}" pattern="dd/MM/yyyy HH:mm"/>
                                </p>
                                <p class="admin-item__meta">Người nhận: ${order.receiverName} - ${order.phone}</p>
                                <p class="admin-item__meta">
                                    Tổng:
                                    <fmt:formatNumber value="${order.totalAmount}" type="number" groupingUsed="true" maxFractionDigits="0"/> đ
                                </p>
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
                            <div class="admin-item__actions">
                                <a class="btn btn-outline btn-sm"
                                   href="${pageContext.request.contextPath}/admin/order?id=${order.id}">
                                    Chi tiết
                                </a>
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
