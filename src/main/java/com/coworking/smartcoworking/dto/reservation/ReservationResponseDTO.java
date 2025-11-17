package com.coworking.smartcoworking.dto.reservation;

import com.coworking.smartcoworking.dto.space.SpaceResponseDTO;
import com.coworking.smartcoworking.dto.user.UserResponseDTO;
import com.coworking.smartcoworking.enums.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationResponseDTO {

    private Long id;
    private UserResponseDTO user;
    private SpaceResponseDTO space;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private BigDecimal totalPrice;
    private ReservationStatus status;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Método helper para conversão
    public static ReservationResponseDTO fromEntity(com.coworking.smartcoworking.entity.Reservation reservation) {
        ReservationResponseDTO dto = new ReservationResponseDTO();
        dto.setId(reservation.getId());

        // Converter User para UserResponseDTO
        dto.setUser(UserResponseDTO.fromEntity(reservation.getUser()));

        // Converter Space para SpaceResponseDTO
        dto.setSpace(SpaceResponseDTO.fromEntity(reservation.getSpace()));

        dto.setStartDateTime(reservation.getStartDateTime());
        dto.setEndDateTime(reservation.getEndDateTime());
        dto.setTotalPrice(reservation.getTotalPrice());
        dto.setStatus(reservation.getStatus());
        dto.setCheckInTime(reservation.getCheckInTime());
        dto.setCheckOutTime(reservation.getCheckOutTime());
        dto.setNotes(reservation.getNotes());
        dto.setCreatedAt(reservation.getCreatedAt());
        dto.setUpdatedAt(reservation.getUpdatedAt());

        return dto;
    }
}