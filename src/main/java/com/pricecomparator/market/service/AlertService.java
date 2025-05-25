package com.pricecomparator.market.service;

import com.pricecomparator.market.dto.ActiveAlertDTO;
import com.pricecomparator.market.dto.AlertRequestDTO;
import com.pricecomparator.market.dto.TriggeredAlertDTO;
import com.pricecomparator.market.model.Alert;
import com.pricecomparator.market.model.Price;
import com.pricecomparator.market.model.Product;
import com.pricecomparator.market.repository.AlertRepository;
import com.pricecomparator.market.repository.PriceRepository;
import com.pricecomparator.market.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AlertService {

    @Autowired
    private AlertRepository alertRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PriceRepository priceRepository;

    public void createAlert(AlertRequestDTO dto) {
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Alert alert = new Alert();
        alert.setProduct(product);
        alert.setEmail(dto.getEmail());
        alert.setTargetPrice(dto.getTargetPrice());
        alert.setCreatedAt(LocalDate.now());
        alert.setNotified(false);

        alertRepository.save(alert);
    }

    public List<TriggeredAlertDTO> getMatchingAlerts() {
        List<TriggeredAlertDTO> results = new ArrayList<>();

        List<Alert> alerts = alertRepository.findAll();

        for (Alert alert : alerts) {
            if (alert.isNotified()) {
                continue;
            }

            Optional<Price> latestPriceOpt = priceRepository.findTopByProductOrderByDateDesc(alert.getProduct());

            if (latestPriceOpt.isPresent()) {
                Price price = latestPriceOpt.get();

                if (price.getValue().compareTo(alert.getTargetPrice()) <= 0) {
                    results.add(new TriggeredAlertDTO(
                            alert.getProduct().getName(),
                            price.getSupermarket().getName(),
                            price.getValue(),
                            alert.getTargetPrice(),
                            price.getDate(),
                            price.getCurrency()
                    ));

                    alert.setNotified(true);
                    alertRepository.save(alert);
                }
            }
        }

        return results;
    }

    public List<ActiveAlertDTO> getActiveAlerts() {
        return alertRepository.findByNotifiedFalse().stream()
                .map(alert -> new ActiveAlertDTO(
                        alert.getProduct().getName(),
                        alert.getEmail(),
                        alert.getTargetPrice()
                ))
                .collect(Collectors.toList());
    }

    public void deleteAlertById(Long id) {
        if (!alertRepository.existsById(id)) {
            throw new NoSuchElementException("Alert with ID " + id + " does not exist.");
        }
        alertRepository.deleteById(id);
    }

    @Transactional
    public void checkAlerts() {
        List<Alert> alerts = alertRepository.findByNotifiedFalse();

        for (Alert alert : alerts) {
            Optional<Price> latestPriceOpt = priceRepository.findTopByProductOrderByDateDesc(alert.getProduct());

            if (latestPriceOpt.isPresent()) {
                Price latestPrice = latestPriceOpt.get();

                if (latestPrice.getValue().compareTo(alert.getTargetPrice()) <= 0) {
                    alert.setNotified(true);
                    alertRepository.save(alert);
                    System.out.println("🔔 Alert triggered for: " +
                            alert.getEmail() + " -> " +
                            alert.getProduct().getName() + " @ " +
                            latestPrice.getValue() + " " +
                            latestPrice.getCurrency());
                }
            }
        }
    }

    @Scheduled(fixedRate = 86_400_000)
    public void scheduledAlertCheck() {
        System.out.println("🔁 Scheduled alert check started...");
        checkAlerts();
    }



}
