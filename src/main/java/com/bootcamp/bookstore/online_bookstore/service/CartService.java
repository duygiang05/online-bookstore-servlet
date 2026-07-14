package com.bootcamp.bookstore.online_bookstore.service;

import com.bootcamp.bookstore.online_bookstore.dao.CartDAO;
import com.bootcamp.bookstore.online_bookstore.dao.CartItemDAO;
import com.bootcamp.bookstore.online_bookstore.model.Cart;
import com.bootcamp.bookstore.online_bookstore.model.CartItem;

import java.sql.SQLException;
import java.util.List;

public class CartService {

    private final CartDAO cartDAO = new CartDAO();
    private final CartItemDAO cartItemDAO = new CartItemDAO();

    public Cart findByUserId(int userId) throws SQLException, ClassNotFoundException {
        Cart cart = cartDAO.findByUserId(userId);
        if (cart == null) {
            return new Cart();
        }
        cart.setItems(cartItemDAO.findByCartId(cart.getId()));
        return cart;
    }

    public void addBook(int userId, int bookId, int quantity) throws SQLException, ClassNotFoundException {
        Cart cart = cartDAO.findByUserId(userId);
        int cartId = (cart == null) ? cartDAO.create(userId) : cart.getId();

        CartItem item = cartItemDAO.findByCartIdAndBookId(cartId, bookId);
        if (item == null) {
            cartItemDAO.insert(cartId, bookId, quantity);
        } else {
            cartItemDAO.updateQuantity(item.getId(), quantity);
        }
    }

    public void deleteBook(int cart_id, int book_id) throws SQLException, ClassNotFoundException {
        CartItem item = cartItemDAO.findByCartIdAndBookId(cart_id, book_id);
        cartItemDAO.delete(item.getId());
    }
}
