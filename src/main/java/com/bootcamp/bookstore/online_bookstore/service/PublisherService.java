package com.bootcamp.bookstore.online_bookstore.service;

import com.bootcamp.bookstore.online_bookstore.dao.PublisherDAO;
import com.bootcamp.bookstore.online_bookstore.model.Book;
import com.bootcamp.bookstore.online_bookstore.model.Publisher;

import java.sql.SQLException;
import java.util.List;

public class PublisherService {
    private final PublisherDAO dao = new PublisherDAO();
    private final BookService bookService = new BookService();

    public List<Publisher> findAll() throws SQLException, ClassNotFoundException {
        return dao.findAll();
    }

    public void add(String name) throws SQLException, ClassNotFoundException {
        dao.add(name);
    }

    public Publisher findById(int publisher_id) throws SQLException, ClassNotFoundException {
        return dao.findById(publisher_id);
    }

    public void update(int publisher_id, String name) throws SQLException, ClassNotFoundException {
        dao.update(publisher_id, name);
    }

    public boolean delete(int publisher_id) throws SQLException, ClassNotFoundException {
        List<Book> list = bookService.findByPublisher(publisher_id);
        if (!list.isEmpty())
            return false;
        dao.delete(publisher_id);
        return true;
    }
}
