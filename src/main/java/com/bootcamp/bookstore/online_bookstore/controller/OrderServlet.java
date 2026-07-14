package com.bootcamp.bookstore.online_bookstore.controller;

import com.bootcamp.bookstore.online_bookstore.model.Order;
import com.bootcamp.bookstore.online_bookstore.model.User;
import com.bootcamp.bookstore.online_bookstore.service.OrderService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/order")
public class OrderServlet extends HttpServlet {

    private OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = getLoggedInUser(request, response);
        if (user == null) {
            return;
        }

        String orderIdParam = request.getParameter("id");
        if (orderIdParam == null || orderIdParam.isBlank()) {
            showOrderList(request, response, user);
            return;
        }

        showOrderDetail(request, response, user, Integer.parseInt(orderIdParam));
    }

    private void showOrderList(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        try {
            List<Order> orders = orderService.findByUserId(user.getId());
            request.setAttribute("orders", orders);
            request.getRequestDispatcher("/order-list.jsp").forward(request, response);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void showOrderDetail(HttpServletRequest request, HttpServletResponse response,
                                 User user, int orderId) throws ServletException, IOException {
        try {
            Order order = orderService.findOrderDetail(orderId, user.getId());
            if (order == null) {
                response.sendRedirect(request.getContextPath() + "/order");
                return;
            }

            request.setAttribute("order", order);
            request.setAttribute("success", "1".equals(request.getParameter("success")));
            request.getRequestDispatcher("/order-detail.jsp").forward(request, response);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private User getLoggedInUser(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return null;
        }

        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return null;
        }
        return user;
    }
}
