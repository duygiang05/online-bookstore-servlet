package com.bootcamp.bookstore.online_bookstore.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    private int id;
    private String title;
    private String isbn;
    private double price;
    private int stock;
    private String description;
    private String coverImage;
    private int publishedYear;
    private Category category;
    private Author author;
    private Publisher publisher;
    private String status;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
