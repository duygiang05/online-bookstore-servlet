package com.bootcamp.bookstore.online_bookstore.dao;

import com.bootcamp.bookstore.online_bookstore.model.Author;
import com.bootcamp.bookstore.online_bookstore.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AuthorDAO {

    public List<Author> findAll() throws SQLException, ClassNotFoundException {
        List<Author> list = new ArrayList<>();
        String sql = """
                SELECT *
                FROM authors
                """;
        try(
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
        )
        {
            while(rs.next()) {
                list.add((mapToAuthor(rs)));
            }
        }
        return list;
    }

    public Author findById(int author_id) throws SQLException, ClassNotFoundException {
        String sql = """
                SELECT *
                FROM authors
                WHERE id = ?
                """;
        try(
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
        )
        {
            ps.setInt(1,author_id);
            try(ResultSet rs = ps.executeQuery()) {
                if(rs.next()) return mapToAuthor(rs);
            }
            return null;
        }
    }

    public void add(String name, LocalDate birth, String bio)
            throws SQLException, ClassNotFoundException {

        String sql = """
            INSERT INTO authors(name, birth, bio)
            VALUES (?, ?, ?)
            """;

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setString(1, name);

            ps.setDate(2, java.sql.Date.valueOf(birth));

            ps.setString(3, bio);

            ps.executeUpdate();
        }
    }

    public void update(Author author) throws SQLException, ClassNotFoundException {
        String sql = """
                UPDATE authors
                SET name = ?, birth = ?, bio = ?
                WHERE id = ?
                """;
        try(
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
        )
        {
            ps.setString(1, author.getName());

            ps.setDate(2, java.sql.Date.valueOf(author.getBirth()));

            ps.setString(3, author.getBio());

            ps.setInt(4, author.getId());

            ps.executeUpdate();
        }
    }

    public void delete(int author_id) throws SQLException, ClassNotFoundException {
        String sql = """
                DELETE FROM authors
                WHERE id = ?
                """;
        try(
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
        )
        {
            ps.setInt(1,author_id);
            ps.executeUpdate();
        }
    }

    private Author mapToAuthor(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        LocalDate birth = rs.getDate("birth").toLocalDate();
        String bio = rs.getString("bio");

        return new Author(id,name,birth,bio);
    }
}
