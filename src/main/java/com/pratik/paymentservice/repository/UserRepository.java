package com.pratik.paymentservice.repository;

import com.pratik.paymentservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Spring Data JPA repository for User entity.
 * JpaRepository<User, Long> gives you free CRUD methods:
 * save(), findById(), findAll(), deleteById(), count(), etc.
 * You just declare method signatures - Spring generates the SQL.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
