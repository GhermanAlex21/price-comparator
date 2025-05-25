package com.pricecomparator.market.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PriceHistoryDTO {
    private LocalDate date;
    private String supermarketName;
    private BigDecimal price;
    private String currency;

    private String productName;

    public PriceHistoryDTO(LocalDate date,String productName, String supermarketName, BigDecimal price, String currency) {
        this.date = date;
        this.supermarketName = supermarketName;
        this.productName= productName;
        this.price = price;
        this.currency = currency;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getSupermarketName() {
        return supermarketName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getCurrency() {
        return currency;
    }

    public String getProductName() {
        return productName;
    }
}