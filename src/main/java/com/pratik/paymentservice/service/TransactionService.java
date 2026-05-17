package com.pratik.paymentservice.service;

import com.pratik.paymentservice.dto.TransactionRequest;
import com.pratik.paymentservice.dto.TransactionResponse;
import com.pratik.paymentservice.entity.*;
import com.pratik.paymentservice.exception.*;
import com.pratik.paymentservice.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Core payment business logic lives here.
 * @Transactional ensures DB operations are atomic -
 * if balance deduction succeeds but credit fails, it all rolls back.
 * This is similar to how you managed data consistency in your Finacle work.
 */
@Slf4j  // adds log.info(), log.error() etc via Lombok
@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    /**
     * Process a payment from sender to receiver.
     * @Transactional = either both balance changes happen, or neither does.
     */
    @Transactional
    public TransactionResponse processTransaction(String senderUsername, TransactionRequest request) {
        // 1. Load sender
        User sender = userRepository.findByUsername(senderUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Sender not found"));

        // 2. Load receiver
        User receiver = userRepository.findById(request.getReceiverId())
                .orElseThrow(() -> new ResourceNotFoundException("Receiver not found with ID: " + request.getReceiverId()));

        // 3. Cannot send to yourself
        if (sender.getId().equals(receiver.getId())) {
            throw new PaymentException("Cannot transfer to your own account");
        }

        // 4. Create transaction record (status = PENDING)
        Transaction transaction = Transaction.builder()
                .sender(sender)
                .receiver(receiver)
                .amount(request.getAmount())
                .description(request.getDescription())
                .build();

        try {
            // 5. Check sufficient balance
            if (sender.getBalance().compareTo(request.getAmount()) < 0) {
                throw new PaymentException("Insufficient balance. Available: " + sender.getBalance());
            }

            // 6. Debit sender, credit receiver
            sender.setBalance(sender.getBalance().subtract(request.getAmount()));
            receiver.setBalance(receiver.getBalance().add(request.getAmount()));

            // 7. Save updated balances
            userRepository.save(sender);
            userRepository.save(receiver);

            // 8. Mark transaction SUCCESS
            transaction.setStatus(TransactionStatus.SUCCESS);
            log.info("Transaction SUCCESS: {} -> {} amount={}", sender.getUsername(), receiver.getUsername(), request.getAmount());

        } catch (PaymentException e) {
            // 9. Mark FAILED on business rule violation
            transaction.setStatus(TransactionStatus.FAILED);
            log.warn("Transaction FAILED: {}", e.getMessage());
            transactionRepository.save(transaction);
            throw e; // re-throw to return 400 to client
        }

        transactionRepository.save(transaction);
        return mapToResponse(transaction);
    }

    public List<TransactionResponse> getUserTransactions(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return transactionRepository.findAllByUserId(user.getId())
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public TransactionResponse getTransactionById(Long id) {
        Transaction tx = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found: " + id));
        return mapToResponse(tx);
    }

    // Map entity to DTO - never return raw entity from API
    private TransactionResponse mapToResponse(Transaction tx) {
        return TransactionResponse.builder()
                .id(tx.getId())
                .senderUsername(tx.getSender().getUsername())
                .receiverUsername(tx.getReceiver().getUsername())
                .amount(tx.getAmount())
                .status(tx.getStatus())
                .description(tx.getDescription())
                .createdAt(tx.getCreatedAt())
                .build();
    }
}
