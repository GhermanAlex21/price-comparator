package com.pricecomparator.market.controller;

import com.pricecomparator.market.dto.BestDiscountDTO;
import com.pricecomparator.market.service.DiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/discounts")
@RequiredArgsConstructor
public class DiscountController {

    private final DiscountService discountService;

    @GetMapping("/best")
    public List<BestDiscountDTO> getBestDiscountsToday() {
        return discountService.getBestDiscountsToday();
    }
    @GetMapping("/new")
    public List<BestDiscountDTO> getNewDiscounts() {
        return discountService.getNewDiscounts();
    }
}
