package com.example.emailservice.infrastructure.exception;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.example.emailservice.business.exception.EmailBusinessException;
import com.example.emailservice.integration.exception.EmailIntegrationException;

@ExtendWith(MockitoExtension.class)
@DisplayName("GlobalExceptionHandler - Testes Unitários")
public class GlobalExceptionHandlerTest {
    
    private GlobalExceptionHandler globalExceptionHandler;

    @Mock
    private MethodArgumentNotValidException methodArgumentNotValidException;

    @Mock
    private BindingResult bindingResult;

    @BeforeEach()
    void setup() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    @DisplayName("Deve tratar EmailBusinessException e retornar BAD_REQUEST com estrutura correta")
    void tratarExcecao_ComEmailBusinessException_DeveRetornarBadRequest() {
        String errorMessage = "Erro de negócio";
        EmailBusinessException exception = new EmailBusinessException(errorMessage);

        ResponseEntity<Map<String, Object>> response = globalExceptionHandler
            .handleEmailBusinessException(exception);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());

        Map<String, Object> body = response.getBody();
        assertEquals(400, body.get("status"));
        assertEquals("BUSINESS_ERROR", body.get("error"));
        assertEquals(errorMessage, body.get("message"));
        assertNotNull(body.get("timestamp"));
        assertTrue(body.get("timestamp").toString().contains(LocalDateTime.now().getYear() + ""));
    }

    @Test
    @DisplayName("Deve tratar EmailIntegrationException e retornar BAD_REQUEST com estrutura correta")
    void tratarExcecao_ComEmailIntegrationException_DeveRetornarBadRequest() {
        String errorMessage = "Erro de integração";
        EmailIntegrationException exception = new EmailIntegrationException(errorMessage);

        ResponseEntity<Map<String, Object>> response = globalExceptionHandler
            .handleEmailIntegrationException(exception);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());

        Map<String, Object> body = response.getBody();
        assertEquals(400, body.get("status"));
        assertEquals("INTEGRATION_ERROR", body.get("error"));
        assertEquals(errorMessage, body.get("message"));
        assertNotNull(body.get("timestamp"));
    }

    @Test
    @DisplayName("Deve tratar MethodArgumentNotValidException e formatar erros de validação corretamente")
    void tratarExcecao_ComValidationException_DeveFormatarFieldErrors() {
        FieldError emailError = new FieldError("emailRequestDTO", "recipientEmail", "deve ser um endereço de e-mail válido");
        FieldError nameError = new FieldError("emailRequestDTO", "recipientName", "não pode estar em branco");

        List<FieldError> fieldErrors = Arrays.asList(emailError, nameError);

        when(methodArgumentNotValidException.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(fieldErrors);

        ResponseEntity<Map<String, Object>> response = globalExceptionHandler
            .handleValidationException(methodArgumentNotValidException);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());

        Map<String, Object> body = response.getBody();
        assertEquals(400, body.get("status"));
        assertEquals("VALIDATION_ERROR", body.get("error"));

        String message = (String) body.get("message");
        assertTrue(message.startsWith("Dados inválidos => "));
        assertTrue(message.contains("recipientEmail: deve ser um endereço de e-mail válido"));
        assertTrue(message.contains("recipientName: não pode estar em branco"));
        assertNotNull(body.get("timestamp"));
    }

    @Test
    @DisplayName("Deve tratar MethodArgumentNotValidException com lista vazia de erros")
    void tratarExcecao_ComValidationExceptionSemFieldErrors_DeveTratarCorretamente() {
        when(methodArgumentNotValidException.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(Arrays.asList());

        ResponseEntity<Map<String, Object>> response = globalExceptionHandler
            .handleValidationException(methodArgumentNotValidException);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());

        Map<String, Object> body = response.getBody();
        assertEquals("Dados inválidos => ", body.get("message"));
    }

    @Test
    @DisplayName("Deve tratar IllegalArgumentException e retornar BAD_REQUEST")
    void tratarExcecao_ComIllegalArgumentException_DeveRetornarBadRequest() {
        String errorMessage = "Argumento inválido fornecido";
        IllegalArgumentException exception = new IllegalArgumentException(errorMessage);

        ResponseEntity<Map<String, Object>> response = globalExceptionHandler
            .handleIllegalArgumentException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());

        Map<String, Object> body = response.getBody();
        assertEquals(400, body.get("status"));
        assertEquals("DOMAIN_VALIDATION_ERROR", body.get("error"));
        assertEquals(errorMessage, body.get("message"));
        assertNotNull(body.get("timestamp"));
    }

    @Test
    @DisplayName("Deve tratar Exception genérica e retornar INTERNAL_SERVER_ERROR")
    void tratarExcecao_ComExceptionGenerica_DeveRetornarInternalServerError() {
        String errorMessage = "Erro inesperado do sistema";
        RuntimeException exception = new RuntimeException(errorMessage);

        ResponseEntity<Map<String, Object>> response = globalExceptionHandler
            .handleGenericException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());

        Map<String, Object> body = response.getBody();
        assertEquals(500, body.get("status"));
        assertEquals("INTERNAL_SERVER_ERROR", body.get("error"));
        assertEquals("Erro interno do servidor", body.get("message"));
        assertNotNull(body.get("timestamp"));
    }

    @Test
    @DisplayName("Deve tratar NullPointerException como erro genérico")
    void tratarExcecao_ComNullPointerException_DeveTratarComoErroGenerico() {
        NullPointerException exception = new NullPointerException("Referência nula encontrada");

        ResponseEntity<Map<String, Object>> response = globalExceptionHandler
            .handleGenericException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());

        Map<String, Object> body = response.getBody();
        assertEquals("INTERNAL_SERVER_ERROR", body.get("error"));
        assertEquals("Erro interno do servidor", body.get("message"));
    }

    @Test
    @DisplayName("Deve criar estrutura de resposta de erro consistente")
    void criarRespostaErro_Sempre_DeveManterEstruturaConsistente() {
        String errorMessage = "Mensagem de teste";
        EmailBusinessException exception = new EmailBusinessException(errorMessage);

        ResponseEntity<Map<String, Object>> response = globalExceptionHandler
            .handleEmailBusinessException(exception);

        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals(4, body.size());
        
        assertTrue(body.containsKey("timestamp"));
        assertTrue(body.containsKey("error"));
        assertTrue(body.containsKey("status"));
        assertTrue(body.containsKey("message"));

        assertInstanceOf(String.class, body.get("timestamp"));
        assertInstanceOf(String.class, body.get("error"));
        assertInstanceOf(Integer.class, body.get("status"));
        assertInstanceOf(String.class, body.get("message"));
    }

    @Test
    @DisplayName("Deve gerar timestamp válido em todas as respostas de erro")
    void criarRespostaErro_Sempre_DeveGerarTimestampValido() {
        LocalDateTime beforeTest = LocalDateTime.now().minusSeconds(1);

        ResponseEntity<Map<String, Object>> businessResponse = globalExceptionHandler
            .handleEmailBusinessException(new EmailBusinessException("teste"));

        ResponseEntity<Map<String, Object>> integrationResponse = globalExceptionHandler
            .handleEmailIntegrationException(new EmailIntegrationException("teste"));
        
        ResponseEntity<Map<String, Object>> argumentResponse = globalExceptionHandler
            .handleIllegalArgumentException(new IllegalArgumentException("teste"));
        
        ResponseEntity<Map<String, Object>> genericResponse = globalExceptionHandler
            .handleGenericException(new RuntimeException("teste"));
        
        LocalDateTime afterTest = LocalDateTime.now().plusSeconds(1);

        String businessTimestamp = (String) businessResponse.getBody().get("timestamp");
        String integrationTimestamp = (String) integrationResponse.getBody().get("timestamp");
        String argumentTimestamp = (String) argumentResponse.getBody().get("timestamp");
        String genericTimestamp = (String) genericResponse.getBody().get("timestamp");

        LocalDateTime businessTime = LocalDateTime.parse(businessTimestamp);
        LocalDateTime integrationTime = LocalDateTime.parse(integrationTimestamp);
        LocalDateTime argumentTime = LocalDateTime.parse(argumentTimestamp);
        LocalDateTime genericTime = LocalDateTime.parse(genericTimestamp);

        assertTrue(businessTime.isAfter(beforeTest) && businessTime.isBefore(afterTest));
        assertTrue(integrationTime.isAfter(beforeTest) && integrationTime.isBefore(afterTest));
        assertTrue(argumentTime.isAfter(beforeTest) && argumentTime.isBefore(afterTest));
        assertTrue(genericTime.isAfter(beforeTest) && genericTime.isBefore(afterTest));
    }

    @Test
    @DisplayName("Deve tratar mensagens de exceção nulas sem falhar")
    void tratarExcecao_ComMensagemNula_DeveTratarSemFalhar() {
        EmailBusinessException businessException = new EmailBusinessException(null);
        EmailIntegrationException integrationException = new EmailIntegrationException(null);
        IllegalArgumentException argumentException = new IllegalArgumentException((String) null);
        RuntimeException genericException = new RuntimeException((String) null);

        assertDoesNotThrow(() ->
            globalExceptionHandler.handleEmailBusinessException(businessException));
        assertDoesNotThrow(() -> 
            globalExceptionHandler.handleEmailIntegrationException(integrationException));
        assertDoesNotThrow(() -> 
            globalExceptionHandler.handleIllegalArgumentException(argumentException));
        assertDoesNotThrow(() -> 
            globalExceptionHandler.handleGenericException(genericException));
    }
}