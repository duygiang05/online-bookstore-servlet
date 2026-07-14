package com.bootcamp.bookstore.online_bookstore.controller;

import com.bootcamp.bookstore.online_bookstore.model.Book;
import com.bootcamp.bookstore.online_bookstore.model.BookSearchFilter;
import com.bootcamp.bookstore.online_bookstore.service.AuthorService;
import com.bootcamp.bookstore.online_bookstore.service.BookService;
import com.bootcamp.bookstore.online_bookstore.service.CategoryService;
import com.bootcamp.bookstore.online_bookstore.service.PublisherService;
import com.bootcamp.bookstore.online_bookstore.util.BookSearchParams;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {

    private final BookService bookService = new BookService();
    private final CategoryService categoryService = new CategoryService();
    private final AuthorService authorService = new AuthorService();
    private final PublisherService publisherService = new PublisherService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        BookSearchFilter filter = BookSearchParams.fromRequest(request);

        try {
            List<Book> books = filter.hasCriteria()
                    ? bookService.search(filter)
                    : bookService.findAll();

            request.setAttribute("books", books);
            request.setAttribute("categories", categoryService.findAll());
            request.setAttribute("authors", authorService.findAll());
            request.setAttribute("publishers", publisherService.findAll());
            request.setAttribute("searching", filter.hasCriteria());
            request.setAttribute("searchAction", request.getContextPath() + "/home");
            request.getRequestDispatcher("/home.jsp").forward(request, response);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
