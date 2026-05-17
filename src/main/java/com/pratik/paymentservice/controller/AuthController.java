package com.pratik.paymentservice.controller;

import com.pratik.paymentservice.dto.ApiResponse;
import com.pratik.paymentservice.entity.User;
import com.pratik.paymentservice.exception.ResourceNotFoundException;
import com.pratik.paymentservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

/**
 * REST Controller for account/balance endpoints.
 */
@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final UserRepository userRepository;

    /**
     * GET /api/accounts/me
     * Returns logged-in user's profile and balance.
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getMyAccount(
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Map<String, Object> data = Map.of(
                "id", user.getId(),
                "username", user.getUsername(),
                "email", user.getEmail(),
                "balance", user.getBalance(),
                "role", user.getRole()
        );
        return ResponseEntity.ok(ApiResponse.success("Account details", data));
    }
}
