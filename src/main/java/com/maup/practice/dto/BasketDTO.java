package com.maup.practice.dto;

import java.util.Objects;
import java.util.Set;

public class BasketDTO {
    private Long id;
    private Set<BasketItemDTO> items;

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BasketDTO basketDTO = (BasketDTO) o;
        return Objects.equals(id, basketDTO.id) && Objects.equals(items, basketDTO.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, items);
    }
}
