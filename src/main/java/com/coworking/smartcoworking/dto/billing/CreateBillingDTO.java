package com.coworking.smartcoworking.dto.billing;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateBillingDTO {

    @NotNull(message = "ID da reserva é obrigatório")
    private Long reservationId;

    @NotNull(message = "ID do usuário é obrigatório")
    private Long userId;

    @NotNull(message = "Valor é obrigatório")
    @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
    private BigDecimal amount;

    @NotNull(message = "Data de vencimento é obrigatória")
    @Future(message = "Data de vencimento deve ser no futuro")
    private LocalDate dueDate;

    private String paymentMethod;

    private String notes;
}