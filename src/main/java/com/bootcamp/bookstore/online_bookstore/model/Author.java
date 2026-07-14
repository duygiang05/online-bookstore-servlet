package com.bootcamp.bookstore.online_bookstore.model;

import lombok.*;

import java.security.Timestamp;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Author {
    private int id;
    private String name;
    private LocalDate birth;
    private String bio;
}
