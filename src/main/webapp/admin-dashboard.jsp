<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="page">

<jsp:include page="/includes/admin-header.jsp"/>

<main>
    <div class="container">
        <section class="store-masthead" style="margin-bottom: 1.75rem;">
            <p class="brand-mark">BookNest Admin</p>
            <h1 class="page-title">Dashboard</h1>
            <p class="page-subtitle">Xin chào, ${sessionScope.user.full_name}</p>
        </section>

        <div class="dashboard-grid">
            <a class="dashboard-card" href="${pageContext.request.contextPath}/admin/book">
                <h2>Quản lý sách</h2>
                <p>Thêm, sửa, ẩn sách khỏi cửa hàng</p>
            </a>
            <a class="dashboard-card" href="${pageContext.request.contextPath}/admin/order">
                <h2>Quản lý đơn hàng</h2>
                <p>Theo dõi và cập nhật trạng thái đơn</p>
            </a>
            <a class="dashboard-card" href="${pageContext.request.contextPath}/admin/author">
                <h2>Quản lý tác giả</h2>
                <p>Thêm, sửa, xóa tác giả</p>
            </a>
            <a class="dashboard-card" href="${pageContext.request.contextPath}/admin/category">
                <h2>Quản lý thể loại</h2>
                <p>Thêm, sửa, xóa thể loại sách</p>
            </a>
            <a class="dashboard-card" href="${pageContext.request.contextPath}/admin/publisher">
                <h2>Quản lý nhà xuất bản</h2>
                <p>Thêm, sửa, xóa nhà xuất bản</p>
            </a>
            <a class="dashboard-card" href="${pageContext.request.contextPath}/admin/user">
                <h2>Quản lý người dùng</h2>
                <p>Thêm, sửa, xóa tài khoản USER và ADMIN</p>
            </a>
        </div>
    </div>
</main>

<footer class="site-footer">
    <div class="container">&copy; BookNest Admin</div>
</footer>

    <jsp:include page="/includes/site-scripts.jsp"/>
</body>
</html>
