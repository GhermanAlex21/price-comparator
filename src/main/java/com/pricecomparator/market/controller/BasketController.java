package com.pricecomparator.market.controller;


import com.pricecomparator.market.dto.BasketItemRequest;
import com.pricecomparator.market.service.BasketService;
import com.pricecomparator.market.dto.OptimizedBasketResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/basket")
@RequiredArgsConstructor
public class BasketController {

    private final BasketService basketService;

    @PostMapping("/optimize")
    public OptimizedBasketResponse optimizeBasket(@RequestBody List<BasketItemRequest> basketItems) {
        return basketService.optimizeBasket(basketItems);
    }
}