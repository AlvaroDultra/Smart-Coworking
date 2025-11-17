package com.coworking.smartcoworking.dto.billing;

import com.coworking.smartcoworking.enums.BillingStatus;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBillingDTO {

    @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
    private BigDecimal amount;

    private BillingStatus status;

    private LocalDate dueDate;

    private LocalDate paidDate;

    private String paymentMethod;

    private String notes;
}