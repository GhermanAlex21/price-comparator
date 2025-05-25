package com.pricecomparator.market.repository;


import com.pricecomparator.market.model.Alert;
import com.pricecomparator.market.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long>{
    List<Alert> findByProduct(Product product);

    List<Alert> findByNotifiedFalse();



}
