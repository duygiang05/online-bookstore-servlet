package com.bootcamp.bookstore.online_bookstore.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

    private int id;

    private Order order;

    private Book book;

    private String bookTitle;

    private double bookPrice;

    private int quantity;

    private double subtotal;
}