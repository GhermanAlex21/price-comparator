package com.pricecomparator.market.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
public class ValuePerUnitDTO {
    private String productName;
    private String brandName;
    private String supermarketName;
    private BigDecimal price;
    private double packageQuantity;
    private String packageUnit;
    private BigDecimal valuePerUnit;

    public ValuePerUnitDTO(String productName, String brandName, String supermarketName,
                           BigDecimal price, double packageQuantity, String packageUnit, BigDecimal valuePerUnit) {
        this.productName = productName;
        this.brandName = brandName;
        this.supermarketName = supermarketName;
        this.price = price;
        this.packageQuantity = packageQuantity;
        this.packageUnit = packageUnit;
        this.valuePerUnit = valuePerUnit;
    }
}
