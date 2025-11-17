package com.coworking.smartcoworking.repository;

import com.coworking.smartcoworking.entity.Billing;
import com.coworking.smartcoworking.entity.Reservation;
import com.coworking.smartcoworking.entity.User;
import com.coworking.smartcoworking.enums.BillingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BillingRepository extends JpaRepository<Billing, Long> {

    // Buscar cobrança por reserva
    Optional<Billing> findByReservation(Reservation reservation);

    // Buscar cobranças por usuário
    List<Billing> findByUser(User user);

    // Buscar cobranças por usuário e status
    List<Billing> findByUserAndStatus(User user, BillingStatus status);

    // Buscar cobranças por status
    List<Billing> findByStatus(BillingStatus status);

    // Buscar cobranças pendentes de um usuário
    List<Billing> findByUserAndStatusOrderByDueDateAsc(User user, BillingStatus status);

    // Buscar cobranças vencidas (data de vencimento passou)
    @Query("SELECT b FROM Billing b WHERE b.status = 'PENDENTE' " +
            "AND b.dueDate < :today")
    List<Billing> findOverdueBillings(@Param("today") LocalDate today);

    // Buscar cobranças a vencer em X dias
    @Query("SELECT b FROM Billing b WHERE b.status = 'PENDENTE' " +
            "AND b.dueDate BETWEEN :today AND :futureDate")
    List<Billing> findBillingsDueSoon(
            @Param("today") LocalDate today,
            @Param("futureDate") LocalDate futureDate
    );

    // Calcular total de dívidas de um usuário
    @Query("SELECT COALESCE(SUM(b.amount), 0) FROM Billing b " +
            "WHERE b.user = :user AND b.status IN ('PENDENTE', 'ATRASADA')")
    BigDecimal calculateTotalDebt(@Param("user") User user);

    // Calcular total pago por um usuário
    @Query("SELECT COALESCE(SUM(b.amount), 0) FROM Billing b " +
            "WHERE b.user = :user AND b.status = 'PAGA'")
    BigDecimal calculateTotalPaid(@Param("user") User user);

    // Buscar cobranças de um período
    @Query("SELECT b FROM Billing b WHERE b.createdAt >= :startDate " +
            "AND b.createdAt <= :endDate ORDER BY b.createdAt DESC")
    List<Billing> findByPeriod(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    // Contar cobranças pendentes de um usuário
    Long countByUserAndStatus(User user, BillingStatus status);

    // Verificar se usuário tem cobranças atrasadas
    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END " +
            "FROM Billing b WHERE b.user = :user AND b.status = 'ATRASADA'")
    boolean hasOverdueBillings(@Param("user") User user);

    // Buscar cobranças pagas em um período (para relatórios)
    @Query("SELECT b FROM Billing b WHERE b.status = 'PAGA' " +
            "AND b.paidDate BETWEEN :startDate AND :endDate")
    List<Billing> findPaidBillingsByPeriod(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}