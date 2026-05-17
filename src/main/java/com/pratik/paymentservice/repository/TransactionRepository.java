package com.pratik.paymentservice.repository;

import com.pratik.paymentservice.entity.Transaction;
import com.pratik.paymentservice.entity.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Repository for Transaction entity.
 * Spring Data JPA derives SQL from method names automatically.
 * e.g. findBySenderId → SELECT * FROM transactions WHERE sender_id = ?
 */
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findBySenderId(Long senderId);
    List<Transaction> findByReceiverId(Long receiverId);
    List<Transaction> findByStatus(TransactionStatus status);

    // Custom JPQL query - gets all transactions where user is sender OR receiver
    @Query("SELECT t FROM Transaction t WHERE t.sender.id = :userId OR t.receiver.id = :userId ORDER BY t.createdAt DESC")
    List<Transaction> findAllByUserId(@Param("userId") Long userId);
}
