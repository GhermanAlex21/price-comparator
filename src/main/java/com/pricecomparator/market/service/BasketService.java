package com.pricecomparator.market.service;

import com.pricecomparator.market.dto.BasketItemRequest;
import com.pricecomparator.market.dto.BasketItemResponse;
import com.pricecomparator.market.dto.OptimizedBasketResponse;
import com.pricecomparator.market.model.Price;
import com.pricecomparator.market.repository.PriceRepository;
import com.pricecomparator.market.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BasketService {

    private final ProductRepository productRepository;
    private final PriceRepository priceRepository;

    public OptimizedBasketResponse optimizeBasket(List<BasketItemRequest> basket) {
        List<BasketItemResponse> optimizedItems = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (BasketItemRequest item : basket) {
            var product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("Product with id " + item.getProductId() + " not found"));

            var bestPrice = priceRepository.findByProduct(product).stream()
                    .min(Comparator.comparing(Price::getValue))
                    .orElseThrow(() -> new IllegalStateException("No price found for product " + product.getName()));

            BigDecimal itemTotal = bestPrice.getValue().multiply(BigDecimal.valueOf(item.getQuantity()));
            total = total.add(itemTotal);

            BasketItemResponse itemResponse = new BasketItemResponse(
                    product.getName(),
                    bestPrice.getSupermarket().getName(),
                    bestPrice.getValue(),
                    item.getQuantity(),
                    itemTotal
            );

            optimizedItems.add(itemResponse);
        }

        return new OptimizedBasketResponse(optimizedItems, total);
    }
}