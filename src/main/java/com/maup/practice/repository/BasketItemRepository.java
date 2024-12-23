package com.maup.practice.repository;

import com.maup.practice.model.BasketItemModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BasketItemRepository extends JpaRepository<BasketItemModel, Long> {
}
