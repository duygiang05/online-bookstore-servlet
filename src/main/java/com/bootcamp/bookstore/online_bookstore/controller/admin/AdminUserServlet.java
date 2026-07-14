package com.bootcamp.bookstore.online_bookstore.controller.admin;

import com.bootcamp.bookstore.online_bookstore.model.User;
import com.bootcamp.bookstore.online_bookstore.service.UserService;
import com.bootcamp.bookstore.online_bookstore.util.UserRole;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/admin/user")
public class AdminUserServlet extends HttpServlet {

    private final UserService service = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action != null) {
            switch (action) {
                case "add" -> {
                    request.setAttribute("roles", new String[]{UserRole.USER, UserRole.ADMIN});
                    request.getRequestDispatcher("/admin-user-form.jsp").forward(request, response);
                    return;
                }
                case "edit" -> {
                    try {
                        int id = Integer.parseInt(request.getParameter("id"));
                        User account = service.findById(id);
                        if (account == null) {
                            response.sendRedirect(request.getContextPath() + "/admin/user");
                            return;
                        }
                        request.setAttribute("account", account);
                        request.setAttribute("roles", new String[]{UserRole.USER, UserRole.ADMIN});
                        request.getRequestDispatcher("/admin-user-form.jsp").forward(request, response);
                        return;
                    } catch (NumberFormatException | SQLException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        try {
            List<User> users = service.findAll();
            request.setAttribute("users", users);
            if ("1".equals(request.getParameter("deleted"))) {
                request.setAttribute("message", "Đã xóa tài khoản.");
            }
            if (request.getParameter("error") != null) {
                request.setAttribute("error", request.getParameter("error"));
            }
            request.getRequestDispatcher("/admin-user-list.jsp").forward(request, response);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String action = request.getParameter("action");
        User current = (User) request.getSession().getAttribute("user");

        if ("delete".equals(action)) {
            try {
                int id = Integer.parseInt(request.getParameter("id"));
                String error = service.delete(id, current.getId());
                if (error != null) {
                    response.sendRedirect(request.getContextPath() + "/admin/user?error="
                            + java.net.URLEncoder.encode(error, java.nio.charset.StandardCharsets.UTF_8));
                    return;
                }
                response.sendRedirect(request.getContextPath() + "/admin/user?deleted=1");
            } catch (NumberFormatException | SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            return;
        }

        String id = request.getParameter("id");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String fullName = request.getParameter("full_name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String role = request.getParameter("role");

        try {
            String error;
            if (id == null || id.isBlank()) {
                error = service.add(username, password, fullName, email, phone, role);
            } else {
                error = service.update(Integer.parseInt(id), username, password, fullName, email, phone, role);
            }

            if (error != null) {
                User account = new User();
                if (id != null && !id.isBlank()) {
                    account.setId(Integer.parseInt(id));
                }
                account.setUsername(username);
                account.setFull_name(fullName);
                account.setEmail(email);
                account.setPhone(phone);
                account.setRole(role);
                request.setAttribute("error", error);
                request.setAttribute("account", account);
                request.setAttribute("roles", new String[]{UserRole.USER, UserRole.ADMIN});
                request.getRequestDispatcher("/admin-user-form.jsp").forward(request, response);
                return;
            }

            // Nếu admin tự sửa role/username của mình — cập nhật session
            if (id != null && !id.isBlank() && Integer.parseInt(id) == current.getId()) {
                User refreshed = service.findById(current.getId());
                request.getSession().setAttribute("user", refreshed);
            }

            response.sendRedirect(request.getContextPath() + "/admin/user");
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
