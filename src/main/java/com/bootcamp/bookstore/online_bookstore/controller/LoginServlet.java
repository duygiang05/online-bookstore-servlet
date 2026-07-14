package com.bootcamp.bookstore.online_bookstore.controller;

import com.bootcamp.bookstore.online_bookstore.model.User;
import com.bootcamp.bookstore.online_bookstore.service.BookService;
import com.bootcamp.bookstore.online_bookstore.service.CartService;
import com.bootcamp.bookstore.online_bookstore.service.UserService;
import com.bootcamp.bookstore.online_bookstore.util.PendingCartAdd;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final UserService service = new UserService();
    private final CartService cartService = new CartService();
    private final BookService bookService = new BookService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if ("1".equals(request.getParameter("logout"))) {
            request.setAttribute("message", "Đăng xuất thành công.");
        }
        if ("1".equals(request.getParameter("registered"))) {
            request.setAttribute("message", "Đăng ký thành công. Vui lòng đăng nhập.");
        }
        if ("access_denied".equals(request.getParameter("error"))) {
            request.setAttribute("error", "Bạn không có quyền truy cập trang này.");
        }
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String redirect = request.getParameter("redirect");

        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            request.setAttribute("error", "Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu.");
            forwardLogin(request, response, username);
            return;
        }

        try {
            User user = service.login(username.trim(), password);

            if (user == null) {
                request.setAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng.");
                forwardLogin(request, response, username);
                return;
            }

            HttpSession session = request.getSession();
            session.setAttribute("user", user);

            if (!user.isAdmin()) {
                PendingCartAdd.applyIfPresent(session, user, cartService, bookService);
            }

            String target = resolveRedirectTarget(redirect, user);
            response.sendRedirect(request.getContextPath() + target);

        } catch (SQLException | ClassNotFoundException e) {
            request.setAttribute("error", "Không thể đăng nhập. Vui lòng thử lại sau.");
            forwardLogin(request, response, username);
        }
    }

    private String resolveRedirectTarget(String redirect, User user) {
        if (redirect != null && !redirect.isBlank() && redirect.startsWith("/") && !redirect.startsWith("//")) {
            // POST-only endpoints cannot be resumed via sendRedirect (GET)
            if ("/cart/add".equals(redirect)) {
                redirect = "/cart";
            }
            if (user.isAdmin() && redirect.startsWith("/admin")) {
                return redirect;
            }
            if (!user.isAdmin() && !redirect.startsWith("/admin")) {
                return redirect;
            }
        }
        return user.isAdmin() ? "/admin/dashboard" : "/home";
    }

    private void forwardLogin(HttpServletRequest request, HttpServletResponse response, String username)
            throws ServletException, IOException {
        request.setAttribute("username", username);
        String redirect = request.getParameter("redirect");
        if (redirect != null && !redirect.isBlank()) {
            request.setAttribute("redirect", redirect);
        }
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }
}
