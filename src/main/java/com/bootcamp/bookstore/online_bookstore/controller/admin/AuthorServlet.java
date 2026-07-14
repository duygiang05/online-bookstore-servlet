package com.bootcamp.bookstore.online_bookstore.controller.admin;

import com.bootcamp.bookstore.online_bookstore.model.Author;
import com.bootcamp.bookstore.online_bookstore.service.AuthorService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/admin/author")
public class AuthorServlet extends HttpServlet {

    private AuthorService service = new AuthorService();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if(action != null) {
            switch (action) {
                //get add
                case "add" :
                    request.getRequestDispatcher("/author-form.jsp")
                            .forward(request, response);
                    return;
                    //get edit
                case "edit" :
                    int author_id = Integer.parseInt(request.getParameter("id"));
                    try {
                        Author aut = service.findById(author_id);
                        request.setAttribute("aut",aut);
                        request.getRequestDispatcher("/author-form.jsp")
                                .forward(request,response);
                        return;
                    } catch (SQLException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
            }
        }

        //get all author
        List<Author> list = null;
        try {
            list = service.findAll();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        request.setAttribute("authors",list);
        request.getRequestDispatcher("/author-list.jsp")
                .forward(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws IOException {
        Author author = null;

        String author_name = request.getParameter("author_name");
        String id = request.getParameter("author_id");
        String action = request.getParameter("action");
        // add or edit
        if(action == null ) {
            String birthStr = request.getParameter("author_birth");
            if (author_name == null || author_name.isBlank()
                    || birthStr == null || birthStr.isBlank()) {
                response.sendRedirect(request.getContextPath() + "/admin/author?action=add");
                return;
            }
            LocalDate birth;
            try {
                birth = LocalDate.parse(birthStr);
            } catch (Exception e) {
                response.sendRedirect(request.getContextPath() + "/admin/author?action=add");
                return;
            }
            String bio = request.getParameter("author_bio");
            //add
            if(id == null || id.isBlank()) {
                try {

                    service.add(author_name,birth,bio);
                } catch (SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
            //edit
            else {
                try {

                    author = new Author(Integer.parseInt(id),author_name,birth,bio);
                    service.update(author);
                } catch (SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        //delete
        else {
            try {
                service.delete(Integer.parseInt(id));
            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        response.sendRedirect(request.getContextPath() + "/admin/author");
    }
}
