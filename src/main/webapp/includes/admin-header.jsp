<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<header class="site-header">
    <div class="container">
        <a class="brand" href="${pageContext.request.contextPath}/admin/dashboard">Book<span>Nest</span> Admin</a>
        <nav class="nav-links">
            <a href="${pageContext.request.contextPath}/admin/dashboard">Dashboard</a>
            <a href="${pageContext.request.contextPath}/admin/book">Sách</a>
            <a href="${pageContext.request.contextPath}/admin/order">Đơn hàng</a>
            <a href="${pageContext.request.contextPath}/admin/author">Tác giả</a>
            <a href="${pageContext.request.contextPath}/admin/category">Thể loại</a>
            <a href="${pageContext.request.contextPath}/admin/publisher">NXB</a>
            <a href="${pageContext.request.contextPath}/admin/user">Người dùng</a>
            <span class="nav-user">${sessionScope.user.full_name}</span>
            <a href="${pageContext.request.contextPath}/logout">Đăng xuất</a>
        </nav>
    </div>
</header>
