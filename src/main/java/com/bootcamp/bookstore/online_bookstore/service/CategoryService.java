package com.bootcamp.bookstore.online_bookstore.service;

import com.bootcamp.bookstore.online_bookstore.dao.CategoryDAO;
import com.bootcamp.bookstore.online_bookstore.model.Book;
import com.bootcamp.bookstore.online_bookstore.model.Category;

import java.sql.SQLException;
import java.util.List;

public class CategoryService {
    private final CategoryDAO dao = new CategoryDAO();
    private final BookService bookService = new BookService();

    public List<Category> findAll() throws SQLException, ClassNotFoundException {
        return dao.findAll();
    }

    public void add(String name) throws SQLException, ClassNotFoundException {
        dao.add(name);
    }

    public Category findById(int category_id) throws SQLException, ClassNotFoundException {
        return dao.findById(category_id);
    }

    public void update(int category_id, String name) throws SQLException, ClassNotFoundException {
        dao.update(category_id, name);
    }

    public boolean delete(int category_id) throws SQLException, ClassNotFoundException {
        List<Book> list = bookService.findByCategory(category_id);
        if (!list.isEmpty())
            return false;  
        dao.delete(category_id);
        return true;
    }
}
