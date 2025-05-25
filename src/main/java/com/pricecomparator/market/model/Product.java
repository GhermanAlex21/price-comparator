package com.pricecomparator.market.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name="product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "package_quantity",nullable = false)
    private BigDecimal packageQuantity;

    @Column(name="package_unit", nullable=false)
    private String packageUnit;

    @ManyToOne
    @JoinColumn(name="id_category",nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "id_brand")
    private Brand brand;
}
