package com.coworking.smartcoworking.service;

import com.coworking.smartcoworking.dto.space.CreateSpaceDTO;
import com.coworking.smartcoworking.dto.space.SpaceResponseDTO;
import com.coworking.smartcoworking.dto.space.UpdateSpaceDTO;
import com.coworking.smartcoworking.entity.Space;
import com.coworking.smartcoworking.enums.SpaceType;
import com.coworking.smartcoworking.exception.ResourceNotFoundException;
import com.coworking.smartcoworking.repository.SpaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SpaceService {

    private final SpaceRepository spaceRepository;

    @Transactional
    public SpaceResponseDTO create(CreateSpaceDTO dto) {
        // Converter DTO → Entity
        Space space = new Space();
        space.setName(dto.getName());
        space.setDescription(dto.getDescription());
        space.setType(dto.getType());
        space.setCapacity(dto.getCapacity());
        space.setPricePerHour(dto.getPricePerHour());
        space.setPricePerDay(dto.getPricePerDay());
        space.setPricePerMonth(dto.getPricePerMonth());
        space.setFloor(dto.getFloor());
        space.setHasWifi(dto.getHasWifi() != null ? dto.getHasWifi() : true);
        space.setHasProjector(dto.getHasProjector() != null ? dto.getHasProjector() : false);
        space.setHasWhiteboard(dto.getHasWhiteboard() != null ? dto.getHasWhiteboard() : false);
        space.setHasAC(dto.getHasAC() != null ? dto.getHasAC() : true);
        space.setActive(true);

        // Salvar
        Space saved = spaceRepository.save(space);

        // Retornar DTO
        return SpaceResponseDTO.fromEntity(saved);
    }

    @Transactional(readOnly = true)
    public SpaceResponseDTO findById(Long id) {
        Space space = spaceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Espaço", id));

        return SpaceResponseDTO.fromEntity(space);
    }

    @Transactional(readOnly = true)
    public List<SpaceResponseDTO> findAll() {
        return spaceRepository.findAll().stream()
                .map(SpaceResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SpaceResponseDTO> findByType(SpaceType type) {
        return spaceRepository.findByType(type).stream()
                .map(SpaceResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SpaceResponseDTO> findActiveSpaces() {
        return spaceRepository.findByActiveTrue().stream()
                .map(SpaceResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SpaceResponseDTO> findByFloor(Integer floor) {
        return spaceRepository.findByFloor(floor).stream()
                .map(SpaceResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SpaceResponseDTO> findByCapacity(Integer minCapacity) {
        return spaceRepository.findByCapacityGreaterThanEqual(minCapacity).stream()
                .map(SpaceResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public SpaceResponseDTO update(Long id, UpdateSpaceDTO dto) {
        Space space = spaceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Espaço", id));

        // Atualizar apenas campos não nulos
        if (dto.getName() != null) {
            space.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            space.setDescription(dto.getDescription());
        }
        if (dto.getCapacity() != null) {
            space.setCapacity(dto.getCapacity());
        }
        if (dto.getPricePerHour() != null) {
            space.setPricePerHour(dto.getPricePerHour());
        }
        if (dto.getPricePerDay() != null) {
            space.setPricePerDay(dto.getPricePerDay());
        }
        if (dto.getPricePerMonth() != null) {
            space.setPricePerMonth(dto.getPricePerMonth());
        }
        if (dto.getFloor() != null) {
            space.setFloor(dto.getFloor());
        }
        if (dto.getHasWifi() != null) {
            space.setHasWifi(dto.getHasWifi());
        }
        if (dto.getHasProjector() != null) {
            space.setHasProjector(dto.getHasProjector());
        }
        if (dto.getHasWhiteboard() != null) {
            space.setHasWhiteboard(dto.getHasWhiteboard());
        }
        if (dto.getHasAC() != null) {
            space.setHasAC(dto.getHasAC());
        }
        if (dto.getActive() != null) {
            space.setActive(dto.getActive());
        }

        Space updated = spaceRepository.save(space);
        return SpaceResponseDTO.fromEntity(updated);
    }

    @Transactional
    public void delete(Long id) {
        Space space = spaceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Espaço", id));

        // Validação: não permitir deletar se houver reservas ativas
        // TODO: Implementar essa validação quando necessário

        spaceRepository.delete(space);
    }

    @Transactional
    public SpaceResponseDTO activate(Long id) {
        Space space = spaceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Espaço", id));

        space.setActive(true);
        Space updated = spaceRepository.save(space);

        return SpaceResponseDTO.fromEntity(updated);
    }

    @Transactional
    public SpaceResponseDTO deactivate(Long id) {
        Space space = spaceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Espaço", id));

        space.setActive(false);
        Space updated = spaceRepository.save(space);

        return SpaceResponseDTO.fromEntity(updated);
    }
}