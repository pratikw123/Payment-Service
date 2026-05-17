package com.pratik.paymentservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/** Thrown when a payment business rule is violated (e.g. insufficient balance) */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PaymentException extends RuntimeException {
    public PaymentException(String message) {
        super(message);
    }
}
