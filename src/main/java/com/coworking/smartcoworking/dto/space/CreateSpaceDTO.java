package com.coworking.smartcoworking.dto.space;

import com.coworking.smartcoworking.enums.SpaceType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateSpaceDTO {

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    private String name;

    @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
    private String description;

    @NotNull(message = "Tipo é obrigatório")
    private SpaceType type;

    @NotNull(message = "Capacidade é obrigatória")
    @Positive(message = "Capacidade deve ser maior que zero")
    private Integer capacity;

    @DecimalMin(value = "0.0", message = "Preço por hora não pode ser negativo")
    private BigDecimal pricePerHour;

    @DecimalMin(value = "0.0", message = "Preço por dia não pode ser negativo")
    private BigDecimal pricePerDay;

    @DecimalMin(value = "0.0", message = "Preço por mês não pode ser negativo")
    private BigDecimal pricePerMonth;

    @NotNull(message = "Andar é obrigatório")
    private Integer floor;

    private Boolean hasWifi = true;
    private Boolean hasProjector = false;
    private Boolean hasWhiteboard = false;
    private Boolean hasAC = true;
}