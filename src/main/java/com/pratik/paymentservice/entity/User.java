package com.pratik.paymentservice.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Represents a user/account in the payment system.
 * @Entity maps this class to a DB table.
 * Lombok @Data auto-generates getters, setters, equals, hashCode, toString.
 */
@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password; // BCrypt hashed

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal balance;  // account balance in INR

    @Column(nullable = false)
    private String role; // "ROLE_USER" or "ROLE_ADMIN"

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist // runs automatically before INSERT into DB
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        if (this.role == null) this.role = "ROLE_USER";
        if (this.balance == null) this.balance = BigDecimal.ZERO;
    }
}
