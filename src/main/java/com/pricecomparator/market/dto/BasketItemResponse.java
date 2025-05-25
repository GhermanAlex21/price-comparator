package com.pricecomparator.market.dto;

import java.math.BigDecimal;

public class BasketItemResponse {

    private String productName;
    private String supermarket;
    private BigDecimal unitPrice;
    private int quantity;
    private BigDecimal totalPrice;

    public BasketItemResponse() {
    }

    public BasketItemResponse(String productName, String supermarket, BigDecimal unitPrice, int quantity, BigDecimal totalPrice) {
        this.productName = productName;
        this.supermarket = supermarket;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSupermarket() {
        return supermarket;
    }

    public void setSupermarket(String supermarket) {
        this.supermarket = supermarket;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
