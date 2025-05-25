package com.pricecomparator.market.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
public class AlertRequestDTO {
    private Long productId;
    private String email;
    private BigDecimal targetPrice;
}