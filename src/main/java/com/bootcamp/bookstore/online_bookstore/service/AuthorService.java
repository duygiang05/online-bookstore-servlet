package com.bootcamp.bookstore.online_bookstore.service;

import com.bootcamp.bookstore.online_bookstore.dao.AuthorDAO;
import com.bootcamp.bookstore.online_bookstore.model.Book;
import com.bootcamp.bookstore.online_bookstore.model.Author;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class AuthorService {
    private final AuthorDAO dao = new AuthorDAO();
    private final BookService bookService = new BookService();


    public List<Author> findAll() throws SQLException, ClassNotFoundException {
        return dao.findAll();
    }

    public void add(String name, LocalDate birth,String bio) throws SQLException, ClassNotFoundException {
        dao.add(name ,birth,bio);
    }

    public Author findById(int author_id) throws SQLException, ClassNotFoundException {
        return dao.findById(author_id);
    }

    public void update(Author author) throws SQLException, ClassNotFoundException {
        dao.update(author);
    }

    public boolean delete(int author_id) throws SQLException, ClassNotFoundException {
        List<Book> list = bookService.findByAuthor(author_id);
        if (!list.isEmpty())
            return false;
        dao.delete(author_id);
        return true;
    }
}

