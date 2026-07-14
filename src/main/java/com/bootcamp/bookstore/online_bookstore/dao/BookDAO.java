package com.bootcamp.bookstore.online_bookstore.dao;

import com.bootcamp.bookstore.online_bookstore.model.Author;
import com.bootcamp.bookstore.online_bookstore.model.Book;
import com.bootcamp.bookstore.online_bookstore.model.BookSearchFilter;
import com.bootcamp.bookstore.online_bookstore.model.Category;
import com.bootcamp.bookstore.online_bookstore.model.Publisher;
import com.bootcamp.bookstore.online_bookstore.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {

    public static final String STATUS_ACTIVE = "ACTIVE";
    public static final String STATUS_INACTIVE = "INACTIVE";
    private static final String BASE_SELECT = """

            SELECT b.id, b.title, b.isbn, b.price, b.stock, b.cover_image, b.description, b.published_year, b.status,
            a.name AS author_name,
            c.name AS category_name,
            p.name AS publisher_name
            FROM books b
            JOIN authors a ON b.author_id = a.id
            JOIN categories c ON b.category_id = c.id
            JOIN publishers p ON b.publisher_id = p.id
            """;

    @FunctionalInterface
    private interface ParamSetter {
        void set(PreparedStatement ps) throws SQLException;
    }

    private List<Book> executeQuery(String whereClause, ParamSetter setter, boolean activeOnly)
            throws SQLException, ClassNotFoundException {
        StringBuilder sql = new StringBuilder(BASE_SELECT);

        if (!whereClause.isEmpty()) {
            sql.append(whereClause);
        }

        if (activeOnly) {
            sql.append(whereClause.isEmpty() ? " WHERE " : " AND ");
            sql.append("b.status = '").append(STATUS_ACTIVE).append("'");
        }

        List<Book> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            if (setter != null) {
                setter.set(ps);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapToBook(rs));
                }
            }
        }
        return list;
    }



    // lay tat ca sach dang ban (khach hang)
    public List<Book> findAll() throws SQLException, ClassNotFoundException {
        return executeQuery("", null, true);
    }

    // lay tat ca sach (admin, bao gom inactive)
    public List<Book> findAllForAdmin() throws SQLException, ClassNotFoundException {
        return executeQuery("", null, false);
    }

    public Book findById(int id) throws SQLException, ClassNotFoundException {
        List<Book> list = executeQuery(" WHERE b.id = ?", ps -> ps.setInt(1, id), false);
        return list.isEmpty() ? null : list.get(0);
    }

    public Book findActiveById(int id) throws SQLException, ClassNotFoundException {
        List<Book> list = executeQuery(" WHERE b.id = ?", ps -> ps.setInt(1, id), true);

        return list.isEmpty() ? null : list.get(0);
    }

    public List<Book> findByName(String keyword) throws SQLException, ClassNotFoundException {

        return executeQuery(" WHERE b.title LIKE ?",
                ps -> ps.setString(1, "%" + keyword + "%"), true);
    }

    public List<Book> findByCategory(int id) throws SQLException, ClassNotFoundException {

        return executeQuery(" WHERE c.id = ?", ps -> ps.setInt(1, id), true);
    }

    public List<Book> findByAuthor(int authorId) throws SQLException, ClassNotFoundException {

        return executeQuery(" WHERE a.id = ?", ps -> ps.setInt(1, authorId), true);
    }

    public List<Book> findByPublisher(int publisherId) throws SQLException, ClassNotFoundException {

        return executeQuery(" WHERE p.id = ?", ps -> ps.setInt(1, publisherId), true);
    }

    public List<Book> search(BookSearchFilter filter, boolean activeOnly)
            throws SQLException, ClassNotFoundException {
        StringBuilder sql = new StringBuilder(BASE_SELECT);
        StringBuilder where = new StringBuilder(" WHERE 1=1 ");

        boolean hasTitle = filter.getTitle() != null && !filter.getTitle().isBlank();
        if (hasTitle) {
            where.append(" AND b.title LIKE ? ");
        }
        if (filter.getAuthorId() != null) {
            where.append(" AND a.id = ? ");
        }
        if (filter.getCategoryId() != null) {
            where.append(" AND c.id = ? ");
        }
        if (filter.getPublisherId() != null) {
            where.append(" AND p.id = ? ");
        }
        if (filter.getPublishedYear() != null) {
            where.append(" AND b.published_year = ? ");
        }
        if (filter.getPriceMin() != null) {
            where.append(" AND b.price >= ? ");
        }
        if (filter.getPriceMax() != null) {
            where.append(" AND b.price <= ? ");
        }

        sql.append(where);

        if (activeOnly) {
            sql.append(" AND b.status = '").append(STATUS_ACTIVE).append("'");
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            int index = 1;
            if (hasTitle) {
                ps.setString(index++, "%" + filter.getTitle().trim() + "%");
            }
            if (filter.getAuthorId() != null) {
                ps.setInt(index++, filter.getAuthorId());
            }
            if (filter.getCategoryId() != null) {
                ps.setInt(index++, filter.getCategoryId());
            }
            if (filter.getPublisherId() != null) {
                ps.setInt(index++, filter.getPublisherId());
            }
            if (filter.getPublishedYear() != null) {
                ps.setInt(index++, filter.getPublishedYear());
            }
            if (filter.getPriceMin() != null) {
                ps.setDouble(index++, filter.getPriceMin());
            }
            if (filter.getPriceMax() != null) {
                ps.setDouble(index, filter.getPriceMax());
            }

            List<Book> list = new ArrayList<>();
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapToBook(rs));
                }
            }
            return list;
        }
    }

    public void add(Book book) throws SQLException, ClassNotFoundException {

        String sql = """
                INSERT INTO books
                (
                    title,
                    isbn,
                    price,
                    stock,
                    description,
                    published_year,
                    cover_image,
                    author_id,
                    category_id,
                    publisher_id,
                    status
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            bindBookParams(ps, book, 1);

            ps.setString(11, STATUS_ACTIVE);
            ps.executeUpdate();
        }
    }



    public void update(Book book) throws SQLException, ClassNotFoundException {

        String sql = """
                UPDATE books
                 SET
                 title=?,
                 isbn=?,
                 price=?,
                 stock=?,
                 description=?,
                 published_year=?,
                 cover_image=?,
                 author_id=?,
                 category_id=?,
                 publisher_id=?
                 WHERE id=?
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            bindBookParams(ps, book, 1);
            ps.setInt(11, book.getId());
            ps.executeUpdate();
        }
    }

    public void updateStock(Connection conn, int bookId, int stock) throws SQLException {
        String sql = "UPDATE books SET stock = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, stock);
            ps.setInt(2, bookId);
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException, ClassNotFoundException {
        String softDeleteSql = "UPDATE books SET status = ? WHERE id = ?";
        String removeCartSql = "DELETE FROM cart_items WHERE book_id = ?";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                try (PreparedStatement ps = conn.prepareStatement(softDeleteSql)) {
                    ps.setString(1, STATUS_INACTIVE);
                    ps.setInt(2, id);
                    ps.executeUpdate();
                }

                try (PreparedStatement ps = conn.prepareStatement(removeCartSql)) {
                    ps.setInt(1, id);
                    ps.executeUpdate();
                }
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    private void bindBookParams(PreparedStatement ps, Book book, int startIndex) throws SQLException {
        ps.setString(startIndex, book.getTitle());
        ps.setString(startIndex + 1, book.getIsbn());
        ps.setDouble(startIndex + 2, book.getPrice());
        ps.setInt(startIndex + 3, book.getStock());
        ps.setString(startIndex + 4, book.getDescription());
        ps.setInt(startIndex + 5, book.getPublishedYear());
        ps.setString(startIndex + 6, book.getCoverImage());
        ps.setInt(startIndex + 7, book.getAuthor().getId());
        ps.setInt(startIndex + 8, book.getCategory().getId());
        ps.setInt(startIndex + 9, book.getPublisher().getId());
    }

    private Book mapToBook(ResultSet rs) throws SQLException {
        Author author = new Author();
        author.setName(rs.getString("author_name"));

        Category category = new Category();
        category.setName(rs.getString("category_name"));

        Publisher publisher = new Publisher();
        publisher.setName(rs.getString("publisher_name"));

        Book book = new Book();
        book.setId(rs.getInt("id"));
        book.setTitle(rs.getString("title"));
        book.setIsbn(rs.getString("isbn"));
        book.setPrice(rs.getDouble("price"));
        book.setStock(rs.getInt("stock"));
        book.setAuthor(author);
        book.setCategory(category);
        book.setCoverImage(rs.getString("cover_image"));
        book.setPublisher(publisher);
        book.setDescription(rs.getString("description"));
        book.setPublishedYear(Integer.parseInt(rs.getString("published_year")));
        book.setStatus(rs.getString("status"));

        return book;
    }

}


