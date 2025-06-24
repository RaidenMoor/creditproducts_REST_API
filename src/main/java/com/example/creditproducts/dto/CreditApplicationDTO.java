package com.example.restbank.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
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

}
