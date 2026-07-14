package com.bootcamp.bookstore.online_bookstore.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    private int id;

    private User user;

    private String receiverName;

    private String phone;

    private String address;

    private double totalAmount;

    private String status;

    private Timestamp createdAt;

    private List<OrderItem> items;
}
