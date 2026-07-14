package com.bootcamp.bookstore.online_bookstore.service;

import com.bootcamp.bookstore.online_bookstore.dao.BookDAO;
import com.bootcamp.bookstore.online_bookstore.model.Book;
import com.bootcamp.bookstore.online_bookstore.model.BookSearchFilter;

import java.sql.SQLException;
import java.util.List;

public class BookService {

    private final BookDAO dao = new BookDAO();

    public List<Book> findAll() throws SQLException, ClassNotFoundException {
        return dao.findAll();
    }

    public List<Book> findAllForAdmin() throws SQLException, ClassNotFoundException {
        return dao.findAllForAdmin();
    }

    public Book findById(int id) throws SQLException, ClassNotFoundException {
        return dao.findById(id);
    }

    public Book findActiveById(int id) throws SQLException, ClassNotFoundException {
        return dao.findActiveById(id);
    }

    public List<Book> findByName(String keyword) throws SQLException, ClassNotFoundException {
        return dao.findByName(keyword);
    }

    public List<Book> findByCategory(int id) throws SQLException, ClassNotFoundException {
        return dao.findByCategory(id);
    }

    public List<Book> findByAuthor(int id) throws SQLException, ClassNotFoundException {
        return dao.findByAuthor(id);
    }

    public List<Book> findByPublisher(int id) throws SQLException, ClassNotFoundException {
        return dao.findByPublisher(id);
    }

    public List<Book> search(BookSearchFilter filter) throws SQLException, ClassNotFoundException {
        return dao.search(filter, true);
    }

    public List<Book> searchForAdmin(BookSearchFilter filter) throws SQLException, ClassNotFoundException {
        return dao.search(filter, false);
    }

    public void add(Book book) throws SQLException, ClassNotFoundException {
        dao.add(book);
    }

    public void update(Book book) throws SQLException, ClassNotFoundException {
        dao.update(book);
    }

    public void delete(int id) throws SQLException, ClassNotFoundException {
        dao.delete(id);
    }
}
