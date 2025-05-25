package com.pricecomparator.market.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRecommendationDTO {
    private String productName;
    private String brandName;
    private String supermarketName;
    private BigDecimal price;
    private double quantity;
    private String unit;
    private BigDecimal valuePerUnit;
}
