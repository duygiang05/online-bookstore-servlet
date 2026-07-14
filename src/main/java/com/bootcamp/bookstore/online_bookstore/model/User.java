package com.bootcamp.bookstore.online_bookstore.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.bootcamp.bookstore.online_bookstore.util.UserRole;

import java.security.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private int id;
    private String username;
    private String password;
    private String full_name;
    private String email;
    private String phone;
    private String role;
    private Timestamp created_at;

    public boolean isAdmin() {
        return UserRole.isAdmin(role);
    }
}
