package com.pricecomparator.market.dto;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
@Getter
public class OptimizedBasketResponse {
    private List<BasketItemResponse> items;
    private BigDecimal totalBasketPrice;

    public OptimizedBasketResponse() {
    }

    public OptimizedBasketResponse(List<BasketItemResponse> items, BigDecimal totalBasketPrice) {
        this.items = items;
        this.totalBasketPrice = totalBasketPrice;
    }

    public List<BasketItemResponse> getItems() {
        return items;
    }

    public void setItems(List<BasketItemResponse> items) {
        this.items = items;
    }

    public BigDecimal getTotalBasketPrice() {
        return totalBasketPrice;
    }

    public void setTotalBasketPrice(BigDecimal totalBasketPrice) {
        this.totalBasketPrice = totalBasketPrice;
    }
}