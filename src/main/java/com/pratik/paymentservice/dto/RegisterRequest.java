package com.pratik.paymentservice.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

/**
 * DTO (Data Transfer Object) for user registration requests.
 * DTOs are used to receive/send data from API - never expose entities directly.
 * Validation annotations throw 400 Bad Request automatically if violated.
 */
@Data
public class RegisterRequest {

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be 3-50 characters")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @DecimalMin(value = "0.0", message = "Initial balance cannot be negative")
    private BigDecimal initialBalance = BigDecimal.ZERO;
}
