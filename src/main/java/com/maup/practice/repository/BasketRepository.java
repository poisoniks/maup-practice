package com.maup.practice.repository;

import com.maup.practice.model.BasketModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BasketRepository extends JpaRepository<BasketModel, Long> {
    BasketModel findByUserId(Long userId);
}
