<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Category</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="page">

<jsp:include page="/includes/admin-header.jsp"/>

<main>
    <div class="container">
        <span class="admin-badge">Quản lý thể loại</span>
        <div class="admin-header">
            <div>
                <h1 class="page-title">Danh sách thể loại</h1>
                <p class="page-subtitle" style="margin-bottom:0;">Thêm, sửa, xóa thể loại</p>
            </div>
            <a class="btn btn-primary" href="${pageContext.request.contextPath}/admin/category?action=add">Add</a>
        </div>

        <div class="admin-list">
            <c:forEach items="${categories}" var="cate">
                <article class="admin-item">
                    <div class="admin-item__info">
                        <h2 class="admin-item__title">${cate.name}</h2>
                    </div>
                    <div class="admin-item__actions">
                        <a class="btn btn-outline btn-sm" href="${pageContext.request.contextPath}/admin/category?action=edit&id=${cate.id}">Edit</a>
                        <form action="${pageContext.request.contextPath}/admin/category?action=delete" method="post">
                            <input type="hidden" name="category_id" value="${cate.id}">
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
