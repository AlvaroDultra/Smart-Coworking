package com.coworking.smartcoworking.service;

import com.coworking.smartcoworking.dto.reservation.CreateReservationDTO;
import com.coworking.smartcoworking.dto.reservation.ReservationResponseDTO;
import com.coworking.smartcoworking.dto.reservation.UpdateReservationDTO;
import com.coworking.smartcoworking.entity.Billing;
import com.coworking.smartcoworking.entity.Reservation;
import com.coworking.smartcoworking.entity.Space;
import com.coworking.smartcoworking.entity.User;
import com.coworking.smartcoworking.enums.BillingStatus;
import com.coworking.smartcoworking.enums.ReservationStatus;
import com.coworking.smartcoworking.exception.BusinessException;
import com.coworking.smartcoworking.exception.ConflictException;
import com.coworking.smartcoworking.exception.ResourceNotFoundException;
import com.coworking.smartcoworking.repository.BillingRepository;
import com.coworking.smartcoworking.repository.ReservationRepository;
import com.coworking.smartcoworking.repository.SpaceRepository;
import com.coworking.smartcoworking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final SpaceRepository spaceRepository;
    private final BillingRepository billingRepository;

    @Transactional
    public ReservationResponseDTO create(CreateReservationDTO dto) {
        // 1. Buscar User e Space
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", dto.getUserId()));

        Space space = spaceRepository.findById(dto.getSpaceId())
                .orElseThrow(() -> new ResourceNotFoundException("Espaço", dto.getSpaceId()));

        // 2. Validações de negócio
        validateReservation(dto, space);

        // 3. Verificar conflitos de horário
        List<Reservation> conflicts = reservationRepository.findConflictingReservations(
                space, dto.getStartDateTime(), dto.getEndDateTime()
        );

        if (!conflicts.isEmpty()) {
            throw new ConflictException("Já existe uma reserva neste horário");
        }

        // 4. Calcular preço total
        BigDecimal totalPrice = calculatePrice(space, dto.getStartDateTime(), dto.getEndDateTime());

        // 5. Criar reserva
        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setSpace(space);
        reservation.setStartDateTime(dto.getStartDateTime());
        reservation.setEndDateTime(dto.getEndDateTime());
        reservation.setTotalPrice(totalPrice);
        reservation.setStatus(ReservationStatus.PENDENTE);
        reservation.setNotes(dto.getNotes());

        // 6. Salvar reserva
        Reservation saved = reservationRepository.save(reservation);

        // 7. Criar cobrança automática
        createBilling(saved);

        // 8. Retornar DTO
        return ReservationResponseDTO.fromEntity(saved);
    }

    @Transactional(readOnly = true)
    public ReservationResponseDTO findById(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva", id));

        return ReservationResponseDTO.fromEntity(reservation);
    }

    @Transactional(readOnly = true)
    public List<ReservationResponseDTO> findAll() {
        return reservationRepository.findAll().stream()
                .map(ReservationResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ReservationResponseDTO> findByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", userId));

        return reservationRepository.findByUserOrderByStartDateTimeDesc(user).stream()
                .map(ReservationResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ReservationResponseDTO> findBySpaceId(Long spaceId) {
        Space space = spaceRepository.findById(spaceId)
                .orElseThrow(() -> new ResourceNotFoundException("Espaço", spaceId));

        return reservationRepository.findBySpace(space).stream()
                .map(ReservationResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ReservationResponseDTO> findUpcomingByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", userId));

        return reservationRepository.findUpcomingReservations(user, LocalDateTime.now()).stream()
                .map(ReservationResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public ReservationResponseDTO update(Long id, UpdateReservationDTO dto) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva", id));

        // Não permitir alterar reservas já concluídas ou canceladas
        if (reservation.getStatus() == ReservationStatus.CONCLUIDA ||
                reservation.getStatus() == ReservationStatus.CANCELADA) {
            throw new BusinessException("Não é possível alterar uma reserva concluída ou cancelada");
        }

        // Atualizar horários se fornecidos
        if (dto.getStartDateTime() != null && dto.getEndDateTime() != null) {
            // Verificar conflitos com o novo horário
            List<Reservation> conflicts = reservationRepository.findConflictingReservations(
                    reservation.getSpace(), dto.getStartDateTime(), dto.getEndDateTime()
            );

            // Remover a própria reserva da lista de conflitos
            conflicts = conflicts.stream()
                    .filter(r -> !r.getId().equals(id))
                    .collect(Collectors.toList());

            if (!conflicts.isEmpty()) {
                throw new ConflictException("Já existe uma reserva neste horário");
            }

            reservation.setStartDateTime(dto.getStartDateTime());
            reservation.setEndDateTime(dto.getEndDateTime());

            // Recalcular preço
            BigDecimal newPrice = calculatePrice(
                    reservation.getSpace(),
                    dto.getStartDateTime(),
                    dto.getEndDateTime()
            );
            reservation.setTotalPrice(newPrice);

            // Atualizar valor na cobrança
            updateBillingAmount(reservation, newPrice);
        }

        if (dto.getStatus() != null) {
            reservation.setStatus(dto.getStatus());
        }

        if (dto.getNotes() != null) {
            reservation.setNotes(dto.getNotes());
        }

        Reservation updated = reservationRepository.save(reservation);
        return ReservationResponseDTO.fromEntity(updated);
    }

    @Transactional
    public ReservationResponseDTO cancel(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva", id));

        if (reservation.getStatus() == ReservationStatus.CONCLUIDA) {
            throw new BusinessException("Não é possível cancelar uma reserva já concluída");
        }

        reservation.setStatus(ReservationStatus.CANCELADA);
        Reservation updated = reservationRepository.save(reservation);

        // Cancelar cobrança
        cancelBilling(reservation);

        return ReservationResponseDTO.fromEntity(updated);
    }

    @Transactional
    public ReservationResponseDTO checkIn(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva", id));

        if (reservation.getStatus() != ReservationStatus.CONFIRMADA) {
            throw new BusinessException("Apenas reservas confirmadas podem fazer check-in");
        }

        LocalDateTime now = LocalDateTime.now();

        // Verificar se está no horário
        if (now.isBefore(reservation.getStartDateTime().minusMinutes(15))) {
            throw new BusinessException("Check-in só pode ser feito até 15 minutos antes do horário");
        }

        if (now.isAfter(reservation.getEndDateTime())) {
            throw new BusinessException("Horário da reserva já passou");
        }

        reservation.setCheckInTime(now);
        reservation.setStatus(ReservationStatus.EM_USO);

        Reservation updated = reservationRepository.save(reservation);
        return ReservationResponseDTO.fromEntity(updated);
    }

    @Transactional
    public ReservationResponseDTO checkOut(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva", id));

        if (reservation.getStatus() != ReservationStatus.EM_USO) {
            throw new BusinessException("Apenas reservas em uso podem fazer check-out");
        }

        if (reservation.getCheckInTime() == null) {
            throw new BusinessException("Não foi feito check-in nesta reserva");
        }

        reservation.setCheckOutTime(LocalDateTime.now());
        reservation.setStatus(ReservationStatus.CONCLUIDA);

        Reservation updated = reservationRepository.save(reservation);
        return ReservationResponseDTO.fromEntity(updated);
    }

    @Transactional
    public void delete(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva", id));

        // Só permitir deletar se estiver cancelada
        if (reservation.getStatus() != ReservationStatus.CANCELADA) {
            throw new BusinessException("Apenas reservas canceladas podem ser deletadas");
        }

        reservationRepository.delete(reservation);
    }

    // ==================== MÉTODOS AUXILIARES ====================

    private void validateReservation(CreateReservationDTO dto, Space space) {
        // Validar se espaço está ativo
        if (!space.getActive()) {
            throw new BusinessException("Espaço está inativo");
        }

        // Validar se data de fim é depois da data de início
        if (!dto.getEndDateTime().isAfter(dto.getStartDateTime())) {
            throw new BusinessException("Data de fim deve ser após a data de início");
        }

        // Validar duração mínima (1 hora)
        Duration duration = Duration.between(dto.getStartDateTime(), dto.getEndDateTime());
        if (duration.toMinutes() < 60) {
            throw new BusinessException("Duração mínima da reserva é de 1 hora");
        }
    }

    private BigDecimal calculatePrice(Space space, LocalDateTime start, LocalDateTime end) {
        // Calcular duração em horas
        Duration duration = Duration.between(start, end);
        long hours = duration.toHours();

        // Se duração for menor que 1 hora, cobrar 1 hora
        if (hours < 1) {
            hours = 1;
        }

        // Calcular preço baseado no tipo de espaço e duração
        BigDecimal pricePerHour = space.getPricePerHour();

        if (pricePerHour == null || pricePerHour.compareTo(BigDecimal.ZERO) == 0) {
            throw new BusinessException("Espaço sem preço definido");
        }

        return pricePerHour.multiply(BigDecimal.valueOf(hours));
    }

    private void createBilling(Reservation reservation) {
        Billing billing = new Billing();
        billing.setReservation(reservation);
        billing.setUser(reservation.getUser());
        billing.setAmount(reservation.getTotalPrice());
        billing.setStatus(BillingStatus.PENDENTE);
        billing.setDueDate(reservation.getStartDateTime().toLocalDate());

        billingRepository.save(billing);
    }

    private void updateBillingAmount(Reservation reservation, BigDecimal newAmount) {
        billingRepository.findByReservation(reservation).ifPresent(billing -> {
            billing.setAmount(newAmount);
            billingRepository.save(billing);
        });
    }

    private void cancelBilling(Reservation reservation) {
        billingRepository.findByReservation(reservation).ifPresent(billing -> {
            if (billing.getStatus() == BillingStatus.PENDENTE) {
                billing.setStatus(BillingStatus.CANCELADA);
                billingRepository.save(billing);
            }
        });
    }
}