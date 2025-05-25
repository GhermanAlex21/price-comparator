package com.pricecomparator.market.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="supermarket")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Supermarket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name",nullable = false,unique = true)
    private String name;


}
