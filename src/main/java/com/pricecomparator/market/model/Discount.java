package com.pricecomparator.market.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "discount")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="from_date",nullable = false)
    private LocalDate fromDate;

    @Column(name = "to_date", nullable = false)
    private LocalDate toDate;

    @Column(name="percentage_of_discount",nullable = false)
    private Integer percentageOfDiscount;

    @ManyToOne
    @JoinColumn(name = "id_product",nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "id_supermarket",nullable = false)
    private Supermarket supermarket;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }


}
