package com.coworking.smartcoworking.dto.billing;

import com.coworking.smartcoworking.dto.reservation.ReservationResponseDTO;
import com.coworking.smartcoworking.dto.user.UserResponseDTO;
import com.coworking.smartcoworking.enums.BillingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillingResponseDTO {

    private Long id;
    private ReservationResponseDTO reservation;
    private UserResponseDTO user;
    private BigDecimal amount;
    private BillingStatus status;
    private LocalDate dueDate;
    private LocalDate paidDate;
    private String paymentMethod;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Método helper para conversão
    public static BillingResponseDTO fromEntity(com.coworking.smartcoworking.entity.Billing billing) {
        BillingResponseDTO dto = new BillingResponseDTO();
        dto.setId(billing.getId());

        // Converter Reservation para ReservationResponseDTO
        dto.setReservation(ReservationResponseDTO.fromEntity(billing.getReservation()));

        // Converter User para UserResponseDTO
        dto.setUser(UserResponseDTO.fromEntity(billing.getUser()));

        dto.setAmount(billing.getAmount());
        dto.setStatus(billing.getStatus());
        dto.setDueDate(billing.getDueDate());
        dto.setPaidDate(billing.getPaidDate());
        dto.setPaymentMethod(billing.getPaymentMethod());
        dto.setNotes(billing.getNotes());
        dto.setCreatedAt(billing.getCreatedAt());
        dto.setUpdatedAt(billing.getUpdatedAt());

        return dto;
    }
}