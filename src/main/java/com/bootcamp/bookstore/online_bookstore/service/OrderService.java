package com.bootcamp.bookstore.online_bookstore.service;

import com.bootcamp.bookstore.online_bookstore.dao.*;
import com.bootcamp.bookstore.online_bookstore.model.*;
import com.bootcamp.bookstore.online_bookstore.util.DBConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderService {

    private final CartDAO cartDAO = new CartDAO();
    private final CartItemDAO cartItemDAO = new CartItemDAO();
    private final OrderDAO orderDAO = new OrderDAO();
    private final OrderItemDAO orderItemDAO = new OrderItemDAO();
    private final BookDAO bookDAO = new BookDAO();

    /** Checkout only the selected lines (buy-now or selected cart rows). */
    public int checkoutSelected(User user, String receiverName, String phone, String address,
                                List<CheckoutLine> lines)
            throws SQLException, ClassNotFoundException {
        List<CheckoutLine> resolved = resolveAndValidate(user, lines);

        Connection conn = DBConnection.getConnection();
        try {
            conn.setAutoCommit(false);

            Order order = createOrder(conn, user, receiverName, phone, address, resolved);
            createOrderItems(conn, order, resolved);
            updateStock(conn, resolved);
            applyCartChanges(conn, user, resolved);

            conn.commit();
            return order.getId();
        } catch (Exception e) {
            conn.rollback();
            if (e instanceof RuntimeException re) {
                throw re;
            }
            if (e instanceof SQLException se) {
                throw se;
            }
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            conn.close();
        }
    }

    public List<CheckoutLine> buildBuyNowLine(int bookId, int quantity)
            throws SQLException, ClassNotFoundException {
        if (quantity < 1) {
            quantity = 1;
        }
        Book book = bookDAO.findActiveById(bookId);
        if (book == null) {
            throw new RuntimeException("Sách không tồn tại hoặc đã ngừng bán.");
        }
        if (quantity > book.getStock()) {
            throw new RuntimeException("Không đủ hàng: " + book.getTitle());
        }
        CheckoutLine line = new CheckoutLine();
        line.setCartItemId(null);
        line.setBookId(book.getId());
        line.setQuantity(quantity);
        line.setBook(book);
        List<CheckoutLine> lines = new ArrayList<>();
        lines.add(line);
        return lines;
    }

    public List<CheckoutLine> buildLinesFromCartSelection(User user, String[] cartItemIds,
                                                          jakarta.servlet.http.HttpServletRequest request)
            throws SQLException, ClassNotFoundException {
        if (cartItemIds == null || cartItemIds.length == 0) {
            throw new RuntimeException("Vui lòng chọn ít nhất một sách để thanh toán.");
        }

        Cart cart = cartDAO.findByUserId(user.getId());
        if (cart == null) {
            throw new RuntimeException("Giỏ hàng trống.");
        }

        List<CheckoutLine> lines = new ArrayList<>();
        for (String rawId : cartItemIds) {
            int cartItemId;
            try {
                cartItemId = Integer.parseInt(rawId.trim());
            } catch (NumberFormatException e) {
                continue;
            }
            CartItem item = cartItemDAO.findById(cartItemId);
            if (item == null || item.getCart_id() != cart.getId()) {
                throw new RuntimeException("Mục giỏ hàng không hợp lệ.");
            }

            String qtyParam = request.getParameter("qty_" + cartItemId);
            int buyQty = item.getQuantity();
            if (qtyParam != null && !qtyParam.isBlank()) {
                try {
                    buyQty = Integer.parseInt(qtyParam.trim());
                } catch (NumberFormatException ignored) {
                    buyQty = item.getQuantity();
                }
            }
            if (buyQty < 1) {
                throw new RuntimeException("Số lượng phải lớn hơn 0.");
            }
            if (buyQty > item.getQuantity()) {
                throw new RuntimeException("Số lượng mua không được vượt quá số trong giỏ: "
                        + item.getBook().getTitle());
            }

            Book book = bookDAO.findActiveById(item.getBook().getId());
            if (book == null) {
                throw new RuntimeException("Sách không còn bán: " + item.getBook().getTitle());
            }
            if (buyQty > book.getStock()) {
                throw new RuntimeException("Không đủ hàng: " + book.getTitle());
            }

            CheckoutLine line = new CheckoutLine();
            line.setCartItemId(item.getId());
            line.setBookId(book.getId());
            line.setQuantity(buyQty);
            line.setBook(book);
            lines.add(line);
        }

        if (lines.isEmpty()) {
            throw new RuntimeException("Vui lòng chọn ít nhất một sách để thanh toán.");
        }
        return lines;
    }

    public List<Order> findByUserId(int userId) throws SQLException, ClassNotFoundException {
        return orderDAO.findByUserId(userId);
    }

    public Order findOrderDetail(int orderId, int userId) throws SQLException, ClassNotFoundException {
        return attachOrderItems(orderDAO.findByIdAndUserId(orderId, userId), orderId);
    }

    public List<Order> findAllForAdmin() throws SQLException, ClassNotFoundException {
        return orderDAO.findAll();
    }

    public Order findOrderDetailForAdmin(int orderId) throws SQLException, ClassNotFoundException {
        return attachOrderItems(orderDAO.findById(orderId), orderId);
    }

    public void updateOrderStatus(int orderId, String currentStatus, String newStatus)
            throws SQLException, ClassNotFoundException {
        if (!isValidTransition(currentStatus, newStatus)) {
            throw new IllegalArgumentException("Khong the chuyen tu " + currentStatus + " sang " + newStatus);
        }
        boolean updated = orderDAO.updateStatus(orderId, newStatus);
        if (!updated) {
            throw new RuntimeException("Cap nhat trang thai that bai");
        }
    }

    private boolean isValidTransition(String from, String to) {
        return switch (from) {
            case "PENDING" -> "CONFIRMED".equals(to) || "CANCELLED".equals(to);
            case "CONFIRMED" -> "SHIPPED".equals(to) || "CANCELLED".equals(to);
            case "SHIPPED" -> "COMPLETED".equals(to);
            default -> false;
        };
    }

    private Order attachOrderItems(Order order, int orderId) throws SQLException, ClassNotFoundException {
        if (order == null) {
            return null;
        }
        order.setItems(orderItemDAO.findByOrderId(orderId));
        return order;
    }

    private List<CheckoutLine> resolveAndValidate(User user, List<CheckoutLine> lines)
            throws SQLException, ClassNotFoundException {
        if (lines == null || lines.isEmpty()) {
            throw new RuntimeException("Không có sản phẩm để thanh toán.");
        }

        Cart cart = cartDAO.findByUserId(user.getId());
        List<CheckoutLine> resolved = new ArrayList<>();

        for (CheckoutLine line : lines) {
            int quantity = line.getQuantity();
            if (quantity < 1) {
                throw new RuntimeException("Số lượng không hợp lệ.");
            }

            if (line.getCartItemId() != null) {
                CartItem item = cartItemDAO.findById(line.getCartItemId());
                if (item == null || cart == null || item.getCart_id() != cart.getId()) {
                    throw new RuntimeException("Mục giỏ hàng không hợp lệ.");
                }
                if (quantity > item.getQuantity()) {
                    throw new RuntimeException("Số lượng vượt quá giỏ hàng: " + item.getBook().getTitle());
                }
            }

            Book book = bookDAO.findActiveById(line.getBookId());
            if (book == null) {
                throw new RuntimeException("Sách không tồn tại hoặc đã ngừng bán.");
            }
            if (quantity > book.getStock()) {
                throw new RuntimeException("Không đủ hàng: " + book.getTitle());
            }

            CheckoutLine fresh = new CheckoutLine();
            fresh.setCartItemId(line.getCartItemId());
            fresh.setBookId(book.getId());
            fresh.setQuantity(quantity);
            fresh.setBook(book);
            resolved.add(fresh);
        }
        return resolved;
    }

    private Order createOrder(Connection conn, User user, String receiverName, String phone,
                              String address, List<CheckoutLine> lines) throws SQLException {
        double total = 0;
        for (CheckoutLine line : lines) {
            total += line.getBook().getPrice() * line.getQuantity();
        }

        Order order = new Order();
        order.setUser(user);
        order.setReceiverName(receiverName);
        order.setPhone(phone);
        order.setAddress(address);
        order.setStatus("PENDING");
        order.setTotalAmount(total);

        int orderId = orderDAO.insert(conn, order);
        order.setId(orderId);
        return order;
    }

    private void createOrderItems(Connection conn, Order order, List<CheckoutLine> lines)
            throws SQLException {
        for (CheckoutLine line : lines) {
            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setBook(line.getBook());
            item.setBookTitle(line.getBook().getTitle());
            item.setBookPrice(line.getBook().getPrice());
            item.setQuantity(line.getQuantity());
            item.setSubtotal(line.getQuantity() * line.getBook().getPrice());
            orderItemDAO.insert(conn, item);
        }
    }

    private void updateStock(Connection conn, List<CheckoutLine> lines) throws SQLException {
        for (CheckoutLine line : lines) {
            int remain = line.getBook().getStock() - line.getQuantity();
            bookDAO.updateStock(conn, line.getBook().getId(), remain);
        }
    }

    private void applyCartChanges(Connection conn, User user, List<CheckoutLine> lines)
            throws SQLException, ClassNotFoundException {
        Cart cart = cartDAO.findByUserId(user.getId());
        if (cart == null) {
            return;
        }
        for (CheckoutLine line : lines) {
            if (line.getCartItemId() == null) {
                continue;
            }
            CartItem item = cartItemDAO.findById(conn, line.getCartItemId());
            if (item == null || item.getCart_id() != cart.getId()) {
                continue;
            }
            int remaining = item.getQuantity() - line.getQuantity();
            if (remaining <= 0) {
                cartItemDAO.delete(conn, item.getId());
            } else {
                cartItemDAO.setQuantity(conn, item.getId(), remaining);
            }
        }
    }
}
