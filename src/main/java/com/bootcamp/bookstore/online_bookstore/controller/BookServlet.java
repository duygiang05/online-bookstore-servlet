package com.bootcamp.bookstore.online_bookstore.controller;

import com.bootcamp.bookstore.online_bookstore.model.Book;
import com.bootcamp.bookstore.online_bookstore.service.BookService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;


@WebServlet("/book")
public class BookServlet extends HttpServlet {

    private BookService service = new BookService();
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        int bookId = Integer.parseInt(id);
        Book book = null;
        try {
            book = service.findActiveById(bookId);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        if (book == null) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }
        request.setAttribute("book",book);
        request.getRequestDispatcher("/bookdetail.jsp")
                .forward(request,response);
    }
}
