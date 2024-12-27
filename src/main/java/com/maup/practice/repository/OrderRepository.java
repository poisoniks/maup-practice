package com.maup.practice.repository;

import com.maup.practice.model.OrderModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<OrderModel, Long> {
    @Query("SELECT o FROM OrderModel o " +
            "WHERE (:userId IS NULL OR o.user.id = :userId)")
    Page<OrderModel> findAllByFilters(Long userId, Pageable pageable);
}
