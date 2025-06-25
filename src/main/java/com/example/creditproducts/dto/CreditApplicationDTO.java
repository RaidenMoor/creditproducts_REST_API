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
public class CreditApplicationDTO extends GenericDTO{
    @Schema(description = "Идентификатор клиента")
    private Long clientId;

    @Schema(description = "продукт")
    private Long creditProductId;

    @Schema(description = "сумма")
    private BigDecimal amount;

    @Schema(description = "статус заявки")
    private String status;

    @Schema(description = "срок в месяцах")
    private int termMonths;

    @Schema(description = "дата подачи заявки")
    private LocalDate applicationDate;

    @Schema(description = "дата решения(отказа/одобрения)")
    private LocalDate decisionDate;

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getCreditProductId() {
        return creditProductId;
    }

    public void setCreditProductId(Long creditProductId) {
        this.creditProductId = creditProductId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTermMonths() {
        return termMonths;
    }

    public void setTermMonths(int termMonths) {
        this.termMonths = termMonths;
    }

    public LocalDate getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(LocalDate applicationDate) {
        this.applicationDate = applicationDate;
    }

    public LocalDate getDecisionDate() {
        return decisionDate;
    }

    public void setDecisionDate(LocalDate decisionDate) {
        this.decisionDate = decisionDate;
    }

}

