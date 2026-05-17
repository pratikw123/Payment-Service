package com.pratik.paymentservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/** DTO for login - just username and password */
@Data
public class LoginRequest {
    @NotBlank private String username;
    @NotBlank private String password;
}
