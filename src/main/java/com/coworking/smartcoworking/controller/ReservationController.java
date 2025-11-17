package com.coworking.smartcoworking.controller;

import com.coworking.smartcoworking.dto.reservation.CreateReservationDTO;
import com.coworking.smartcoworking.dto.reservation.ReservationResponseDTO;
import com.coworking.smartcoworking.dto.reservation.UpdateReservationDTO;
import com.coworking.smartcoworking.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationResponseDTO> create(@Valid @RequestBody CreateReservationDTO dto) {
        ReservationResponseDTO created = reservationService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponseDTO> findById(@PathVariable Long id) {
        ReservationResponseDTO reservation = reservationService.findById(id);
        return ResponseEntity.ok(reservation);
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponseDTO>> findAll() {
        List<ReservationResponseDTO> reservations = reservationService.findAll();
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReservationResponseDTO>> findByUserId(@PathVariable Long userId) {
        List<ReservationResponseDTO> reservations = reservationService.findByUserId(userId);
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/user/{userId}/upcoming")
    public ResponseEntity<List<ReservationResponseDTO>> findUpcomingByUserId(@PathVariable Long userId) {
        List<ReservationResponseDTO> reservations = reservationService.findUpcomingByUserId(userId);
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/space/{spaceId}")
    public ResponseEntity<List<ReservationResponseDTO>> findBySpaceId(@PathVariable Long spaceId) {
        List<ReservationResponseDTO> reservations = reservationService.findBySpaceId(spaceId);
        return ResponseEntity.ok(reservations);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservationResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateReservationDTO dto) {
        ReservationResponseDTO updated = reservationService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ReservationResponseDTO> cancel(@PathVariable Long id) {
        ReservationResponseDTO cancelled = reservationService.cancel(id);
        return ResponseEntity.ok(cancelled);
    }

    @PatchMapping("/{id}/check-in")
    public ResponseEntity<ReservationResponseDTO> checkIn(@PathVariable Long id) {
        ReservationResponseDTO checkedIn = reservationService.checkIn(id);
        return ResponseEntity.ok(checkedIn);
    }

    @PatchMapping("/{id}/check-out")
    public ResponseEntity<ReservationResponseDTO> checkOut(@PathVariable Long id) {
        ReservationResponseDTO checkedOut = reservationService.checkOut(id);
        return ResponseEntity.ok(checkedOut);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reservationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}