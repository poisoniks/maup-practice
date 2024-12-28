package com.maup.practice.dto;

import java.util.Objects;

public class ProductDTO {

    private Long id;
    private String name;
    private String description;
    private double price;
    private int stockQuantity;

    private CategoryDTO category;
    private SupplierDTO supplier;
    private BrandDTO brand;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public CategoryDTO getCategory() {
        return category;
    }

    public void setCategory(CategoryDTO category) {
        this.category = category;
    }

    public SupplierDTO getSupplier() {
        return supplier;
    }

    public void setSupplier(SupplierDTO supplier) {
        this.supplier = supplier;
    }

    public BrandDTO getBrand() {
        return brand;
    }

    public void setBrand(BrandDTO brand) {
        this.brand = brand;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ProductDTO that = (ProductDTO) o;
        return Double.compare(price, that.price) == 0 && stockQuantity == that.stockQuantity && Objects.equals(id, that.id)
                && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(category, that.category)
                && Objects.equals(supplier, that.supplier) && Objects.equals(brand, that.brand);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, price, stockQuantity, category, supplier, brand);
    }
}
