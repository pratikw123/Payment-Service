package com.pratik.paymentservice.service;

import com.pratik.paymentservice.dto.*;
import com.pratik.paymentservice.entity.User;
import com.pratik.paymentservice.exception.PaymentException;
import com.pratik.paymentservice.repository.UserRepository;
import com.pratik.paymentservice.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service layer handles business logic.
 * Controllers call services; services call repositories.
 * Pattern: Controller → Service → Repository → DB
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthResponse register(RegisterRequest request) {
        // Check for duplicate username/email
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new PaymentException("Username already taken: " + request.getUsername());
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new PaymentException("Email already registered: " + request.getEmail());
        }

        // Build and save user entity
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword())) // hash password
                .email(request.getEmail())
                .balance(request.getInitialBalance())
                .role("ROLE_USER")
                .build();

        userRepository.save(user);

        // Generate JWT and return
        String token = jwtUtil.generateToken(user.getUsername());
        return new AuthResponse(token, user.getUsername(), "Registration successful");
    }

    public AuthResponse login(LoginRequest request) {
        // Spring Security validates credentials against DB
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        String token = jwtUtil.generateToken(auth.getName());
        return new AuthResponse(token, auth.getName(), "Login successful");
    }
}
