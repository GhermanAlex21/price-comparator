package com.pricecomparator.market.dto;

import java.time.LocalDate;

public class BestDiscountDTO {
    private String productName;
    private String brandName;
    private String categoryName;
    private String supermarketName;
    private Integer discountPercentage;
    private LocalDate validFrom;
    private LocalDate validTo;

    public BestDiscountDTO(String productName, String brandName, String categoryName,
                           String supermarketName, Integer discountPercentage,
                           LocalDate validFrom, LocalDate validTo) {
        this.productName = productName;
        this.brandName = brandName;
        this.categoryName = categoryName;
        this.supermarketName = supermarketName;
        this.discountPercentage = discountPercentage;
        this.validFrom = validFrom;
        this.validTo = validTo;
    }


    public String getProductName() {
        return productName;
    }

    public String getBrandName() {
        return brandName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getSupermarketName() {
        return supermarketName;
    }

    public Integer getDiscountPercentage() {
        return discountPercentage;
    }

    public LocalDate getValidFrom() {
        return validFrom;
    }

    public LocalDate getValidTo() {
        return validTo;
    }
}