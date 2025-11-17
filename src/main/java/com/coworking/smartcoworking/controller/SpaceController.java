package com.coworking.smartcoworking.controller;

import com.coworking.smartcoworking.dto.space.CreateSpaceDTO;
import com.coworking.smartcoworking.dto.space.SpaceResponseDTO;
import com.coworking.smartcoworking.dto.space.UpdateSpaceDTO;
import com.coworking.smartcoworking.enums.SpaceType;
import com.coworking.smartcoworking.service.SpaceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/spaces")
@RequiredArgsConstructor
public class SpaceController {

    private final SpaceService spaceService;

    @PostMapping
    public ResponseEntity<SpaceResponseDTO> create(@Valid @RequestBody CreateSpaceDTO dto) {
        SpaceResponseDTO created = spaceService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SpaceResponseDTO> findById(@PathVariable Long id) {
        SpaceResponseDTO space = spaceService.findById(id);
        return ResponseEntity.ok(space);
    }

    @GetMapping
    public ResponseEntity<List<SpaceResponseDTO>> findAll() {
        List<SpaceResponseDTO> spaces = spaceService.findAll();
        return ResponseEntity.ok(spaces);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<SpaceResponseDTO>> findByType(@PathVariable SpaceType type) {
        List<SpaceResponseDTO> spaces = spaceService.findByType(type);
        return ResponseEntity.ok(spaces);
    }

    @GetMapping("/active")
    public ResponseEntity<List<SpaceResponseDTO>> findActiveSpaces() {
        List<SpaceResponseDTO> spaces = spaceService.findActiveSpaces();
        return ResponseEntity.ok(spaces);
    }

    @GetMapping("/floor/{floor}")
    public ResponseEntity<List<SpaceResponseDTO>> findByFloor(@PathVariable Integer floor) {
        List<SpaceResponseDTO> spaces = spaceService.findByFloor(floor);
        return ResponseEntity.ok(spaces);
    }

    @GetMapping("/capacity/{minCapacity}")
    public ResponseEntity<List<SpaceResponseDTO>> findByCapacity(@PathVariable Integer minCapacity) {
        List<SpaceResponseDTO> spaces = spaceService.findByCapacity(minCapacity);
        return ResponseEntity.ok(spaces);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SpaceResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateSpaceDTO dto) {
        SpaceResponseDTO updated = spaceService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<SpaceResponseDTO> activate(@PathVariable Long id) {
        SpaceResponseDTO activated = spaceService.activate(id);
        return ResponseEntity.ok(activated);
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<SpaceResponseDTO> deactivate(@PathVariable Long id) {
        SpaceResponseDTO deactivated = spaceService.deactivate(id);
        return ResponseEntity.ok(deactivated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        spaceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}