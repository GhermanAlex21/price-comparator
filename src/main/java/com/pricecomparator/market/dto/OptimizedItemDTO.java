package com.pricecomparator.market.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OptimizedItemDTO {
    private String productName;
    private String brand;
    private BigDecimal requestedQuantity;
    private String unit;
    private String supermarket;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
}