package com.coworking.smartcoworking.repository;

import com.coworking.smartcoworking.entity.Reservation;
import com.coworking.smartcoworking.entity.Space;
import com.coworking.smartcoworking.entity.User;
import com.coworking.smartcoworking.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    // Buscar reservas por usuário
    List<Reservation> findByUser(User user);

    // Buscar reservas por usuário e status
    List<Reservation> findByUserAndStatus(User user, ReservationStatus status);

    // Buscar reservas por espaço
    List<Reservation> findBySpace(Space space);

    // Buscar reservas por espaço e status
    List<Reservation> findBySpaceAndStatus(Space space, ReservationStatus status);

    // Buscar reservas por status
    List<Reservation> findByStatus(ReservationStatus status);

    // Buscar reservas ativas (CONFIRMADA ou EM_USO)
    @Query("SELECT r FROM Reservation r WHERE r.status IN ('CONFIRMADA', 'EM_USO')")
    List<Reservation> findActiveReservations();

    // Buscar reservas de um usuário ordenadas por data
    List<Reservation> findByUserOrderByStartDateTimeDesc(User user);

    // Verificar se existe conflito de horário para um espaço
    @Query("SELECT r FROM Reservation r WHERE r.space = :space " +
            "AND r.status NOT IN ('CANCELADA', 'CONCLUIDA', 'EXPIRADA') " +
            "AND ((r.startDateTime < :endDateTime AND r.endDateTime > :startDateTime))")
    List<Reservation> findConflictingReservations(
            @Param("space") Space space,
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime
    );

    // Buscar reservas de um espaço em um período
    @Query("SELECT r FROM Reservation r WHERE r.space = :space " +
            "AND r.startDateTime >= :start AND r.endDateTime <= :end " +
            "ORDER BY r.startDateTime")
    List<Reservation> findBySpaceAndDateRange(
            @Param("space") Space space,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    // Buscar reservas que deveriam começar mas não tiveram check-in
    @Query("SELECT r FROM Reservation r WHERE r.status = 'CONFIRMADA' " +
            "AND r.startDateTime < :now AND r.checkInTime IS NULL")
    List<Reservation> findExpiredReservations(@Param("now") LocalDateTime now);

    // Buscar reservas em uso (com check-in mas sem check-out)
    @Query("SELECT r FROM Reservation r WHERE r.status = 'EM_USO' " +
            "AND r.checkInTime IS NOT NULL AND r.checkOutTime IS NULL")
    List<Reservation> findReservationsInUse();

    // Contar reservas de um usuário por status
    Long countByUserAndStatus(User user, ReservationStatus status);

    // Buscar próximas reservas de um usuário
    @Query("SELECT r FROM Reservation r WHERE r.user = :user " +
            "AND r.status = 'CONFIRMADA' AND r.startDateTime > :now " +
            "ORDER BY r.startDateTime ASC")
    List<Reservation> findUpcomingReservations(
            @Param("user") User user,
            @Param("now") LocalDateTime now
    );
}