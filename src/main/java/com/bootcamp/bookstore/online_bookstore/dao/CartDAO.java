package com.bootcamp.bookstore.online_bookstore.dao;

import com.bootcamp.bookstore.online_bookstore.model.Cart;
import com.bootcamp.bookstore.online_bookstore.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CartDAO {
    public Cart findByUserId(int id) throws SQLException, ClassNotFoundException {
        String sql =
                "SELECT * FROM carts WHERE user_id = ?";
        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
        ) {
            ps.setInt(1,id);
            try(ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    return mapToCart(rs);
                }
                return null;
            }
        }
    }

    public int create (int id) throws SQLException, ClassNotFoundException {
        String sql = """
                INSERT INTO carts(user_id)
                VALUES (?)
                """;
        try(
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                )
        {
            ps.setInt(1,id);
            ps.executeUpdate();

            return findByUserId(id).getId();
        }
    }

    private Cart mapToCart(ResultSet rs) throws SQLException {
        Cart cart = new Cart();
        cart.setId(rs.getInt("id"));
        cart.setUser_id(rs.getInt("user_id"));

        return cart;
    }
}
