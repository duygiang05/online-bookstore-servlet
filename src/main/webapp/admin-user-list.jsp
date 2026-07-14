<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý người dùng</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="page">

<jsp:include page="/includes/admin-header.jsp"/>

<main>
    <div class="container">
        <span class="admin-badge">Quản lý tài khoản</span>
        <div class="admin-header">
            <div>
                <h1 class="page-title">Danh sách người dùng</h1>
                <p class="page-subtitle" style="margin-bottom:0;">Quản lý tài khoản USER và ADMIN</p>
            </div>
            <a class="btn btn-primary" href="${pageContext.request.contextPath}/admin/user?action=add">Thêm</a>
        </div>

        <c:if test="${not empty message}">
            <div class="success-banner" style="margin-bottom:1rem;">${message}</div>
        </c:if>
        <c:if test="${not empty error}">
            <div class="error-banner" style="margin-bottom:1rem;">${error}</div>
        </c:if>

        <div class="admin-list">
            <c:forEach items="${users}" var="u">
                <article class="admin-item">
                    <div class="admin-item__info">
                        <h2 class="admin-item__title">${u.full_name}</h2>
                        <p class="page-subtitle" style="margin:0.25rem 0 0;">
                            @${u.username} · ${u.email}
                            <c:if test="${not empty u.phone}"> · ${u.phone}</c:if>
                            · <strong>${u.role}</strong>
                        </p>
                    </div>
                    <div class="admin-item__actions">
                        <a class="btn btn-outline btn-sm"
                           href="${pageContext.request.contextPath}/admin/user?action=edit&id=${u.id}">Sửa</a>
                        <c:if test="${u.id != sessionScope.user.id}">
                            <form action="${pageContext.request.contextPath}/admin/user" method="post"
                                  onsubmit="return confirm('Xóa tài khoản này?');">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="id" value="${u.id}">
                                <button type="submit" class="btn-danger btn-sm">Xóa</button>
                            </form>
                        </c:if>
                    </div>
                </article>
            </c:forEach>
        </div>
    </div>
</main>

<footer class="site-footer">
    <div class="container">&copy; BookNest Admin</div>
</footer>

    <jsp:include page="/includes/site-scripts.jsp"/>
</body>
</html>
