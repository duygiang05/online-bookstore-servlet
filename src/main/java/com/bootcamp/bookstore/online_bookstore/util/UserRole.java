package com.bootcamp.bookstore.online_bookstore.util;

public final class UserRole {

    public static final String ADMIN = "ADMIN";
    public static final String USER = "USER";

    private UserRole() {
    }

    public static boolean isAdmin(String role) {
        return ADMIN.equals(role);
    }
}
