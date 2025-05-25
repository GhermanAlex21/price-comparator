package com.pricecomparator.market.repository;

import com.pricecomparator.market.model.Brand;
import com.pricecomparator.market.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByNameAndPackageQuantityAndPackageUnitAndBrand(
            String name,
            BigDecimal packageQuantity,
            String packageUnit,
            Brand brand
    );
}
