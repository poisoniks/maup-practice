package com.maup.practice.repository;

import com.maup.practice.model.SupplierModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierRepository extends JpaRepository<SupplierModel, Long> {
    SupplierModel findByName(String name);
}
