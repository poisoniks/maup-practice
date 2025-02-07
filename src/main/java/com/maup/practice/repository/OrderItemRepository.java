package com.maup.practice.repository;

import com.maup.practice.model.OrderItemModel;
import com.maup.practice.model.ProductModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItemModel, Long> {
    boolean existsByProduct(ProductModel id);
}
