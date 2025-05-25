package com.pricecomparator.market.controller;

import com.pricecomparator.market.dto.ActiveAlertDTO;
import com.pricecomparator.market.dto.AlertRequestDTO;
import com.pricecomparator.market.service.AlertService;
import com.pricecomparator.market.dto.TriggeredAlertDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
public class AlertController {

    @Autowired
    private AlertService alertService;

    @PostMapping
    public ResponseEntity<Void> createAlert(@RequestBody AlertRequestDTO dto) {
        alertService.createAlert(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/matches")
    public List<TriggeredAlertDTO> getMatchingAlerts() {
        return alertService.getMatchingAlerts();
    }

    @GetMapping("/active")
    public List<ActiveAlertDTO> getActiveAlerts() {
        return alertService.getActiveAlerts();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlert(@PathVariable Long id) {
        alertService.deleteAlertById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/check")
    public ResponseEntity<Void> checkAlertsManually() {
        alertService.checkAlerts();
        return ResponseEntity.ok().build();
    }





}
