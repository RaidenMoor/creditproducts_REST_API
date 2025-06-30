package com.example.creditproducts.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.*;

@Entity
@Table(name = "credit_products")
@NoArgsConstructor
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
    private Set<CreditApplication> applications = new HashSet<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(BigDecimal minAmount) {
        this.minAmount = minAmount;
    }

    public BigDecimal getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(BigDecimal maxAmount) {
        this.maxAmount = maxAmount;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public int getTermMin() {
        return termMin;
    }

    public void setTermMin(int termMin) {
        this.termMin = termMin;
    }

    public int getTermMax() {
        return termMax;
    }

    public void setTermMax(int termMax) {
        this.termMax = termMax;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Set<CreditApplication> getApplications() {
        return applications;
    }

    public void setApplications(Set<CreditApplication> applications) {
        this.applications = applications;
    }
}
