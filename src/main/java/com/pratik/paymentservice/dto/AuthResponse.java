package com.pratik.paymentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/** Response returned after successful login - contains the JWT token */
@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String username;
    private String message;
}
