<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Author</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="page">

<jsp:include page="/includes/admin-header.jsp"/>

<main>
    <div class="container">
        <span class="admin-badge">Quản lý tác giả</span>
        <div class="admin-header">
            <div>
                <h1 class="page-title">Danh sách tác giả</h1>
                <p class="page-subtitle" style="margin-bottom:0;">Thêm, sửa, xóa tác giả</p>
            </div>
            <a class="btn btn-primary" href="${pageContext.request.contextPath}/admin/author?action=add">Add</a>
        </div>

        <div class="admin-list">
            <c:forEach items="${authors}" var="aut">
                <article class="admin-item">
                    <div class="admin-item__info">
                        <h2 class="admin-item__title">${aut.name}</h2>
                        <p class="admin-item__meta">Ngày sinh: ${aut.birth}</p>
                        <p class="admin-item__meta">Tiểu sử: ${aut.bio}</p>
                    </div>
                    <div class="admin-item__actions">
                        <a class="btn btn-outline btn-sm" href="${pageContext.request.contextPath}/admin/author?action=edit&id=${aut.id}">Edit</a>
                        <form action="${pageContext.request.contextPath}/admin/author?action=delete" method="post">
                            <input type="hidden" name="author_id" value="${aut.id}">
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
