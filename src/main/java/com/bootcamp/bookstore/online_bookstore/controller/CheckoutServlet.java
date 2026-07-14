package com.bootcamp.bookstore.online_bookstore.controller;

import com.bootcamp.bookstore.online_bookstore.model.CheckoutLine;
import com.bootcamp.bookstore.online_bookstore.model.User;
import com.bootcamp.bookstore.online_bookstore.service.OrderService;
import com.bootcamp.bookstore.online_bookstore.util.CheckoutSession;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {

    private final OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = getLoggedInUser(request, response);
        if (user == null) {
            return;
        }

        HttpSession session = request.getSession();
        try {
            restorePendingBuyNowIfNeeded(session);

            List<CheckoutLine> lines = CheckoutSession.getLines(session);
            if (lines == null || lines.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/cart");
                return;
            }

            request.setAttribute("checkoutLines", lines);
            request.setAttribute("totalAmount", CheckoutSession.totalOf(lines));
            request.setAttribute("user", user);
            request.getRequestDispatcher("/checkout.jsp").forward(request, response);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (RuntimeException e) {
            CheckoutSession.clear(session);
            response.sendRedirect(request.getContextPath() + "/cart?error="
                    + java.net.URLEncoder.encode(e.getMessage(), java.nio.charset.StandardCharsets.UTF_8));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = getLoggedInUser(request, response);
        if (user == null) {
            return;
        }

        HttpSession session = request.getSession();
        String action = request.getParameter("action");

        try {
            if ("prepareBuyNow".equals(action)) {
                int bookId = Integer.parseInt(request.getParameter("book_id"));
                int quantity = parseQuantity(request.getParameter("quantity"));
                List<CheckoutLine> lines = orderService.buildBuyNowLine(bookId, quantity);
                CheckoutSession.setLines(session, lines);
                response.sendRedirect(request.getContextPath() + "/checkout");
                return;
            }

            if ("prepareCart".equals(action)) {
                String[] selected = request.getParameterValues("selectedItem");
                List<CheckoutLine> lines = orderService.buildLinesFromCartSelection(user, selected, request);
                CheckoutSession.setLines(session, lines);
                response.sendRedirect(request.getContextPath() + "/checkout");
                return;
            }

            // place order
            List<CheckoutLine> lines = CheckoutSession.getLines(session);
            if (lines == null || lines.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/cart");
                return;
            }

            String receiverName = request.getParameter("receiverName");
            String phone = request.getParameter("phone");
            String address = request.getParameter("address");

            int orderId = orderService.checkoutSelected(user, receiverName, phone, address, lines);
            CheckoutSession.clear(session);
            response.sendRedirect(request.getContextPath() + "/order?id=" + orderId + "&success=1");
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/home");
        } catch (RuntimeException e) {
            if ("prepareCart".equals(action) || "prepareBuyNow".equals(action)) {
                String back = "prepareBuyNow".equals(action) ? "/home" : "/cart";
                response.sendRedirect(request.getContextPath() + back + "?error="
                        + java.net.URLEncoder.encode(e.getMessage(), java.nio.charset.StandardCharsets.UTF_8));
                return;
            }
            forwardWithError(request, response, user, e.getMessage(),
                    request.getParameter("receiverName"),
                    request.getParameter("phone"),
                    request.getParameter("address"));
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void restorePendingBuyNowIfNeeded(HttpSession session)
            throws SQLException, ClassNotFoundException {
        List<CheckoutLine> existing = CheckoutSession.getLines(session);
        if (existing != null && !existing.isEmpty()) {
            return;
        }
        int[] pending = CheckoutSession.takePendingBuyNow(session);
        if (pending == null) {
            return;
        }
        List<CheckoutLine> lines = orderService.buildBuyNowLine(pending[0], pending[1]);
        CheckoutSession.setLines(session, lines);
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

    private void forwardWithError(HttpServletRequest request, HttpServletResponse response,
                                  User user, String error,
                                  String receiverName, String phone, String address)
            throws ServletException, IOException {
        List<CheckoutLine> lines = CheckoutSession.getLines(request.getSession());
        if (lines == null || lines.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }
        request.setAttribute("checkoutLines", lines);
        request.setAttribute("totalAmount", CheckoutSession.totalOf(lines));
        request.setAttribute("user", user);
        request.setAttribute("error", error);
        request.setAttribute("receiverName", receiverName);
        request.setAttribute("phone", phone);
        request.setAttribute("address", address);
        request.getRequestDispatcher("/checkout.jsp").forward(request, response);
    }

    private int parseQuantity(String raw) {
        try {
            int quantity = Integer.parseInt(raw == null ? "1" : raw.trim());
            return quantity < 1 ? 1 : quantity;
        } catch (NumberFormatException e) {
            return 1;
        }
    }
}
