package com.example.emailservice.integration.exception;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * Exceção específica da camada de integração
 * Representa violações de regras de integração
 */
public class EmailIntegrationException extends RuntimeException {
    public EmailIntegrationException(String message) {
        super(message);
    }

    public EmailIntegrationException(String message, Throwable cause) {
        super(message, cause);    
    }
    
    public EmailIntegrationException(String message, String cause, JsonProcessingException jpe) {
        super(message, jpe);    
    }
}