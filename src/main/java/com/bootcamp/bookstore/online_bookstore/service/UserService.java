package com.bootcamp.bookstore.online_bookstore.service;

import com.bootcamp.bookstore.online_bookstore.dao.UserDAO;
import com.bootcamp.bookstore.online_bookstore.model.User;
import com.bootcamp.bookstore.online_bookstore.util.UserRole;

import java.sql.SQLException;
import java.util.List;

public class UserService {
    private final UserDAO dao = new UserDAO();

    public User login(String username, String password) throws SQLException, ClassNotFoundException {
        User user = dao.findByUsername(username);

        if (user == null) return null;
        if (!password.equals(user.getPassword())) return null;

        return user;
    }

    public User findById(int id) throws SQLException, ClassNotFoundException {
        return dao.findById(id);
    }

    public List<User> findAll() throws SQLException, ClassNotFoundException {
        return dao.findAll();
    }

    /** Đăng ký tài khoản khách — luôn gán role USER. */
    public String register(String username, String password, String fullName, String email, String phone)
            throws SQLException, ClassNotFoundException {
        String error = validateRequired(username, password, fullName, email);
        if (error != null) return error;

        username = username.trim();
        if (dao.existsByUsername(username)) {
            return "Tên đăng nhập đã tồn tại.";
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setFull_name(fullName.trim());
        user.setEmail(email.trim());
        user.setPhone(phone == null ? "" : phone.trim());
        user.setRole(UserRole.USER);
        dao.insert(user);
        return null;
    }

    /** Admin thêm user hoặc admin. Trả về lỗi (nếu có). */
    public String add(String username, String password, String fullName, String email, String phone, String role)
            throws SQLException, ClassNotFoundException {
        String error = validateRequired(username, password, fullName, email);
        if (error != null) return error;

        role = normalizeRole(role);
        username = username.trim();
        if (dao.existsByUsername(username)) {
            return "Tên đăng nhập đã tồn tại.";
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setFull_name(fullName.trim());
        user.setEmail(email.trim());
        user.setPhone(phone == null ? "" : phone.trim());
        user.setRole(role);
        dao.insert(user);
        return null;
    }

    /**
     * Admin cập nhật tài khoản. Để trống mật khẩu = giữ mật khẩu cũ.
     * Trả về lỗi (nếu có).
     */
    public String update(int id, String username, String password, String fullName,
                         String email, String phone, String role)
            throws SQLException, ClassNotFoundException {
        User existing = dao.findById(id);
        if (existing == null) {
            return "Không tìm thấy tài khoản.";
        }

        if (username == null || username.isBlank() || fullName == null || fullName.isBlank()
                || email == null || email.isBlank()) {
            return "Vui lòng nhập đủ tên đăng nhập, họ tên và email.";
        }

        username = username.trim();
        if (dao.existsByUsernameExceptId(username, id)) {
            return "Tên đăng nhập đã tồn tại.";
        }

        existing.setUsername(username);
        if (password != null && !password.isBlank()) {
            existing.setPassword(password);
        }
        existing.setFull_name(fullName.trim());
        existing.setEmail(email.trim());
        existing.setPhone(phone == null ? "" : phone.trim());
        existing.setRole(normalizeRole(role));
        dao.update(existing);
        return null;
    }

    public String delete(int id, int currentUserId) throws SQLException, ClassNotFoundException {
        if (id == currentUserId) {
            return "Bạn không thể xóa tài khoản đang đăng nhập.";
        }
        if (dao.findById(id) == null) {
            return "Không tìm thấy tài khoản.";
        }
        try {
            dao.delete(id);
            return null;
        } catch (SQLException e) {
            return "Không thể xóa tài khoản (có thể còn đơn hàng hoặc giỏ hàng liên quan).";
        }
    }

    private String validateRequired(String username, String password, String fullName, String email) {
        if (username == null || username.isBlank()
                || password == null || password.isBlank()
                || fullName == null || fullName.isBlank()
                || email == null || email.isBlank()) {
            return "Vui lòng nhập đủ tên đăng nhập, mật khẩu, họ tên và email.";
        }
        return null;
    }

    private String normalizeRole(String role) {
        if (UserRole.ADMIN.equalsIgnoreCase(role)) {
            return UserRole.ADMIN;
        }
        return UserRole.USER;
    }
}
