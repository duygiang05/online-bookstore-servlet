package com.bootcamp.bookstore.online_bookstore.dao;



import com.bootcamp.bookstore.online_bookstore.model.*;

import com.bootcamp.bookstore.online_bookstore.util.DBConnection;



import java.sql.Connection;

import java.sql.PreparedStatement;

import java.sql.ResultSet;

import java.sql.SQLException;

import java.util.ArrayList;

import java.util.List;



public class CartItemDAO {



    private static final String SELECT_CART_ITEM = """

            SELECT b.title, b.price, b.stock, b.cover_image, b.description, b.published_year,

                   a.name AS author_name,

                   c.name AS category_name,

                   p.name AS publisher_name,

                   ci.id AS cart_item_id, ci.cart_id, ci.quantity, ci.book_id

            FROM books b

            JOIN authors a ON b.author_id = a.id

            JOIN categories c ON b.category_id = c.id

            JOIN publishers p ON b.publisher_id = p.id

            JOIN cart_items ci ON ci.book_id = b.id

            """;



    @FunctionalInterface

    private interface ParamSetter {

        void set(PreparedStatement ps) throws SQLException;

    }



    public List<CartItem> findByCartId(int id) throws SQLException, ClassNotFoundException {

        return queryCartItems(SELECT_CART_ITEM + " WHERE ci.cart_id = ?", ps -> ps.setInt(1, id));

    }



    public CartItem findByCartIdAndBookId(int cartId, int bookId) throws SQLException, ClassNotFoundException {

        List<CartItem> list = queryCartItems(

                SELECT_CART_ITEM + " WHERE ci.cart_id = ? AND b.id = ?",

                ps -> {

                    ps.setInt(1, cartId);

                    ps.setInt(2, bookId);

                });

        return list.isEmpty() ? null : list.get(0);

    }



    public void insert(int cart_id, int book_id, int quantity) throws SQLException, ClassNotFoundException {

        String sql = """

                INSERT INTO cart_items (cart_id, book_id, quantity)

                VALUES (?, ?, ?)

                """;

        try (Connection conn = DBConnection.getConnection();

             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, cart_id);

            ps.setInt(2, book_id);

            ps.setInt(3, quantity);

            ps.executeUpdate();

        }

    }



    public void updateQuantity(int cart_item_id, int quantity) throws SQLException, ClassNotFoundException {

        String sql = """

                UPDATE cart_items

                SET quantity = quantity + ?

                WHERE id = ?

                """;

        try (Connection conn = DBConnection.getConnection();

             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, quantity);

            ps.setInt(2, cart_item_id);

            ps.executeUpdate();

        }

    }



    public void deleteByCartId(Connection conn, int cartId) throws SQLException {

        String sql = "DELETE FROM cart_items WHERE cart_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, cartId);

            ps.executeUpdate();

        }

    }



    public void delete(int cart_item_id) throws SQLException, ClassNotFoundException {
        try (Connection conn = DBConnection.getConnection()) {
            delete(conn, cart_item_id);
        }
    }

    public void delete(Connection conn, int cartItemId) throws SQLException {
        String sql = "DELETE FROM cart_items WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cartItemId);
            ps.executeUpdate();
        }
    }

    public void setQuantity(Connection conn, int cartItemId, int quantity) throws SQLException {
        String sql = "UPDATE cart_items SET quantity = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setInt(2, cartItemId);
            ps.executeUpdate();
        }
    }

    public CartItem findById(int cartItemId) throws SQLException, ClassNotFoundException {
        try (Connection conn = DBConnection.getConnection()) {
            return findById(conn, cartItemId);
        }
    }

    public CartItem findById(Connection conn, int cartItemId) throws SQLException {
        String sql = SELECT_CART_ITEM + " WHERE ci.id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cartItemId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapToCartItem(rs);
                }
                return null;
            }
        }
    }

    private List<CartItem> queryCartItems(String sql, ParamSetter setter)

            throws SQLException, ClassNotFoundException {

        List<CartItem> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();

             PreparedStatement ps = conn.prepareStatement(sql)) {

            setter.set(ps);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    list.add(mapToCartItem(rs));

                }

            }

        }

        return list;

    }



    private CartItem mapToCartItem(ResultSet rs) throws SQLException {

        Author author = new Author();

        author.setName(rs.getString("author_name"));



        Category category = new Category();

        category.setName(rs.getString("category_name"));



        Publisher publisher = new Publisher();

        publisher.setName(rs.getString("publisher_name"));



        Book book = new Book();

        book.setId(rs.getInt("book_id"));

        book.setTitle(rs.getString("title"));

        book.setPrice(rs.getDouble("price"));

        book.setStock(rs.getInt("stock"));

        book.setAuthor(author);

        book.setCategory(category);

        book.setCoverImage(rs.getString("cover_image"));

        book.setPublisher(publisher);

        book.setDescription(rs.getString("description"));

        book.setPublishedYear(Integer.parseInt(rs.getString("published_year")));



        CartItem cartItem = new CartItem();

        cartItem.setId(rs.getInt("cart_item_id"));

        cartItem.setCart_id(rs.getInt("cart_id"));

        cartItem.setBook(book);

        cartItem.setQuantity(rs.getInt("quantity"));



        return cartItem;

    }

}


