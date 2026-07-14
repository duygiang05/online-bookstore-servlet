package com.bootcamp.bookstore.online_bookstore.controller;

import com.bootcamp.bookstore.online_bookstore.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private final UserService service = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirm_password");
        String fullName = request.getParameter("full_name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");

        if (password != null && confirmPassword != null && !password.equals(confirmPassword)) {
            request.setAttribute("error", "Mật khẩu xác nhận không khớp.");
            keepForm(request, username, fullName, email, phone);
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        try {
            String error = service.register(username, password, fullName, email, phone);
            if (error != null) {
                request.setAttribute("error", error);
                keepForm(request, username, fullName, email, phone);
                request.getRequestDispatcher("/register.jsp").forward(request, response);
                return;
            }
            response.sendRedirect(request.getContextPath() + "/login?registered=1");
        } catch (SQLException | ClassNotFoundException e) {
            request.setAttribute("error", "Không thể đăng ký. Vui lòng thử lại sau.");
            keepForm(request, username, fullName, email, phone);
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        }
    }

    private void keepForm(HttpServletRequest request, String username, String fullName,
                          String email, String phone) {
        request.setAttribute("username", username);
        request.setAttribute("full_name", fullName);
        request.setAttribute("email", email);
        request.setAttribute("phone", phone);
    }
}
