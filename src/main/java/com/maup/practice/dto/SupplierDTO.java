package com.maup.practice.dto;

import java.util.Objects;

public class SupplierDTO {

    private Long id;
    private String name;
    private String address;
    private String contactInfo;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SupplierDTO that = (SupplierDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(address, that.address) && Objects.equals(contactInfo, that.contactInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, address, contactInfo);
    }
}
