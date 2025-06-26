package com.example.creditproducts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;


@NoArgsConstructor
@AllArgsConstructor
public class IssuedLoanDTO extends GenericDTO{

    @Schema(description = "дата начала")
    private LocalDate startDate;

    @Schema(description = "дата завершения")
    private LocalDate endDate;

    @Schema(description = "выплата в месяц")
    private BigDecimal monthlyPayment;

    @Schema(description = "остаточная сумма")
    private BigDecimal remainingAmount;

    @Schema(description = "кредитная заявка")
    private Long creditApplicationId;
    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getMonthlyPayment() {
        return monthlyPayment;
    }

    public void setMonthlyPayment(BigDecimal monthlyAmount) {

        this.monthlyPayment = monthlyAmount;
    }

    public BigDecimal getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(BigDecimal remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public Long getCreditApplicationId() {
        return creditApplicationId;
    }

    public void setCreditApplicationId(Long creditApplicationId) {
        this.creditApplicationId = creditApplicationId;
    }
}
