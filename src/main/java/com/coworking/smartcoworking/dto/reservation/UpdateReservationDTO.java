package com.coworking.smartcoworking.dto.reservation;

import com.coworking.smartcoworking.enums.ReservationStatus;
import jakarta.validation.constraints.Future;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateReservationDTO {

    @Future(message = "Data/hora de início deve ser no futuro")
    private LocalDateTime startDateTime;

    @Future(message = "Data/hora de fim deve ser no futuro")
    private LocalDateTime endDateTime;

    private ReservationStatus status;

    private String notes;
}