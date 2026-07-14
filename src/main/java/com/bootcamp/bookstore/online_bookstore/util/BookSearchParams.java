package com.bootcamp.bookstore.online_bookstore.util;

import com.bootcamp.bookstore.online_bookstore.model.BookSearchFilter;
import jakarta.servlet.http.HttpServletRequest;

public final class BookSearchParams {

    private BookSearchParams() {
    }

    public static BookSearchFilter fromRequest(HttpServletRequest request) {
        BookSearchFilter filter = new BookSearchFilter();
        filter.setTitle(blankToNull(request.getParameter("title")));
        filter.setAuthorId(parsePositiveInt(request.getParameter("authorId")));
        filter.setCategoryId(parsePositiveInt(request.getParameter("categoryId")));
        filter.setPublisherId(parsePositiveInt(request.getParameter("publisherId")));
        filter.setPublishedYear(parsePositiveInt(request.getParameter("publishedYear")));
        filter.setPriceMin(parseDouble(request.getParameter("priceMin")));
        filter.setPriceMax(parseDouble(request.getParameter("priceMax")));
        return filter;
    }

    private static String blankToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }

    private static Integer parsePositiveInt(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            int parsed = Integer.parseInt(value.trim());
            return parsed > 0 ? parsed : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static Double parseDouble(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
