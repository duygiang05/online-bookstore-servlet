package com.bootcamp.bookstore.online_bookstore.model;

import lombok.Data;

@Data
public class BookSearchFilter {

    private String title;
    private Integer authorId;
    private Integer categoryId;
    private Integer publisherId;
    private Integer publishedYear;
    private Double priceMin;
    private Double priceMax;

    public boolean hasCriteria() {
        return isNotBlank(title)
                || authorId != null
                || categoryId != null
                || publisherId != null
                || publishedYear != null
                || priceMin != null
                || priceMax != null;
    }

    private boolean isNotBlank(String value) {
        return value != null && !value.isBlank();
    }
}
