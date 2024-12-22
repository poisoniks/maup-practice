package com.maup.practice.dto;

import java.util.HashSet;
import java.util.Set;

public class BasketDTO {
    private Long id;
    private Set<BasketItemDTO> items = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<BasketItemDTO> getItems() {
        return items;
    }

    public void setItems(Set<BasketItemDTO> items) {
        this.items = items;
    }

}
