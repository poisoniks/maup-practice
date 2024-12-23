package com.maup.practice.repository;

import com.maup.practice.model.ProductModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface ProductRepository extends JpaRepository<ProductModel, Long> {

    @Query("SELECT p FROM ProductModel p " +
            "WHERE (:minPrice IS NULL OR p.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR p.price <= :maxPrice) " +
            "AND (:brandId IS NULL OR p.brand.id = :brandId) " +
            "AND (:supplierId IS NULL OR p.supplier.id = :supplierId) " +
            "AND (:categoryId IS NULL OR p.category.id = :categoryId) " +
            "AND (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')))")
    Page<ProductModel> findProductsByFilters(
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("brandId") Long brandId,
            @Param("supplierId") Long supplierId,
            @Param("categoryId") Long categoryId,
            @Param("name") String name,
            Pageable pageable
    );

    ProductModel findByName(String name);
}
