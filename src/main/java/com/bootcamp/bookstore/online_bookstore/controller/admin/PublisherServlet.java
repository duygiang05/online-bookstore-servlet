package com.bootcamp.bookstore.online_bookstore.controller.admin;

import com.bootcamp.bookstore.online_bookstore.model.Publisher;
import com.bootcamp.bookstore.online_bookstore.service.PublisherService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/admin/publisher")
public class PublisherServlet extends HttpServlet {

    private PublisherService service = new PublisherService();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if(action != null) {
            switch (action) {
                case "add" :
                    request.getRequestDispatcher("/publisher-form.jsp")
                            .forward(request, response);
                    return;
                case "edit" :
                    int publisher_id = Integer.parseInt(request.getParameter("id"));
                    try {
                        Publisher pub = service.findById(publisher_id);
                        request.setAttribute("pub",pub);
                        request.getRequestDispatcher("/publisher-form.jsp")
                                .forward(request,response);
                        return;
                    } catch (SQLException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
            }
        }

        List<Publisher> list = null;
        try {
            list = service.findAll();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        request.setAttribute("publishers",list);
        request.getRequestDispatcher("/publisher-list.jsp")
                .forward(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws IOException {
        String publisher_name = request.getParameter("publisher_name");
        String id = request.getParameter("publisher_id");
        String action = request.getParameter("action");
        //add or edit
        if(action == null ) {
            if (publisher_name == null || publisher_name.isBlank()) {
                response.sendRedirect(request.getContextPath() + "/admin/publisher?action=add");
                return;
            }
            //add
            if(id == null || id.isBlank()) {
                try {
                    service.add((publisher_name));
                } catch (SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
            //edit
            else {
                try {
                    service.update(Integer.parseInt(id),publisher_name);
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

        response.sendRedirect(request.getContextPath() + "/admin/publisher");
    }
}
