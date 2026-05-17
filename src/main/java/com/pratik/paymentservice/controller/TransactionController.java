package com.pratik.paymentservice.controller;

import com.pratik.paymentservice.dto.*;
import com.pratik.paymentservice.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * REST Controller for payment transaction endpoints.
 * All endpoints here require JWT authentication (configured in SecurityConfig).
 * @AuthenticationPrincipal injects the currently logged-in user from JWT.
 */
@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    /**
     * POST /api/transactions
     * Initiate a new payment. Sender is extracted from JWT token.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<TransactionResponse>> createTransaction(
            @Valid @RequestBody TransactionRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        TransactionResponse response = transactionService.processTransaction(
                userDetails.getUsername(), request);
        return ResponseEntity.ok(ApiResponse.success("Transaction processed", response));
    }

    /**
     * GET /api/transactions
     * Get all transactions for the logged-in user (sent + received).
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<TransactionResponse>>> getMyTransactions(
            @AuthenticationPrincipal UserDetails userDetails) {
        List<TransactionResponse> transactions = transactionService.getUserTransactions(
                userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success("Transactions retrieved", transactions));
    }

    /**
     * GET /api/transactions/{id}
     * Get a specific transaction by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TransactionResponse>> getTransaction(@PathVariable Long id) {
        TransactionResponse response = transactionService.getTransactionById(id);
        return ResponseEntity.ok(ApiResponse.success("Transaction found", response));
    }
}
