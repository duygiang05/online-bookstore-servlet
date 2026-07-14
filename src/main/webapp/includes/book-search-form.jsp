<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<form class="search-bar search-bar--filters" action="${searchAction}" method="get">
    <div class="search-field">
        <label for="title">Tên sách</label>
        <input type="text" id="title" name="title" placeholder="Nhập tên sách..."
               value="${param.title}">
    </div>

    <div class="search-field">
        <label for="authorId">Tác giả</label>
        <select id="authorId" name="authorId">
            <option value="0">-- Tất cả tác giả --</option>
            <c:forEach items="${authors}" var="author">
                <option value="${author.id}" ${param.authorId == author.id ? 'selected' : ''}>
                    ${author.name}
                </option>
            </c:forEach>
        </select>
    </div>

    <div class="search-field">
        <label for="categoryId">Thể loại</label>
        <select id="categoryId" name="categoryId">
            <option value="0">-- Tất cả thể loại --</option>
            <c:forEach items="${categories}" var="cat">
                <option value="${cat.id}" ${param.categoryId == cat.id ? 'selected' : ''}>
                    ${cat.name}
                </option>
            </c:forEach>
        </select>
    </div>

    <div class="search-field">
        <label for="publisherId">Nhà xuất bản</label>
        <select id="publisherId" name="publisherId">
            <option value="0">-- Tất cả NXB --</option>
            <c:forEach items="${publishers}" var="pub">
                <option value="${pub.id}" ${param.publisherId == pub.id ? 'selected' : ''}>
                    ${pub.name}
                </option>
            </c:forEach>
        </select>
    </div>

    <div class="search-field">
        <label for="publishedYear">Năm xuất bản</label>
        <input type="number" id="publishedYear" name="publishedYear" min="1000" max="9999"
               placeholder="VD: 2020" value="${param.publishedYear}">
    </div>

    <div class="search-field">
        <label for="priceMin">Giá từ</label>
        <input type="number" id="priceMin" name="priceMin" min="0" step="any"
               placeholder="Min" value="${param.priceMin}">
    </div>

    <div class="search-field">
        <label for="priceMax">Giá đến</label>
        <input type="number" id="priceMax" name="priceMax" min="0" step="any"
               placeholder="Max" value="${param.priceMax}">
    </div>

    <div class="search-actions">
        <button type="submit" class="btn btn-primary">Tìm kiếm</button>
        <c:if test="${searching}">
            <a href="${searchAction}" class="btn btn-outline">Xóa bộ lọc</a>
        </c:if>
    </div>
</form>
