package com.bootcamp.bookstore.online_bookstore.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Cart {
    private int id;
    private int user_id;
    private List<CartItem> items;

}
