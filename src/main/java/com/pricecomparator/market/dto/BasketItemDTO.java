package com.pricecomparator.market.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BasketItemDTO {
    private String productName;
    private BigDecimal quantity;
    private String unit;
}