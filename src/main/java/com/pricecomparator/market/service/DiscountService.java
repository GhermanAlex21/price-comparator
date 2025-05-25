package com.pricecomparator.market.service;

import com.pricecomparator.market.dto.BestDiscountDTO;
import com.pricecomparator.market.model.Discount;
import com.pricecomparator.market.repository.DiscountRepository;
import com.pricecomparator.market.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DiscountService {

    private final DiscountRepository discountRepository;

    public List<BestDiscountDTO> getBestDiscountsToday() {
        LocalDate today = LocalDate.now();
        List<Discount> activeDiscounts = discountRepository.findActiveDiscountsOrderByPercentageDesc(today);

        return activeDiscounts.stream().map(discount -> {
            Product product = discount.getProduct();
            return new BestDiscountDTO(
                    product.getName(),
                    product.getBrand().getName(),
                    product.getCategory().getName(),
                    discount.getSupermarket().getName(),
                    discount.getPercentageOfDiscount(),
                    discount.getFromDate(),
                    discount.getToDate()
            );
        }).toList();
    }

    public List<BestDiscountDTO> getNewDiscounts() {
        LocalDateTime last24h = LocalDateTime.now().minusHours(24);
        List<Discount> discounts = discountRepository.findAllAddedSince(last24h);

        return discounts.stream().map(d -> {
            Product p = d.getProduct();
            return new BestDiscountDTO(
                    p.getName(),
                    p.getBrand().getName(),
                    p.getCategory().getName(),
                    d.getSupermarket().getName(),
                    d.getPercentageOfDiscount(),
                    d.getFromDate(),
                    d.getToDate()
            );
        }).toList();
    }

}