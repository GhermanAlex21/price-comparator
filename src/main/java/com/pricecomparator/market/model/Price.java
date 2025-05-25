package com.pricecomparator.market.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "price")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Price {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "value",nullable = false)
    private BigDecimal value;

    @Column(name = "currency",nullable = false)
    private String currency;

    @Column(name = "date",nullable = false)
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "id_product",nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "id_supermarket",nullable = false)
    private Supermarket supermarket;
}
