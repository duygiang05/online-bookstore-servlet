<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<header class="site-header">
    <div class="container">
        <a class="brand" href="${pageContext.request.contextPath}/home">Book<span>Nest</span></a>
        <nav class="nav-links">
            <a href="${pageContext.request.contextPath}/home">Trang chủ</a>
            <c:choose>
                <c:when test="${not empty sessionScope.user}">
                    <c:choose>
                        <c:when test="${sessionScope.user.admin}">
                            <a href="${pageContext.request.contextPath}/admin/dashboard">Quản trị</a>
                        </c:when>
                        <c:otherwise>
                            <a href="${pageContext.request.contextPath}/cart">Giỏ hàng</a>
                            <a href="${pageContext.request.contextPath}/order">Đơn hàng</a>
                            <a href="${pageContext.request.contextPath}/user?id=${sessionScope.user.id}">Tài khoản</a>
                        </c:otherwise>
                    </c:choose>
                    <span class="nav-user">${sessionScope.user.full_name}</span>
                    <a href="${pageContext.request.contextPath}/logout">Đăng xuất</a>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/login">Đăng nhập</a>
                    <a href="${pageContext.request.contextPath}/register">Đăng ký</a>
                </c:otherwise>
            </c:choose>
        </nav>
    </div>
</header>
