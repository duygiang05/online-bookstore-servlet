package com.bootcamp.bookstore.online_bookstore.controller.admin;

import com.bootcamp.bookstore.online_bookstore.model.Order;
import com.bootcamp.bookstore.online_bookstore.service.OrderService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/admin/order")
public class AdminOrderServlet extends HttpServlet {

    private final OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idParam = request.getParameter("id");

        try {
            if (idParam == null || idParam.isBlank()) {
                List<Order> orders = orderService.findAllForAdmin();
                request.setAttribute("orders", orders);
                request.getRequestDispatcher("/admin-order-list.jsp").forward(request, response);
                return;
            }

            int orderId = Integer.parseInt(idParam);
            Order order = orderService.findOrderDetailForAdmin(orderId);
            if (order == null) {
                response.sendRedirect(request.getContextPath() + "/admin/order");
                return;
            }

            request.setAttribute("order", order);
            request.setAttribute("updated", "1".equals(request.getParameter("updated")));
            request.getRequestDispatcher("/admin-order-detail.jsp").forward(request, response);

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int orderId = Integer.parseInt(request.getParameter("order_id"));
        String newStatus = request.getParameter("status");

        try {
            Order order = orderService.findOrderDetailForAdmin(orderId);
            if (order == null) {
                response.sendRedirect(request.getContextPath() + "/admin/order");
                return;
            }

            orderService.updateOrderStatus(orderId, order.getStatus(), newStatus);
            response.sendRedirect(request.getContextPath() + "/admin/order?id=" + orderId + "&updated=1");

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            response.sendRedirect(request.getContextPath() + "/admin/order?id=" + orderId);
        }
    }
}

