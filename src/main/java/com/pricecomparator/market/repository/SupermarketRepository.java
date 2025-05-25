package com.pricecomparator.market.repository;


import com.pricecomparator.market.model.Supermarket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface SupermarketRepository extends JpaRepository<Supermarket, Long>{
    Optional<Supermarket> findByNameIgnoreCase(String name);
}
