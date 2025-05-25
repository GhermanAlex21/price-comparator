package com.pricecomparator.market.repository;


import com.pricecomparator.market.model.Price;
import com.pricecomparator.market.model.Product;
import com.pricecomparator.market.model.Supermarket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PriceRepository extends JpaRepository<Price, Long>{
    List<Price> findByProductIdOrderByDateAsc(Long productId);

    List<Price> findByProductIdOrderByDateDesc(Long productId);
    List<Price> findByProductId(Long productId);

    List<Price> findByProduct_Category_NameIgnoreCase(String category);

    List<Price> findByProduct(Product product);
    List<Price> findByProduct_NameIgnoreCase(String name);

    boolean existsByProductAndSupermarketAndDate(Product product, Supermarket supermarket, LocalDate date);




    Optional<Price> findTopByProductOrderByDateDesc(Product product);

    @Query("""
    SELECT p FROM Price p
    JOIN p.product prod
    JOIN p.supermarket s
    WHERE prod.id = :productId
    AND (:supermarketName IS NULL OR s.name = :supermarketName)
    AND (:categoryName IS NULL OR prod.category.name = :categoryName)
    AND (:brandName IS NULL OR prod.brand.name = :brandName)
    ORDER BY p.date
""")
    List<Price> filterPrices(
            @Param("productId") Long productId,
            @Param("supermarketName") String supermarketName,
            @Param("categoryName") String categoryName,
            @Param("brandName") String brandName
    );





}
