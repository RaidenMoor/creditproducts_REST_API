package com.example.restbank.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Stack;

@Entity
@Table(name = "credit_products")
@NoArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
@SequenceGenerator(name = "default_gen", sequenceName = "products_seq", allocationSize = 1, initialValue = 12)
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "jsonId")
public class CreditProduct extends GenericModel{
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "min_amount", nullable = false)
    private BigDecimal minAmount;

    @Column(name = "max_amount", nullable = false)
    private BigDecimal maxAmount;

    @Column(name = "interest_rate", nullable = false)
    private BigDecimal interestRate;

    @Column(name = "term_min", nullable = false)
    private int termMin;

    @Column(name = "term_max", nullable = false)
    private int termMax;

    @Column(name = "is_active")
    private boolean isActive;

    @OneToMany(mappedBy = "creditProduct")
    private Set<CreditApplication> applications;

}
