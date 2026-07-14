package com.bootcamp.bookstore.online_bookstore.controller;

import com.bootcamp.bookstore.online_bookstore.model.Book;
import com.bootcamp.bookstore.online_bookstore.model.User;
import com.bootcamp.bookstore.online_bookstore.service.BookService;
import com.bootcamp.bookstore.online_bookstore.service.CartService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/cart/add")
public class AddToCartServlet extends HttpServlet {
    private CartService service = new CartService();
    private BookService bookService = new BookService();

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        int bookId = Integer.parseInt(request.getParameter("book_id"));
        int quantity = parseQuantity(request.getParameter("quantity"));

        try {
            Book book = bookService.findActiveById(bookId);
            if (book == null) {
                response.sendRedirect(request.getContextPath() + "/home");
                return;
            }
            service.addBook(user.getId(), bookId, quantity);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        response.sendRedirect(request.getContextPath() + "/cart");
    }

    private int parseQuantity(String raw) {
        try {
            int quantity = Integer.parseInt(raw == null ? "1" : raw.trim());
            // cart +/- uses negative delta; preserve that
            if (quantity == 0) {
                return 1;
            }
            return quantity;
        } catch (NumberFormatException e) {
            return 1;
        }
    }
}
