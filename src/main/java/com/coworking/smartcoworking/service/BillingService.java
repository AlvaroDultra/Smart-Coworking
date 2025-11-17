package com.coworking.smartcoworking.service;

import com.coworking.smartcoworking.dto.billing.BillingResponseDTO;
import com.coworking.smartcoworking.dto.billing.CreateBillingDTO;
import com.coworking.smartcoworking.dto.billing.UpdateBillingDTO;
import com.coworking.smartcoworking.entity.Billing;
import com.coworking.smartcoworking.entity.Reservation;
import com.coworking.smartcoworking.entity.User;
import com.coworking.smartcoworking.enums.BillingStatus;
import com.coworking.smartcoworking.exception.BusinessException;
import com.coworking.smartcoworking.exception.ResourceNotFoundException;
import com.coworking.smartcoworking.repository.BillingRepository;
import com.coworking.smartcoworking.repository.ReservationRepository;
import com.coworking.smartcoworking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BillingService {

    private final BillingRepository billingRepository;
    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;

    @Transactional
    public BillingResponseDTO create(CreateBillingDTO dto) {
        // Buscar Reservation e User
        Reservation reservation = reservationRepository.findById(dto.getReservationId())
                .orElseThrow(() -> new ResourceNotFoundException("Reserva", dto.getReservationId()));

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", dto.getUserId()));

        // Validar se já existe cobrança para essa reserva
        if (billingRepository.findByReservation(reservation).isPresent()) {
            throw new BusinessException("Já existe uma cobrança para esta reserva");
        }

        // Criar cobrança
        Billing billing = new Billing();
        billing.setReservation(reservation);
        billing.setUser(user);
        billing.setAmount(dto.getAmount());
        billing.setStatus(BillingStatus.PENDENTE);
        billing.setDueDate(dto.getDueDate());
        billing.setPaymentMethod(dto.getPaymentMethod());
        billing.setNotes(dto.getNotes());

        Billing saved = billingRepository.save(billing);
        return BillingResponseDTO.fromEntity(saved);
    }

    @Transactional(readOnly = true)
    public BillingResponseDTO findById(Long id) {
        Billing billing = billingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cobrança", id));

        return BillingResponseDTO.fromEntity(billing);
    }

    @Transactional(readOnly = true)
    public List<BillingResponseDTO> findAll() {
        return billingRepository.findAll().stream()
                .map(BillingResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BillingResponseDTO> findByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", userId));

        return billingRepository.findByUser(user).stream()
                .map(BillingResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BillingResponseDTO> findPendingByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", userId));

        return billingRepository.findByUserAndStatusOrderByDueDateAsc(user, BillingStatus.PENDENTE).stream()
                .map(BillingResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BillingResponseDTO> findOverdue() {
        return billingRepository.findOverdueBillings(LocalDate.now()).stream()
                .map(BillingResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BigDecimal calculateTotalDebt(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", userId));

        return billingRepository.calculateTotalDebt(user);
    }

    @Transactional(readOnly = true)
    public BigDecimal calculateTotalPaid(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", userId));

        return billingRepository.calculateTotalPaid(user);
    }

    @Transactional(readOnly = true)
    public boolean hasOverdueBillings(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", userId));

        return billingRepository.hasOverdueBillings(user);
    }

    @Transactional
    public BillingResponseDTO update(Long id, UpdateBillingDTO dto) {
        Billing billing = billingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cobrança", id));

        // Atualizar campos não nulos
        if (dto.getAmount() != null) {
            billing.setAmount(dto.getAmount());
        }
        if (dto.getStatus() != null) {
            billing.setStatus(dto.getStatus());
        }
        if (dto.getDueDate() != null) {
            billing.setDueDate(dto.getDueDate());
        }
        if (dto.getPaidDate() != null) {
            billing.setPaidDate(dto.getPaidDate());
        }
        if (dto.getPaymentMethod() != null) {
            billing.setPaymentMethod(dto.getPaymentMethod());
        }
        if (dto.getNotes() != null) {
            billing.setNotes(dto.getNotes());
        }

        Billing updated = billingRepository.save(billing);
        return BillingResponseDTO.fromEntity(updated);
    }

    @Transactional
    public BillingResponseDTO markAsPaid(Long id, String paymentMethod) {
        Billing billing = billingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cobrança", id));

        if (billing.getStatus() == BillingStatus.PAGA) {
            throw new BusinessException("Cobrança já está paga");
        }

        if (billing.getStatus() == BillingStatus.CANCELADA) {
            throw new BusinessException("Não é possível pagar uma cobrança cancelada");
        }

        billing.setStatus(BillingStatus.PAGA);
        billing.setPaidDate(LocalDate.now());
        billing.setPaymentMethod(paymentMethod);

        Billing updated = billingRepository.save(billing);
        return BillingResponseDTO.fromEntity(updated);
    }

    @Transactional
    public BillingResponseDTO markAsOverdue(Long id) {
        Billing billing = billingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cobrança", id));

        if (billing.getStatus() != BillingStatus.PENDENTE) {
            throw new BusinessException("Apenas cobranças pendentes podem ser marcadas como atrasadas");
        }

        billing.setStatus(BillingStatus.ATRASADA);

        Billing updated = billingRepository.save(billing);
        return BillingResponseDTO.fromEntity(updated);
    }

    @Transactional
    public BillingResponseDTO cancel(Long id) {
        Billing billing = billingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cobrança", id));

        if (billing.getStatus() == BillingStatus.PAGA) {
            throw new BusinessException("Não é possível cancelar uma cobrança já paga");
        }

        billing.setStatus(BillingStatus.CANCELADA);

        Billing updated = billingRepository.save(billing);
        return BillingResponseDTO.fromEntity(updated);
    }

    @Transactional
    public BillingResponseDTO refund(Long id) {
        Billing billing = billingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cobrança", id));

        if (billing.getStatus() != BillingStatus.PAGA) {
            throw new BusinessException("Apenas cobranças pagas podem ser reembolsadas");
        }

        billing.setStatus(BillingStatus.REEMBOLSADA);

        Billing updated = billingRepository.save(billing);
        return BillingResponseDTO.fromEntity(updated);
    }

    @Transactional
    public void delete(Long id) {
        Billing billing = billingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cobrança", id));

        // Só permitir deletar se estiver cancelada
        if (billing.getStatus() != BillingStatus.CANCELADA) {
            throw new BusinessException("Apenas cobranças canceladas podem ser deletadas");
        }

        billingRepository.delete(billing);
    }

    // ==================== MÉTODOS PARA JOBS AUTOMÁTICOS ====================

    @Transactional
    public void processOverdueBillings() {
        List<Billing> overdueBillings = billingRepository.findOverdueBillings(LocalDate.now());

        for (Billing billing : overdueBillings) {
            billing.setStatus(BillingStatus.ATRASADA);
            billingRepository.save(billing);
        }
    }

    @Transactional(readOnly = true)
    public List<BillingResponseDTO> findBillingsDueSoon(int days) {
        LocalDate today = LocalDate.now();
        LocalDate futureDate = today.plusDays(days);

        return billingRepository.findBillingsDueSoon(today, futureDate).stream()
                .map(BillingResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }
}