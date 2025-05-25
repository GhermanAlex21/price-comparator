package com.pricecomparator.market.service;

import com.pricecomparator.market.dto.*;
import com.pricecomparator.market.model.Price;
import com.pricecomparator.market.repository.PriceRepository;
import com.pricecomparator.market.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PriceService {

    private final PriceRepository priceRepository;
    private final ProductRepository productRepository;

    public List<PriceHistoryDTO> getPriceHistoryForProduct(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new NoSuchElementException("Product with ID " + productId + " does not exist.");
        }

        List<Price> prices = priceRepository.findByProductIdOrderByDateAsc(productId);

        return prices.stream().map(price -> new PriceHistoryDTO(
                price.getDate(),
                price.getProduct().getName(),
                price.getSupermarket().getName(),
                price.getValue(),
                price.getCurrency()
        )).toList();
    }

    public List<PriceHistoryDTO> getFilteredPriceHistory(Long productId, String supermarket, LocalDate from, LocalDate to) {
        if (!productRepository.existsById(productId)) {
            throw new NoSuchElementException("Product with ID " + productId + " does not exist.");
        }

        List<Price> prices = priceRepository.findByProductId(productId);

        return prices.stream()
                .filter(p -> supermarket == null || p.getSupermarket().getName().equalsIgnoreCase(supermarket))
                .filter(p -> from == null || !p.getDate().isBefore(from))
                .filter(p -> to == null || !p.getDate().isAfter(to))
                .sorted(Comparator.comparing(Price::getDate))
                .map(price -> new PriceHistoryDTO(
                        price.getDate(),
                        price.getProduct().getName(),
                        price.getSupermarket().getName(),
                        price.getValue(),
                        price.getCurrency()
                ))
                .toList();
    }

    public List<ValuePerUnitDTO> getValuePerUnitByCategory(String categoryName) {
        List<Price> prices = priceRepository.findByProduct_Category_NameIgnoreCase(categoryName);

        return prices.stream()
                .filter(p -> p.getProduct().getPackageQuantity().compareTo(BigDecimal.ZERO) != 0)
                .map(p -> {
                    BigDecimal valuePerUnit = p.getValue()
                            .divide(BigDecimal.valueOf(p.getProduct().getPackageQuantity().doubleValue()), 2, RoundingMode.HALF_UP);
                    return new ValuePerUnitDTO(
                            p.getProduct().getName(),
                            p.getProduct().getBrand().getName(),
                            p.getSupermarket().getName(),
                            p.getValue(),
                            p.getProduct().getPackageQuantity().doubleValue(),
                            p.getProduct().getPackageUnit(),
                            valuePerUnit
                    );
                })
                .sorted(Comparator.comparing(ValuePerUnitDTO::getValuePerUnit))
                .toList();
    }

    public List<OptimizedItemDTO> optimizeBasket(List<BasketItemDTO> items) {
        List<OptimizedItemDTO> optimizedItems = new ArrayList<>();

        for (BasketItemDTO item : items) {
            List<Price> matchingPrices = priceRepository
                    .findByProduct_NameIgnoreCase(item.getProductName());

            Optional<Price> bestPrice = matchingPrices.stream()
                    .filter(p -> p.getProduct().getPackageUnit().equalsIgnoreCase(item.getUnit()))
                    .filter(p -> p.getProduct().getPackageQuantity().compareTo(BigDecimal.ZERO) > 0)
                    .min(Comparator.comparing(p ->
                            p.getValue().divide(
                                    p.getProduct().getPackageQuantity(), 4, RoundingMode.HALF_UP)
                    ));

            if (bestPrice.isPresent()) {
                Price price = bestPrice.get();
                BigDecimal unitPrice = price.getValue().divide(
                        price.getProduct().getPackageQuantity(), 4, RoundingMode.HALF_UP);
                BigDecimal totalPrice = unitPrice.multiply(item.getQuantity());

                optimizedItems.add(new OptimizedItemDTO(
                        price.getProduct().getName(),
                        price.getProduct().getBrand().getName(),
                        item.getQuantity(),
                        item.getUnit(),
                        price.getSupermarket().getName(),
                        unitPrice.setScale(2, RoundingMode.HALF_UP),
                        totalPrice.setScale(2, RoundingMode.HALF_UP)
                ));
            }
        }

        return optimizedItems;
    }

    public List<ProductRecommendationDTO> getProductRecommendationsByCategory(String categoryName) {
        List<Price> prices = priceRepository.findByProduct_Category_NameIgnoreCase(categoryName);

        return prices.stream()
                .filter(p -> p.getProduct().getPackageQuantity().compareTo(BigDecimal.ZERO) != 0)
                .map(p -> {
                    BigDecimal valuePerUnit = p.getValue()
                            .divide(BigDecimal.valueOf(p.getProduct().getPackageQuantity().doubleValue()), 2, RoundingMode.HALF_UP);

                    return new ProductRecommendationDTO(
                            p.getProduct().getName(),
                            p.getProduct().getBrand().getName(),
                            p.getSupermarket().getName(),
                            p.getValue(),
                            p.getProduct().getPackageQuantity().doubleValue(),
                            p.getProduct().getPackageUnit(),
                            valuePerUnit
                    );
                })
                .sorted(Comparator.comparing(ProductRecommendationDTO::getValuePerUnit))
                .toList();
    }


}
