<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Author Form</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="page">

<jsp:include page="/includes/admin-header.jsp"/>

<main>
    <div class="container">
        <span class="admin-badge">Quản lý tác giả</span>
        <h1 class="page-title">Thêm / Sửa tác giả</h1>

        <div class="form-card">
            <form action="${pageContext.request.contextPath}/admin/author" method="post" class="form-grid admin-form">
                <div class="form-field">
                    <label for="author_name">Tên tác giả *</label>
                    <input type="text" id="author_name" name="author_name" value="${aut.name}"
                           placeholder="Nhập tên tác giả..." required maxlength="255">
                </div>
                <div class="form-field">
                    <label for="author_birth">Ngày sinh *</label>
                    <input type="date" id="author_birth" name="author_birth" value="${aut.birth}" required>
                </div>
                <div class="form-field">
                    <label for="author_bio">Tiểu sử</label>
                    <textarea id="author_bio" name="author_bio" rows="4"
                              placeholder="Nhập tiểu sử...">${aut.bio}</textarea>
                </div>
                <c:if test="${not empty aut.id}">
                    <input type="hidden" name="author_id" value="${aut.id}">
                </c:if>
                <div class="btn-group">
                    <button type="submit" class="btn btn-primary">Lưu</button>
                    <a class="btn btn-outline" href="${pageContext.request.contextPath}/admin/author">Hủy</a>
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
