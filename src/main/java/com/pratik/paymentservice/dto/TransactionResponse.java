package com.pratik.paymentservice.dto;

import com.pratik.paymentservice.entity.TransactionStatus;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/** DTO returned to client after transaction - never expose full entity */
@Data
@Builder
public class TransactionResponse {
    private Long id;
    private String senderUsername;
    private String receiverUsername;
    private BigDecimal amount;
    private TransactionStatus status;
    private String description;
    private LocalDateTime createdAt;
}
