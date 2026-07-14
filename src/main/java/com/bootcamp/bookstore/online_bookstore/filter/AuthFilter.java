package com.bootcamp.bookstore.online_bookstore.filter;

import com.bootcamp.bookstore.online_bookstore.model.User;
import com.bootcamp.bookstore.online_bookstore.util.CheckoutSession;
import com.bootcamp.bookstore.online_bookstore.util.PendingCartAdd;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@WebFilter("/*")
public class AuthFilter implements Filter {

    private boolean isPublicPath(String path) {
        return path.equals("/login")
                || path.equals("/register")
                || path.equals("/home")
                || path.equals("/book")
                || path.equals("/logout")
                || path.equals("/access-denied")
                || path.equals("/")
                || path.equals("/index.jsp")
                || path.startsWith("/css/")
                || path.startsWith("/js/")
                || path.startsWith("/images/");
    }

    private boolean isAdminPath(String path) {
        return path.startsWith("/admin");
    }

    private boolean isUserOnlyPath(String path) {
        return path.equals("/cart")
                || path.equals("/cart/add")
                || path.equals("/checkout")
                || path.equals("/order")
                || path.equals("/user");
    }

    private void redirectByRole(HttpServletResponse resp, String contextPath, User user)
            throws IOException {
        if (user.isAdmin()) {
            resp.sendRedirect(contextPath + "/admin/dashboard");
        } else {
            resp.sendRedirect(contextPath + "/home");
        }
    }

    private void redirectToLogin(HttpServletRequest req, HttpServletResponse resp,
                                 String contextPath, String path) throws IOException {
        if ("POST".equalsIgnoreCase(req.getMethod()) && "/cart/add".equals(path)) {
            PendingCartAdd.save(req.getSession(true), req.getParameter("book_id"),
                    req.getParameter("quantity"));
            resp.sendRedirect(contextPath + "/login?redirect="
                    + URLEncoder.encode("/cart", StandardCharsets.UTF_8));
            return;
        }

        if ("POST".equalsIgnoreCase(req.getMethod()) && "/checkout".equals(path)
                && "prepareBuyNow".equals(req.getParameter("action"))) {
            CheckoutSession.savePendingBuyNow(req.getSession(true),
                    req.getParameter("book_id"), req.getParameter("quantity"));
            resp.sendRedirect(contextPath + "/login?redirect="
                    + URLEncoder.encode("/checkout", StandardCharsets.UTF_8));
            return;
        }

        String redirect = path;
        if ("/cart/add".equals(path)) {
            redirect = "/cart";
        }
        resp.sendRedirect(contextPath + "/login?redirect="
                + URLEncoder.encode(redirect, StandardCharsets.UTF_8));
    }

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String uri = req.getRequestURI();
        String contextPath = req.getContextPath();
        String path = uri.substring(contextPath.length());

        HttpSession session = req.getSession(false);
        User user = session != null ? (User) session.getAttribute("user") : null;
        boolean loggedIn = user != null;

        if ((path.equals("/login") || path.equals("/register")) && loggedIn) {
            redirectByRole(resp, contextPath, user);
            return;
        }

        if (isPublicPath(path)) {
            chain.doFilter(req, resp);
            return;
        }

        if (!loggedIn) {
            redirectToLogin(req, resp, contextPath, path);
            return;
        }

        if (isAdminPath(path)) {
            if (!user.isAdmin()) {
                resp.sendRedirect(contextPath + "/access-denied");
                return;
            }
            chain.doFilter(req, resp);
            return;
        }

        if (isUserOnlyPath(path) && user.isAdmin()) {
            resp.sendRedirect(contextPath + "/admin/dashboard");
            return;
        }

        chain.doFilter(req, resp);
    }
}
