package com.bootcamp.bookstore.online_bookstore.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * One line selected for checkout (from cart or buy-now).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutLine {
    private Integer cartItemId;
    private int bookId;
    private int quantity;
    private Book book;

    public double getSubtotal() {
        if (book == null) {
            return 0;
        }
        return book.getPrice() * quantity;
    }
}
