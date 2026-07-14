package com.bootcamp.bookstore.online_bookstore.dao;

import com.bootcamp.bookstore.online_bookstore.model.Category;
import com.bootcamp.bookstore.online_bookstore.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {

    public List<Category> findAll() throws SQLException, ClassNotFoundException {
        List<Category> list = new ArrayList<>();
        String sql = """
                SELECT *
                FROM categories
                """;
        try(
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                )
        {
            while(rs.next()) {
                list.add((mapToCategory(rs)));
            }
        }
        return list;
    }

    public Category findById(int category_id) throws SQLException, ClassNotFoundException {
        String sql = """
                SELECT *
                FROM categories
                WHERE id = ?
                """;
        try(
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                )
        {
            ps.setInt(1,category_id);
            try(ResultSet rs = ps.executeQuery()) {
                if(rs.next()) return mapToCategory(rs);
            }
            return null;
        }
    }

    public void add(String name) throws SQLException, ClassNotFoundException {
        String sql = """
                INSERT INTO categories (name)
                VALUES (?)
                """;
        try(
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                )
        {
            ps.setString(1,name);
            ps.executeUpdate();
        }
    }

    public void update(int category_id,String name) throws SQLException, ClassNotFoundException {
        String sql = """
                UPDATE categories
                SET name = ?
                WHERE id = ?
                """;
        try(
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                )
        {
            ps.setInt(2,category_id);
            ps.setString(1,name);
            ps.executeUpdate();
        }
    }

    public void delete(int category_id) throws SQLException, ClassNotFoundException {
        String sql = """
                DELETE FROM categories
                WHERE id = ?
                """;
        try(
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                )
        {
            ps.setInt(1,category_id);
            ps.executeUpdate();
        }
    }

    private Category mapToCategory(ResultSet rs) throws SQLException {
        return new Category(rs.getInt("id"),rs.getString("name"));
    }
}
