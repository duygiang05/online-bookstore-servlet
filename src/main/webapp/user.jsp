<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="page">

<jsp:include page="/includes/user-header.jsp"/>

<main>
    <div class="container">
        <h1 class="page-title">Thông tin tài khoản</h1>

        <div class="profile-card">
            <div class="profile-row">
                <span class="profile-label">User</span>
                <span>${user.username}</span>
            </div>
            <div class="profile-row">
                <span class="profile-label">Full Name</span>
                <span>${user.full_name}</span>
            </div>
            <div class="profile-row">
                <span class="profile-label">Email</span>
                <span>${user.email}</span>
            </div>
            <div class="profile-row">
                <span class="profile-label">Phone</span>
                <span>${user.phone}</span>
            </div>
            <div class="profile-row">
                <span class="profile-label">Role</span>
                <span>${user.role}</span>
            </div>
        </div>

        <p style="margin-top: 1.5rem;">
            <a href="home" class="btn btn-outline">Back</a>
        </p>
    </div>
</main>

<footer class="site-footer">
    <div class="container">&copy; BookNest</div>
</footer>

    <jsp:include page="/includes/site-scripts.jsp"/>
</body>
</html>
