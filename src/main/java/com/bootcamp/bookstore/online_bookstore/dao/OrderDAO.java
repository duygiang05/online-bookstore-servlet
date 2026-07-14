package com.bootcamp.bookstore.online_bookstore.dao;

import com.bootcamp.bookstore.online_bookstore.model.Order;
import com.bootcamp.bookstore.online_bookstore.model.User;
import com.bootcamp.bookstore.online_bookstore.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    private static final String SELECT_ORDER = """
            SELECT id, receiver_name, phone, address, total_amount, status, created_at
            FROM orders
            """;

    private static final String SELECT_ORDER_WITH_USER = """
            SELECT o.id, o.receiver_name, o.phone, o.address, o.total_amount, o.status,
                   o.created_at, u.id AS user_id, u.full_name, u.email
            FROM orders o
            JOIN users u ON u.id = o.user_id
            """;

    @FunctionalInterface
    private interface ParamSetter {
        void set(PreparedStatement ps) throws SQLException;
    }

    public List<Order> findAll() throws SQLException, ClassNotFoundException {
        return queryList(SELECT_ORDER_WITH_USER + " ORDER BY o.created_at DESC", null, true);
    }

    public Order findById(int orderId) throws SQLException, ClassNotFoundException {
        return queryOne(SELECT_ORDER_WITH_USER + " WHERE o.id = ?", ps -> ps.setInt(1, orderId), true);
    }

    public List<Order> findByUserId(int userId) throws SQLException, ClassNotFoundException {
        return queryList(SELECT_ORDER + " WHERE user_id = ? ORDER BY created_at DESC",
                ps -> ps.setInt(1, userId), false);
    }

    public Order findByIdAndUserId(int orderId, int userId) throws SQLException, ClassNotFoundException {
        return queryOne(SELECT_ORDER + " WHERE id = ? AND user_id = ?",
                ps -> {
                    ps.setInt(1, orderId);
                    ps.setInt(2, userId);
                }, false);
    }

    public int insert(Connection conn, Order order) throws SQLException {
        String sql = """
                INSERT INTO orders
                (
                    user_id,
                    receiver_name,
                    phone,
                    address,
                    total_amount,
                    status
                )
                VALUES
                (
                    ?,?,?,?,?,?
                )
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, order.getUser().getId());
            ps.setString(2, order.getReceiverName());
            ps.setString(3, order.getPhone());
            ps.setString(4, order.getAddress());
            ps.setDouble(5, order.getTotalAmount());
            ps.setString(6, order.getStatus());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    public boolean updateStatus(int orderId, String newStatus) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE orders SET status = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setInt(2, orderId);
            return ps.executeUpdate() > 0;
        }
    }

    private List<Order> queryList(String sql, ParamSetter setter, boolean withUser)
            throws SQLException, ClassNotFoundException {
        List<Order> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (setter != null) {
                setter.set(ps);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(withUser ? mapToOrderWithUser(rs) : mapToOrder(rs));
                }
            }
        }
        return list;
    }

    private Order queryOne(String sql, ParamSetter setter, boolean withUser)
            throws SQLException, ClassNotFoundException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            setter.set(ps);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return withUser ? mapToOrderWithUser(rs) : mapToOrder(rs);
                }
            }
        }
        return null;
    }

    private Order mapToOrder(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setId(rs.getInt("id"));
        order.setReceiverName(rs.getString("receiver_name"));
        order.setPhone(rs.getString("phone"));
        order.setAddress(rs.getString("address"));
        order.setTotalAmount(rs.getDouble("total_amount"));
        order.setStatus(rs.getString("status"));
        order.setCreatedAt(rs.getTimestamp("created_at"));
        return order;
    }

    private Order mapToOrderWithUser(ResultSet rs) throws SQLException {
        Order order = mapToOrder(rs);

        User user = new User();
        user.setId(rs.getInt("user_id"));
        user.setFull_name(rs.getString("full_name"));
        user.setEmail(rs.getString("email"));
        order.setUser(user);

        return order;
    }
}

