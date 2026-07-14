package com.bootcamp.bookstore.online_bookstore.controller.admin;

import com.bootcamp.bookstore.online_bookstore.model.Category;
import com.bootcamp.bookstore.online_bookstore.service.CategoryService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/admin/category")
public class CategoryServlet extends HttpServlet {

    private CategoryService service = new CategoryService();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if(action != null) {
            switch (action) {
                case "add" :
                    request.getRequestDispatcher("/category-form.jsp")
                            .forward(request, response);
                    return;
                case "edit" :
                    int category_id = Integer.parseInt(request.getParameter("id"));
                    try {
                        Category cate = service.findById(category_id);
                        request.setAttribute("cate",cate);
                        request.getRequestDispatcher("/category-form.jsp")
                                .forward(request,response);
                        return;
                    } catch (SQLException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
            }
        }

        List<Category> list = null;
        try {
            list = service.findAll();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        request.setAttribute("categories",list);
        request.getRequestDispatcher("/category-list.jsp")
                .forward(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws IOException {
        String category_name = request.getParameter("category_name");
        String id = request.getParameter("category_id");
        String action = request.getParameter("action");
        if(action == null ) {
            if (category_name == null || category_name.isBlank()) {
                response.sendRedirect(request.getContextPath() + "/admin/category?action=add");
                return;
            }
            if(id == null || id.isBlank()) {
                try {
                    service.add((category_name));
                } catch (SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
            else {
                try {
                    service.update(Integer.parseInt(id),category_name);
                } catch (SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        else {
            try {
                service.delete(Integer.parseInt(id));
            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        response.sendRedirect(request.getContextPath() + "/admin/category");
    }
}
