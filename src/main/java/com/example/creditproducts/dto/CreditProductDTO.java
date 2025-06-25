package com.example.creditproducts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
public class CreditProductDTO extends GenericDTO{
    @Schema(description = "минимальная сумма")
    private BigDecimal minAmount;

    @Schema(description = "максимальная сумма")
    private BigDecimal maxAmount;

    @Schema(description = "процентная ставка")
    private BigDecimal interestRate;

    @Schema(description = "минимальный срок")
    private int termMin;

    @Schema(description = "максимальный срок")
    private int termMax;

    @Schema(description = "активен ли продукт для выдачи")
    private boolean isActive;

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

}

