package com.bootcamp.bookstore.online_bookstore.util;

import com.bootcamp.bookstore.online_bookstore.model.Book;
import com.bootcamp.bookstore.online_bookstore.model.User;
import com.bootcamp.bookstore.online_bookstore.service.BookService;
import com.bootcamp.bookstore.online_bookstore.service.CartService;
import jakarta.servlet.http.HttpSession;

public final class PendingCartAdd {

    public static final String BOOK_ID_ATTR = "pendingCartBookId";
    public static final String QUANTITY_ATTR = "pendingCartQuantity";

    private PendingCartAdd() {
    }

    public static void save(HttpSession session, String bookId, String quantity) {
        if (session == null || bookId == null || bookId.isBlank()) {
            return;
        }
        session.setAttribute(BOOK_ID_ATTR, bookId.trim());
        String qty = (quantity == null || quantity.isBlank()) ? "1" : quantity.trim();
        session.setAttribute(QUANTITY_ATTR, qty);
    }

    public static void applyIfPresent(HttpSession session, User user,
                                      CartService cartService, BookService bookService) {
        if (session == null || user == null) {
            return;
        }
        Object bookIdObj = session.getAttribute(BOOK_ID_ATTR);
        if (bookIdObj == null) {
            return;
        }
        try {
            int bookId = Integer.parseInt(String.valueOf(bookIdObj));
            Object qtyObj = session.getAttribute(QUANTITY_ATTR);
            int quantity = 1;
            if (qtyObj != null) {
                try {
                    quantity = Integer.parseInt(String.valueOf(qtyObj));
                } catch (NumberFormatException ignored) {
                    quantity = 1;
                }
            }
            if (quantity < 1) {
                quantity = 1;
            }
            Book book = bookService.findActiveById(bookId);
            if (book != null) {
                cartService.addBook(user.getId(), bookId, quantity);
            }
        } catch (Exception ignored) {
            // Pending add is best-effort; clear below either way.
        } finally {
            session.removeAttribute(BOOK_ID_ATTR);
            session.removeAttribute(QUANTITY_ATTR);
        }
    }
}
