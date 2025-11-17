package com.coworking.smartcoworking.repository;

import com.coworking.smartcoworking.entity.Space;
import com.coworking.smartcoworking.enums.SpaceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface SpaceRepository extends JpaRepository<Space, Long> {

    // Buscar espaços por tipo
    List<Space> findByType(SpaceType type);

    // Buscar espaços ativos
    List<Space> findByActiveTrue();

    // Buscar espaços por andar
    List<Space> findByFloor(Integer floor);

    // Buscar espaços com capacidade mínima
    List<Space> findByCapacityGreaterThanEqual(Integer capacity);

    // Buscar espaços com WiFi
    List<Space> findByHasWifiTrue();

    // Buscar espaços com projetor
    List<Space> findByHasProjectorTrue();

    // Buscar por tipo e ativo
    List<Space> findByTypeAndActiveTrue(SpaceType type);

    // Buscar espaços com preço por hora menor ou igual
    List<Space> findByPricePerHourLessThanEqual(BigDecimal maxPrice);

    // Query customizada: Buscar espaços disponíveis com recursos
    @Query("SELECT s FROM Space s WHERE s.active = true AND s.type = :type " +
            "AND (:hasWifi = false OR s.hasWifi = true) " +
            "AND (:hasProjector = false OR s.hasProjector = true) " +
            "AND (:hasWhiteboard = false OR s.hasWhiteboard = true) " +
            "AND (:hasAC = false OR s.hasAC = true)")
    List<Space> findAvailableSpacesWithResources(
            @Param("type") SpaceType type,
            @Param("hasWifi") Boolean hasWifi,
            @Param("hasProjector") Boolean hasProjector,
            @Param("hasWhiteboard") Boolean hasWhiteboard,
            @Param("hasAC") Boolean hasAC
    );
}