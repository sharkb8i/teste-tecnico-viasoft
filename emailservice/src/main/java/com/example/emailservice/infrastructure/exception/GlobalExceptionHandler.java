package com.example.emailservice.infrastructure.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.emailservice.business.exception.EmailBusinessException;
import com.example.emailservice.integration.exception.EmailIntegrationException;

/**
 * Handler global da camada de infraestrutura
 * Trata exceções de todas as camadas da aplicação
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Trata as exceções da camada de negócio
     */
    @ExceptionHandler(EmailBusinessException.class)
    public ResponseEntity<Map<String, Object>> handleEmailBusinessException(EmailBusinessException ex) {
        logger.error("Erro de negócio: {}.", ex.getMessage());

        Map<String, Object> errorResponse = createErrorResponse(
            "BUSINESS_ERROR",
            ex.getMessage(),
            HttpStatus.BAD_REQUEST.value()
        );

        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * Trata as exceções da camada de integração
     */
    @ExceptionHandler(EmailIntegrationException.class)
    public ResponseEntity<Map<String, Object>> handleEmailIntegrationException(EmailIntegrationException ex) {
        logger.error("Erro de integração: {}.", ex.getMessage());

        Map<String, Object> errorResponse = createErrorResponse(
            "INTEGRATION_ERROR",
            ex.getMessage(),
            HttpStatus.BAD_REQUEST.value()
        );

        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * Trata as exceções de validação da camada de apresentação
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException ex) {
        logger.error("Erro de validação: {}.", ex.getMessage());

        String validationErrors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .collect(Collectors.joining(", "));

        Map<String, Object> errorResponse = createErrorResponse(
            "VALIDATION_ERROR",
            "Dados inválidos => " + validationErrors,
            HttpStatus.BAD_REQUEST.value()
        );

        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * Trata as exceções de argumento ilegal (camada de domínio)
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        logger.error("Argumento ilegal: {}.", ex.getMessage());

        Map<String, Object> errorResponse = createErrorResponse(
            "DOMAIN_VALIDATION_ERROR",
            ex.getMessage(),
            HttpStatus.BAD_REQUEST.value()
        );

        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * Trata as exceções gerais 
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        logger.error("Erro interno do servidor: {}.", ex.getMessage(), ex);

        Map<String, Object> errorResponse = createErrorResponse(
            "INTERNAL_SERVER_ERROR",
            "Erro interno do servidor",
            HttpStatus.INTERNAL_SERVER_ERROR.value()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    /**
     * Cria um mapa de resposta de erro padronizado
     */
    private Map<String, Object> createErrorResponse(String errorType, String message, int status) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now().toString());
        errorResponse.put("status", status);
        errorResponse.put("error", errorType);
        errorResponse.put("message", message);
        return errorResponse;
    }
}