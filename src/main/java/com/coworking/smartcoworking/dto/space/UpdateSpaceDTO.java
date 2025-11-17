package com.coworking.smartcoworking.dto.space;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSpaceDTO {

    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    private String name;

    @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
    private String description;

    @Positive(message = "Capacidade deve ser maior que zero")
    private Integer capacity;

    @DecimalMin(value = "0.0", message = "Preço por hora não pode ser negativo")
    private BigDecimal pricePerHour;

    @DecimalMin(value = "0.0", message = "Preço por dia não pode ser negativo")
    private BigDecimal pricePerDay;

    @DecimalMin(value = "0.0", message = "Preço por mês não pode ser negativo")
    private BigDecimal pricePerMonth;

    private Integer floor;
    private Boolean hasWifi;
    private Boolean hasProjector;
    private Boolean hasWhiteboard;
    private Boolean hasAC;
    private Boolean active;
}