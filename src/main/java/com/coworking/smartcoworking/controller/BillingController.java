package com.coworking.smartcoworking.controller;

import com.coworking.smartcoworking.dto.billing.BillingResponseDTO;
import com.coworking.smartcoworking.dto.billing.CreateBillingDTO;
import com.coworking.smartcoworking.dto.billing.UpdateBillingDTO;
import com.coworking.smartcoworking.service.BillingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/billings")
@RequiredArgsConstructor
public class BillingController {

    private final BillingService billingService;

    @PostMapping
    public ResponseEntity<BillingResponseDTO> create(@Valid @RequestBody CreateBillingDTO dto) {
        BillingResponseDTO created = billingService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BillingResponseDTO> findById(@PathVariable Long id) {
        BillingResponseDTO billing = billingService.findById(id);
        return ResponseEntity.ok(billing);
    }

    @GetMapping
    public ResponseEntity<List<BillingResponseDTO>> findAll() {
        List<BillingResponseDTO> billings = billingService.findAll();
        return ResponseEntity.ok(billings);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BillingResponseDTO>> findByUserId(@PathVariable Long userId) {
        List<BillingResponseDTO> billings = billingService.findByUserId(userId);
        return ResponseEntity.ok(billings);
    }

    @GetMapping("/user/{userId}/pending")
    public ResponseEntity<List<BillingResponseDTO>> findPendingByUserId(@PathVariable Long userId) {
        List<BillingResponseDTO> billings = billingService.findPendingByUserId(userId);
        return ResponseEntity.ok(billings);
    }

    @GetMapping("/overdue")
    public ResponseEntity<List<BillingResponseDTO>> findOverdue() {
        List<BillingResponseDTO> billings = billingService.findOverdue();
        return ResponseEntity.ok(billings);
    }

    @GetMapping("/user/{userId}/debt")
    public ResponseEntity<BigDecimal> calculateTotalDebt(@PathVariable Long userId) {
        BigDecimal debt = billingService.calculateTotalDebt(userId);
        return ResponseEntity.ok(debt);
    }

    @GetMapping("/user/{userId}/paid")
    public ResponseEntity<BigDecimal> calculateTotalPaid(@PathVariable Long userId) {
        BigDecimal paid = billingService.calculateTotalPaid(userId);
        return ResponseEntity.ok(paid);
    }

    @GetMapping("/user/{userId}/summary")
    public ResponseEntity<Map<String, Object>> getUserBillingSummary(@PathVariable Long userId) {
        BigDecimal debt = billingService.calculateTotalDebt(userId);
        BigDecimal paid = billingService.calculateTotalPaid(userId);
        boolean hasOverdue = billingService.hasOverdueBillings(userId);
        List<BillingResponseDTO> pending = billingService.findPendingByUserId(userId);

        Map<String, Object> summary = Map.of(
                "totalDebt", debt,
                "totalPaid", paid,
                "hasOverdueBillings", hasOverdue,
                "pendingCount", pending.size(),
                "status", hasOverdue ? "IRREGULAR" : "REGULAR"
        );

        return ResponseEntity.ok(summary);
    }

    @GetMapping("/due-soon")
    public ResponseEntity<List<BillingResponseDTO>> findBillingsDueSoon(@RequestParam(defaultValue = "3") int days) {
        List<BillingResponseDTO> billings = billingService.findBillingsDueSoon(days);
        return ResponseEntity.ok(billings);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BillingResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateBillingDTO dto) {
        BillingResponseDTO updated = billingService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}/pay")
    public ResponseEntity<BillingResponseDTO> markAsPaid(
            @PathVariable Long id,
            @RequestParam String paymentMethod) {
        BillingResponseDTO paid = billingService.markAsPaid(id, paymentMethod);
        return ResponseEntity.ok(paid);
    }

    @PatchMapping("/{id}/mark-overdue")
    public ResponseEntity<BillingResponseDTO> markAsOverdue(@PathVariable Long id) {
        BillingResponseDTO overdue = billingService.markAsOverdue(id);
        return ResponseEntity.ok(overdue);
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<BillingResponseDTO> cancel(@PathVariable Long id) {
        BillingResponseDTO cancelled = billingService.cancel(id);
        return ResponseEntity.ok(cancelled);
    }

    @PatchMapping("/{id}/refund")
    public ResponseEntity<BillingResponseDTO> refund(@PathVariable Long id) {
        BillingResponseDTO refunded = billingService.refund(id);
        return ResponseEntity.ok(refunded);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        billingService.delete(id);
        return ResponseEntity.noContent().build();
    }
}