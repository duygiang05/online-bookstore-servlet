<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Không có quyền truy cập</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="page">

<main>
    <div class="container hero">
        <h1>Không có quyền truy cập</h1>
        <p>Bạn không có quyền xem trang này. Vui lòng đăng nhập bằng tài khoản phù hợp.</p>
        <div style="display:flex;gap:0.75rem;justify-content:center;flex-wrap:wrap;margin-top:1.5rem;">
            <a class="btn btn-primary" href="${pageContext.request.contextPath}/login">Đăng nhập</a>
            <a class="btn btn-outline" href="${pageContext.request.contextPath}/home">Về trang chủ</a>
            <c:if test="${not empty sessionScope.user and sessionScope.user.admin}">
                <a class="btn btn-outline" href="${pageContext.request.contextPath}/admin/dashboard">Admin Dashboard</a>
            </c:if>
        </div>
    </div>
</main>

    <jsp:include page="/includes/site-scripts.jsp"/>
</body>
</html>
