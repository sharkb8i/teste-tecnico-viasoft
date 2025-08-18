package com.example.emailservice.business.exception;

/**
 * Exceção específica da camada de negócio
 * Representa violações de regras de negócio
 */
public class EmailBusinessException extends RuntimeException {
    public EmailBusinessException(String message) {
        super(message);
    }

    public EmailBusinessException(String message, Throwable cause) {
        super(message, cause);    
    }
}