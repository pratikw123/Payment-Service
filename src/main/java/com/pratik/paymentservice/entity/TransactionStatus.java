package com.pratik.paymentservice.entity;

/**
 * Lifecycle states of a payment transaction.
 * Mirrors real payment systems like Finacle where payments move through states.
 */
public enum TransactionStatus {
    PENDING,   // initiated, awaiting processing
    SUCCESS,   // money transferred successfully
    FAILED     // failed due to insufficient balance or error
}
