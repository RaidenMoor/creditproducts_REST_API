package com.example.restbank.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
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

}
