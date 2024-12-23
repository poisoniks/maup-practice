package com.maup.practice.repository;

import com.maup.practice.model.BrandModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends JpaRepository<BrandModel, Long> {
    BrandModel findByName(String name);
}
