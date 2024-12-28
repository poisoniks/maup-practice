package com.maup.practice.repository;

import com.maup.practice.model.BasketItemModel;
import com.maup.practice.model.ProductModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BasketItemRepository extends JpaRepository<BasketItemModel, Long> {
    boolean existsByProduct(ProductModel productModel);
}
