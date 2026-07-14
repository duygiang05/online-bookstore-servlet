package com.bootcamp.bookstore.online_bookstore.dao;

import com.bootcamp.bookstore.online_bookstore.model.Book;
import com.bootcamp.bookstore.online_bookstore.model.OrderItem;
import com.bootcamp.bookstore.online_bookstore.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderItemDAO {

    public void insert(Connection conn, OrderItem orderItem) throws SQLException {
        String sql = """
                INSERT INTO order_items
                (
                    order_id,
                    book_id,
                    book_title,
                    book_price,
                    quantity,
                    subtotal
                )
                VALUES
                (
                    ?,?,?,?,?,?
                )
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderItem.getOrder().getId());
            ps.setInt(2, orderItem.getBook().getId());
            ps.setString(3, orderItem.getBookTitle());
            ps.setDouble(4, orderItem.getBookPrice());
            ps.setInt(5, orderItem.getQuantity());
            ps.setDouble(6, orderItem.getSubtotal());

            ps.executeUpdate();
        }
    }

    public List<OrderItem> findByOrderId(int orderId) throws SQLException, ClassNotFoundException {
        String sql = """
                SELECT id, order_id, book_id, book_title, book_price, quantity, subtotal
                FROM order_items
                WHERE order_id = ?
                """;
        List<OrderItem> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapToOrderItem(rs));
                }
            }
        }
        return list;
    }

    private OrderItem mapToOrderItem(ResultSet rs) throws SQLException {
        Book book = new Book();
        book.setId(rs.getInt("book_id"));

        OrderItem item = new OrderItem();
        item.setId(rs.getInt("id"));
        item.setBook(book);
        item.setBookTitle(rs.getString("book_title"));
        item.setBookPrice(rs.getDouble("book_price"));
        item.setQuantity(rs.getInt("quantity"));
        item.setSubtotal(rs.getDouble("subtotal"));
        return item;
    }
}
