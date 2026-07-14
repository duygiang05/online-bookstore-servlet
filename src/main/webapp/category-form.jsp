<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Category Form</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="page">

<jsp:include page="/includes/admin-header.jsp"/>

<main>
    <div class="container">
        <span class="admin-badge">Quản lý thể loại</span>
        <h1 class="page-title">Thêm / Sửa thể loại</h1>

        <div class="form-card">
            <form action="${pageContext.request.contextPath}/admin/category" method="post" class="form-grid admin-form">
                <div class="form-field">
                    <label for="category_name">Tên thể loại *</label>
                    <input type="text" id="category_name" name="category_name" value="${cate.name}"
                           placeholder="Nhập thể loại..." required maxlength="255">
                </div>
                <c:if test="${not empty cate.id}">
                    <input type="hidden" name="category_id" value="${cate.id}">
                </c:if>
                <div class="btn-group">
                    <button type="submit" class="btn btn-primary">Lưu</button>
                    <a class="btn btn-outline" href="${pageContext.request.contextPath}/admin/category">Hủy</a>
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
