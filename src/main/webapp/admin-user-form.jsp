<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Form tài khoản</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="page">

<jsp:include page="/includes/admin-header.jsp"/>

<main>
    <div class="container">
        <span class="admin-badge">Quản lý tài khoản</span>
        <h1 class="page-title">
            <c:choose>
                <c:when test="${not empty account.id}">Sửa tài khoản</c:when>
                <c:otherwise>Thêm tài khoản</c:otherwise>
            </c:choose>
        </h1>

        <c:if test="${not empty error}">
            <div class="error-banner" style="margin-bottom:1rem;">${error}</div>
        </c:if>

        <div class="form-card">
            <form action="${pageContext.request.contextPath}/admin/user" method="post" class="form-grid admin-form">
                <c:if test="${not empty account.id}">
                    <input type="hidden" name="id" value="${account.id}">
                </c:if>
                <div class="form-field">
                    <label for="username">Tên đăng nhập</label>
                    <input type="text" id="username" name="username" value="${account.username}"
                           placeholder="Username" required>
                </div>

                <div class="form-field">
                    <label for="password">
                        Mật khẩu
                        <c:if test="${not empty account.id}">
                            <span style="font-weight:normal;opacity:0.7;">(để trống nếu giữ nguyên)</span>
                        </c:if>
                    </label>
                    <input type="password" id="password" name="password" placeholder="Password"
                           <c:if test="${empty account.id}">required</c:if>>
                </div>

                <div class="form-field">
                    <label for="full_name">Họ và tên</label>
                    <input type="text" id="full_name" name="full_name" value="${account.full_name}"
                           placeholder="Họ và tên" required>
                </div>

                <div class="form-field">
                    <label for="email">Email</label>
                    <input type="email" id="email" name="email" value="${account.email}"
                           placeholder="Email" required>
                </div>

                <div class="form-field">
                    <label for="phone">Số điện thoại</label>
                    <input type="text" id="phone" name="phone" value="${account.phone}"
                           placeholder="Số điện thoại">
                </div>

                <div class="form-field">
                    <label for="role">Vai trò</label>
                    <select id="role" name="role" required>
                        <c:forEach items="${roles}" var="r">
                            <option value="${r}" <c:if test="${account.role == r}">selected</c:if>>${r}</option>
                        </c:forEach>
                    </select>
                </div>

                <div class="btn-group">
                    <button type="submit" class="btn btn-primary">Lưu</button>
                    <a class="btn btn-outline" href="${pageContext.request.contextPath}/admin/user">Hủy</a>
                </div>
            </form>
        </div>
    </div>
</main>

<footer class="site-footer">
    <div class="container">&copy; BookNest Admin</div>
</footer>

    <jsp:include page="/includes/site-scripts.jsp"/>
</body>
</html>
