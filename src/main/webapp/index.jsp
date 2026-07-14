<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>BookNest</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="page">
<main>
    <div class="container" style="padding-top: 3rem;">
        <section class="store-masthead" style="max-width: 640px; margin: 0 auto; text-align: left;">
            <p class="brand-mark">BookNest</p>
            <h1 class="page-title">Cửa hàng sách trực tuyến</h1>
            <p class="page-subtitle">Khám phá bộ sưu tập sách — lọc theo tác giả, thể loại và giá.</p>
            <p style="margin: 1.25rem 0 0;">
                <a class="btn btn-primary" href="${pageContext.request.contextPath}/home">Vào cửa hàng</a>
            </p>
        </section>
    </div>
</main>
<footer class="site-footer">
    <div class="container">&copy; BookNest</div>
</footer>
<jsp:include page="/includes/site-scripts.jsp"/>
</body>
</html>
