package com.bootcamp.bookstore.online_bookstore.util;

import com.bootcamp.bookstore.online_bookstore.model.CheckoutLine;
import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;

public final class CheckoutSession {

    public static final String LINES_ATTR = "checkoutLines";
    public static final String PENDING_BUY_NOW_BOOK = "pendingBuyNowBookId";
    public static final String PENDING_BUY_NOW_QTY = "pendingBuyNowQty";

    private CheckoutSession() {
    }

    @SuppressWarnings("unchecked")
    public static List<CheckoutLine> getLines(HttpSession session) {
        if (session == null) {
            return null;
        }
        Object value = session.getAttribute(LINES_ATTR);
        if (value instanceof List<?> list) {
            return (List<CheckoutLine>) list;
        }
        return null;
    }

    public static void setLines(HttpSession session, List<CheckoutLine> lines) {
        session.setAttribute(LINES_ATTR, lines);
    }

    public static void clear(HttpSession session) {
        if (session != null) {
            session.removeAttribute(LINES_ATTR);
        }
    }

    public static void savePendingBuyNow(HttpSession session, String bookId, String quantity) {
        if (session == null || bookId == null || bookId.isBlank()) {
            return;
        }
        session.setAttribute(PENDING_BUY_NOW_BOOK, bookId.trim());
        String qty = (quantity == null || quantity.isBlank()) ? "1" : quantity.trim();
        session.setAttribute(PENDING_BUY_NOW_QTY, qty);
    }

    public static int[] takePendingBuyNow(HttpSession session) {
        if (session == null) {
            return null;
        }
        Object bookId = session.getAttribute(PENDING_BUY_NOW_BOOK);
        Object qty = session.getAttribute(PENDING_BUY_NOW_QTY);
        session.removeAttribute(PENDING_BUY_NOW_BOOK);
        session.removeAttribute(PENDING_BUY_NOW_QTY);
        if (bookId == null) {
            return null;
        }
        try {
            int id = Integer.parseInt(String.valueOf(bookId));
            int quantity = 1;
            if (qty != null) {
                quantity = Integer.parseInt(String.valueOf(qty));
                if (quantity < 1) {
                    quantity = 1;
                }
            }
            return new int[]{id, quantity};
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static double totalOf(List<CheckoutLine> lines) {
        double total = 0;
        if (lines == null) {
            return total;
        }
        for (CheckoutLine line : lines) {
            total += line.getSubtotal();
        }
        return total;
    }

    public static List<CheckoutLine> copyOf(List<CheckoutLine> lines) {
        return lines == null ? new ArrayList<>() : new ArrayList<>(lines);
    }
}
