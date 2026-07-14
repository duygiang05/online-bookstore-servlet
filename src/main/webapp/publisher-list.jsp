<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Publisher</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="page">

<jsp:include page="/includes/admin-header.jsp"/>

<main>
    <div class="container">
        <span class="admin-badge">Quản lý NXB</span>
        <div class="admin-header">
            <div>
                <h1 class="page-title">Danh sách nhà xuất bản</h1>
                <p class="page-subtitle" style="margin-bottom:0;">Thêm, sửa, xóa NXB</p>
            </div>
            <a class="btn btn-primary" href="${pageContext.request.contextPath}/admin/publisher?action=add">Add</a>
        </div>

        <div class="admin-list">
            <c:forEach items="${publishers}" var="pub">
                <article class="admin-item">
                    <div class="admin-item__info">
                        <h2 class="admin-item__title">${pub.name}</h2>
                    </div>
                    <div class="admin-item__actions">
                        <a class="btn btn-outline btn-sm" href="${pageContext.request.contextPath}/admin/publisher?action=edit&id=${pub.id}">Edit</a>
                        <form action="${pageContext.request.contextPath}/admin/publisher?action=delete" method="post">
                            <input type="hidden" name="publisher_id" value="${pub.id}">
                            <button type="submit" class="btn-danger btn-sm">Delete</button>
                        </form>
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
