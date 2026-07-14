<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="vi_VN"/>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cart</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="page">

<jsp:include page="/includes/user-header.jsp"/>

<main>
    <div class="container">
        <h1 class="page-title">Giỏ hàng</h1>
        <p class="page-subtitle">Chọn sách và số lượng muốn thanh toán</p>

        <c:if test="${not empty param.error}">
            <div class="error-banner" style="margin-bottom:1rem;">${param.error}</div>
        </c:if>

        <c:choose>
            <c:when test="${empty cart.items}">
                <section class="form-card empty-state">
                    <p>Giỏ hàng đang trống.</p>
                    <a href="${pageContext.request.contextPath}/home" class="btn btn-primary">Mua sách</a>
                </section>
            </c:when>
            <c:otherwise>
                <div class="cart-list">
                    <c:forEach items="${cart.items}" var="item">
                        <article class="cart-item cart-item--selectable">
                            <label class="cart-item__check">
                                <input type="checkbox" name="selectedItem" value="${item.id}"
                                       form="cartCheckoutForm"
                                       class="cart-select" data-price="${item.book.price}" checked>
                            </label>
                            <div class="cart-item__cover">
                                <img src="images/${item.book.coverImage}" alt="${item.book.title}">
                            </div>
                            <div class="cart-item__info">
                                <h2 class="cart-item__title">
                                    <a href="book?id=${item.book.id}">${item.book.title}</a>
                                </h2>
                                <p class="cart-item__meta">Tác giả: ${item.book.author.name}</p>
                                <p class="cart-item__price">
                                    <fmt:formatNumber value="${item.book.price}" type="number" groupingUsed="true" maxFractionDigits="0"/> đ
                                </p>
                                <div class="cart-item__buy-qty">
                                    <span>Mua</span>
                                    <div class="qty-picker qty-picker--sm" data-qty-picker>
                                        <button type="button" class="qty-btn" data-qty-delta="-1">−</button>
                                        <input type="number"
                                               name="qty_${item.id}"
                                               form="cartCheckoutForm"
                                               class="cart-buy-qty"
                                               value="${item.quantity}"
                                               min="1"
                                               max="${item.quantity}"
                                               data-price="${item.book.price}"
                                               required>
                                        <button type="button" class="qty-btn" data-qty-delta="1">+</button>
                                    </div>
                                    <span class="cart-item__meta">/ ${item.quantity} trong giỏ</span>
                                </div>
                            </div>
                            <div class="cart-item__actions">
                                <div class="qty-controls">
                                    <form action="${pageContext.request.contextPath}/cart/add" method="post">
                                        <input type="hidden" name="book_id" value="${item.book.id}">
                                        <input type="hidden" name="quantity" value="-1">
                                        <button type="submit" class="btn-outline btn-sm">−</button>
                                    </form>
                                    <form action="${pageContext.request.contextPath}/cart/add" method="post">
                                        <input type="hidden" name="book_id" value="${item.book.id}">
                                        <input type="hidden" name="quantity" value="1">
                                        <button type="submit" class="btn-outline btn-sm">+</button>
                                    </form>
                                </div>
                                <form action="${pageContext.request.contextPath}/cart" method="post">
                                    <input type="hidden" name="book_id" value="${item.book.id}">
                                    <input type="hidden" name="cart_id" value="${cart.id}">
                                    <button type="submit" class="btn-danger btn-sm">Xóa</button>
                                </form>
                            </div>
                        </article>
                    </c:forEach>
                </div>

                <form action="${pageContext.request.contextPath}/checkout" method="post" id="cartCheckoutForm">
                    <input type="hidden" name="action" value="prepareCart">
                    <div class="checkout-bar">
                        <div class="checkout-bar__info">
                            <label class="checkout-bar__select-all">
                                <input type="checkbox" id="selectAllCart" checked>
                                Chọn tất cả
                            </label>
                            <p class="checkout-bar__total">
                                Tạm tính:
                                <strong id="cartSelectedTotal">0 đ</strong>
                            </p>
                        </div>
                        <button type="submit" class="btn btn-primary checkout-bar__btn" id="cartCheckoutBtn">
                            Thanh toán đã chọn
                        </button>
                    </div>
                </form>
            </c:otherwise>
        </c:choose>
    </div>
</main>

<footer class="site-footer">
    <div class="container">&copy; BookNest</div>
</footer>

<jsp:include page="/includes/site-scripts.jsp"/>
</body>
</html>
