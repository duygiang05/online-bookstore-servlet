<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng ký</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="page">

<div class="login-wrap">
    <div class="login-card">
        <h1>Đăng ký</h1>
        <p class="page-subtitle" style="text-align:center;margin-bottom:1.25rem;">
            Tạo tài khoản để mua sách tại BookNest
        </p>

        <c:if test="${not empty error}">
            <div class="error-banner" style="margin-bottom:1rem;">${error}</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/register" method="post" class="form-grid">
            <div class="form-field">
                <label for="username">Tên đăng nhập</label>
                <input type="text" id="username" name="username" placeholder="Username"
                       value="${username}" required autofocus>
            </div>
            <div class="form-field">
                <label for="password">Mật khẩu</label>
                <input type="password" id="password" name="password" placeholder="Password" required>
            </div>
            <div class="form-field">
                <label for="confirm_password">Xác nhận mật khẩu</label>
                <input type="password" id="confirm_password" name="confirm_password"
                       placeholder="Nhập lại mật khẩu" required>
            </div>
            <div class="form-field">
                <label for="full_name">Họ và tên</label>
                <input type="text" id="full_name" name="full_name" placeholder="Họ và tên"
                       value="${full_name}" required>
            </div>
            <div class="form-field">
                <label for="email">Email</label>
                <input type="email" id="email" name="email" placeholder="Email"
                       value="${email}" required>
            </div>
            <div class="form-field">
                <label for="phone">Số điện thoại</label>
                <input type="text" id="phone" name="phone" placeholder="Số điện thoại"
                       value="${phone}">
            </div>
            <button type="submit" class="btn btn-primary">Đăng ký</button>
        </form>

        <p style="text-align:center;margin-top:1.25rem;">
            Đã có tài khoản?
            <a href="${pageContext.request.contextPath}/login">Đăng nhập</a>
        </p>
        <p style="text-align:center;margin-top:0.75rem;">
            <a href="${pageContext.request.contextPath}/home" class="btn btn-outline btn-sm">Về trang chủ</a>
        </p>
    </div>
</div>

    <jsp:include page="/includes/site-scripts.jsp"/>
</body>
</html>
