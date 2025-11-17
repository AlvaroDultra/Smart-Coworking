package com.coworking.smartcoworking.dto.reservation;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateReservationDTO {

    @NotNull(message = "ID do usuário é obrigatório")
    private Long userId;

    @NotNull(message = "ID do espaço é obrigatório")
    private Long spaceId;

    @NotNull(message = "Data/hora de início é obrigatória")
    @Future(message = "Data/hora de início deve ser no futuro")
    private LocalDateTime startDateTime;

    @NotNull(message = "Data/hora de fim é obrigatória")
    @Future(message = "Data/hora de fim deve ser no futuro")
    private LocalDateTime endDateTime;

    private String notes;
}