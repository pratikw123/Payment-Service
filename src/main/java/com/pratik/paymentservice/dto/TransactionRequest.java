package com.pratik.paymentservice.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

/** DTO for initiating a payment transaction */
@Data
public class TransactionRequest {

    @NotNull(message = "Receiver ID is required")
    private Long receiverId;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;

    @Size(max = 255, message = "Description too long")
    private String description;
}
