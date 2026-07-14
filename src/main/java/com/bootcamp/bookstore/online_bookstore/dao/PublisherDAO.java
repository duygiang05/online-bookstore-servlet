package com.bootcamp.bookstore.online_bookstore.dao;

import com.bootcamp.bookstore.online_bookstore.model.Publisher;
import com.bootcamp.bookstore.online_bookstore.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PublisherDAO {

    public List<Publisher> findAll() throws SQLException, ClassNotFoundException {
        List<Publisher> list = new ArrayList<>();
        String sql = """
                SELECT *
                FROM publishers
                """;
        try(
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
        )
        {
            while(rs.next()) {
                list.add((mapToPublisher(rs)));
            }
        }
        return list;
    }

    public Publisher findById(int publisher_id) throws SQLException, ClassNotFoundException {
        String sql = """
                SELECT *
                FROM publishers
                WHERE id = ?
                """;
        try(
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
        )
        {
            ps.setInt(1,publisher_id);
            try(ResultSet rs = ps.executeQuery()) {
                if(rs.next()) return mapToPublisher(rs);
            }
            return null;
        }
    }

    public void add(String name) throws SQLException, ClassNotFoundException {
        String sql = """
                INSERT INTO publishers (name)
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

    public void update(int publisher_id,String name) throws SQLException, ClassNotFoundException {
        String sql = """
                UPDATE publishers
                SET name = ?
                WHERE id = ?
                """;
        try(
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
        )
        {
            ps.setInt(2,publisher_id);
            ps.setString(1,name);
            ps.executeUpdate();
        }
    }

    public void delete(int publisher_id) throws SQLException, ClassNotFoundException {
        String sql = """
                DELETE FROM publishers
                WHERE id = ?
                """;
        try(
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
        )
        {
            ps.setInt(1,publisher_id);
            ps.executeUpdate();
        }
    }

    private Publisher mapToPublisher(ResultSet rs) throws SQLException {
        return new Publisher(rs.getInt("id"),rs.getString("name"));
    }
}
