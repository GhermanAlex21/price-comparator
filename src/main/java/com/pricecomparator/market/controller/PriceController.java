package com.pricecomparator.market.controller;

import com.pricecomparator.market.dto.*;
import com.pricecomparator.market.service.PriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/prices")
@RequiredArgsConstructor
public class PriceController {

    private final PriceService priceService;

    @GetMapping("/history/{productId}")
    public List<PriceHistoryDTO> getPriceHistory(@PathVariable Long productId) {
        return priceService.getPriceHistoryForProduct(productId);
    }
    @GetMapping("/history/filter")
    public List<PriceHistoryDTO> filterPriceHistory(
            @RequestParam Long productId,
            @RequestParam(required = false) String supermarket,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        return priceService.getFilteredPriceHistory(productId, supermarket, from, to);
    }

    @GetMapping("/value-per-unit")
    public List<ValuePerUnitDTO> getBestValueProducts(@RequestParam String category) {
        return priceService.getValuePerUnitByCategory(category);
    }

    @PostMapping("/optimize-basket")
    public List<OptimizedItemDTO> optimizeBasket(@RequestBody List<BasketItemDTO> basketItems) {
        return priceService.optimizeBasket(basketItems);
    }

    @GetMapping("/recommendations")
    public List<ProductRecommendationDTO> getProductRecommendations(@RequestParam String category) {
        return priceService.getProductRecommendationsByCategory(category);
    }



}