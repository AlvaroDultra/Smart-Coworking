package com.coworking.smartcoworking.dto.space;

import com.coworking.smartcoworking.enums.SpaceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpaceResponseDTO {

    private Long id;
    private String name;
    private String description;
    private SpaceType type;
    private Integer capacity;
    private BigDecimal pricePerHour;
    private BigDecimal pricePerDay;
    private BigDecimal pricePerMonth;
    private Integer floor;
    private Boolean hasWifi;
    private Boolean hasProjector;
    private Boolean hasWhiteboard;
    private Boolean hasAC;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Método helper para conversão
    public static SpaceResponseDTO fromEntity(com.coworking.smartcoworking.entity.Space space) {
        SpaceResponseDTO dto = new SpaceResponseDTO();
        dto.setId(space.getId());
        dto.setName(space.getName());
        dto.setDescription(space.getDescription());
        dto.setType(space.getType());
        dto.setCapacity(space.getCapacity());
        dto.setPricePerHour(space.getPricePerHour());
        dto.setPricePerDay(space.getPricePerDay());
        dto.setPricePerMonth(space.getPricePerMonth());
        dto.setFloor(space.getFloor());
        dto.setHasWifi(space.getHasWifi());
        dto.setHasProjector(space.getHasProjector());
        dto.setHasWhiteboard(space.getHasWhiteboard());
        dto.setHasAC(space.getHasAC());
        dto.setActive(space.getActive());
        dto.setCreatedAt(space.getCreatedAt());
        dto.setUpdatedAt(space.getUpdatedAt());
        return dto;
    }
}
