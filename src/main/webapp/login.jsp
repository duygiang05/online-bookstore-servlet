<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng nhập</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="page">

<div class="login-wrap">
    <div class="login-card">
        <h1>Đăng nhập</h1>
        <p class="page-subtitle" style="text-align:center;margin-bottom:1.25rem;">
            Đăng nhập để mua sách hoặc quản trị hệ thống
        </p>

        <c:if test="${not empty message}">
            <div class="success-banner" style="margin-bottom:1rem;">${message}</div>
        </c:if>
        <c:if test="${not empty error}">
            <div class="error-banner" style="margin-bottom:1rem;">${error}</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/login" method="post" class="form-grid">
            <c:if test="${not empty param.redirect}">
                <input type="hidden" name="redirect" value="${param.redirect}">
            </c:if>
            <c:if test="${empty param.redirect and not empty redirect}">
                <input type="hidden" name="redirect" value="${redirect}">
            </c:if>
            <div class="form-field">
                <label for="username">Tên đăng nhập</label>
                <input type="text" id="username" name="username" placeholder="Username"
                       value="${username}" required autofocus>
            </div>
            <div class="form-field">
                <label for="password">Mật khẩu</label>
                <input type="password" id="password" name="password" placeholder="Password" required>
            </div>
            <button type="submit" class="btn btn-primary">Đăng nhập</button>
        </form>

        <p style="text-align:center;margin-top:1.25rem;">
            Chưa có tài khoản?
            <a href="${pageContext.request.contextPath}/register">Đăng ký</a>
        </p>
        <p style="text-align:center;margin-top:0.75rem;">
            <a href="${pageContext.request.contextPath}/home" class="btn btn-outline btn-sm">Về trang chủ</a>
        </p>
    </div>
</div>

    <jsp:include page="/includes/site-scripts.jsp"/>
</body>
</html>
