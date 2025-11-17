package com.coworking.smartcoworking.repository;

import com.coworking.smartcoworking.entity.OccupancyLog;
import com.coworking.smartcoworking.entity.Space;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OccupancyLogRepository extends JpaRepository<OccupancyLog, Long> {

    // Buscar logs de um espaço
    List<OccupancyLog> findBySpaceOrderByTimestampDesc(Space space);

    // Buscar último log de um espaço (para saber o status atual)
    Optional<OccupancyLog> findFirstBySpaceOrderByTimestampDesc(Space space);

    // Buscar logs de um espaço em um período
    @Query("SELECT o FROM OccupancyLog o WHERE o.space = :space " +
            "AND o.timestamp BETWEEN :start AND :end " +
            "ORDER BY o.timestamp ASC")
    List<OccupancyLog> findBySpaceAndPeriod(
            @Param("space") Space space,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    // Buscar logs de ocupação (somente quando ficou ocupado)
    List<OccupancyLog> findBySpaceAndOccupiedTrueOrderByTimestampDesc(Space space);

    // Buscar logs de liberação (somente quando ficou livre)
    List<OccupancyLog> findBySpaceAndOccupiedFalseOrderByTimestampDesc(Space space);

    // Buscar espaços atualmente ocupados
    @Query("SELECT DISTINCT o.space FROM OccupancyLog o WHERE o.id IN " +
            "(SELECT MAX(ol.id) FROM OccupancyLog ol GROUP BY ol.space) " +
            "AND o.occupied = true")
    List<Space> findCurrentlyOccupiedSpaces();

    // Buscar espaços atualmente livres
    @Query("SELECT DISTINCT o.space FROM OccupancyLog o WHERE o.id IN " +
            "(SELECT MAX(ol.id) FROM OccupancyLog ol GROUP BY ol.space) " +
            "AND o.occupied = false")
    List<Space> findCurrentlyAvailableSpaces();

    // Verificar se espaço está ocupado agora
    @Query("SELECT CASE WHEN o.occupied = true THEN true ELSE false END " +
            "FROM OccupancyLog o WHERE o.space = :space " +
            "ORDER BY o.timestamp DESC LIMIT 1")
    Optional<Boolean> isSpaceCurrentlyOccupied(@Param("space") Space space);

    // Calcular tempo total de ocupação de um espaço em um período
    @Query("SELECT SUM(TIMESTAMPDIFF(MINUTE, o1.timestamp, o2.timestamp)) " +
            "FROM OccupancyLog o1, OccupancyLog o2 " +
            "WHERE o1.space = :space AND o2.space = :space " +
            "AND o1.occupied = true AND o2.occupied = false " +
            "AND o2.timestamp > o1.timestamp " +
            "AND o1.timestamp BETWEEN :start AND :end " +
            "AND o2.timestamp BETWEEN :start AND :end " +
            "AND o1.id = (SELECT MIN(o3.id) FROM OccupancyLog o3 " +
            "WHERE o3.space = :space AND o3.timestamp > o1.timestamp " +
            "AND o3.occupied = false)")
    Long calculateOccupancyMinutes(
            @Param("space") Space space,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    // Contar quantas vezes um espaço foi usado em um período
    @Query("SELECT COUNT(o) FROM OccupancyLog o WHERE o.space = :space " +
            "AND o.occupied = true AND o.timestamp BETWEEN :start AND :end")
    Long countOccupanciesByPeriod(
            @Param("space") Space space,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    // Buscar logs por reserva
    List<OccupancyLog> findByReservationOrderByTimestampAsc(
            @Param("reservation") com.coworking.smartcoworking.entity.Reservation reservation
    );
}