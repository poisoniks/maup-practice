package com.maup.practice.dto;

import java.util.Objects;

public class BrandDTO {

    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BrandDTO brandDTO = (BrandDTO) o;
        return Objects.equals(id, brandDTO.id) && Objects.equals(name, brandDTO.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
