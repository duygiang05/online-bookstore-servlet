package com.bootcamp.bookstore.online_bookstore.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    private int id;
    private User user;
    private Book book;
    private int rating;
    private String comment;
    private Timestamp created_at;
}
