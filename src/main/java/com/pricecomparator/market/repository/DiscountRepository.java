package com.pricecomparator.market.repository;


import com.pricecomparator.market.model.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long>{
    @Query("SELECT d FROM Discount d " +
            "WHERE :today BETWEEN d.fromDate AND d.toDate " +
            "ORDER BY d.percentageOfDiscount DESC")
    List<Discount> findActiveDiscountsOrderByPercentageDesc(LocalDate today);

    @Query("SELECT d FROM Discount d WHERE d.createdAt >= :since")
    List<Discount> findAllAddedSince(LocalDateTime since);
}
