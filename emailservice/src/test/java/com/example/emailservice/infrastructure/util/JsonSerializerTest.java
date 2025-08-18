package com.example.emailservice.infrastructure.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.emailservice.integration.exception.EmailIntegrationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("JsonSerializer - Testes Unitários")
public class JsonSerializerTest {
    
    @Mock
    private ObjectMapper objectMapper;

    private JsonSerializer jsonSerializer;

    @BeforeEach
    void setup() {
        jsonSerializer = new JsonSerializer(objectMapper);
    }

    @Test
    @DisplayName("Deve serializar objeto em JSON formatado corretamente")
    void serialize_DeveRetornarJsonFormatado_QuandoObjetoValido() throws Exception {
        Object obj = new Object();
        String expectedJson = "{\n  \"key\": \"value\"\n}";

        ObjectWriter objectWriter = mock(ObjectWriter.class);
        when(objectMapper.writerWithDefaultPrettyPrinter()).thenReturn(objectWriter);
        when(objectWriter.writeValueAsString(any(Object.class))).thenReturn(expectedJson);
        
        String json = jsonSerializer.serialize(obj);

        assertEquals(expectedJson, json);
        verify(objectWriter).writeValueAsString(obj);
    }

    @Test
    @DisplayName("Deve serializar objeto em JSON compacto corretamente")
    void serializeCompact_DeveRetornarJsonCompactoFormatado() throws Exception {
        Object obj = new Object();
        String expectedJson = "{\"key\":\"value\"}";

        when(objectMapper.writeValueAsString(obj)).thenReturn(expectedJson);

        String json = jsonSerializer.serializeCompact(obj);

        assertEquals(expectedJson, json);
        verify(objectMapper).writeValueAsString(obj);
    }

    @Test
    @DisplayName("Deve lançar EmailIntegrationException ao falhar na serialização formatada")
    void serialize_QuandoJsonProcessingException_DeveLancarEmailIntegrationException() throws Exception {
        Object obj = new Object();

        ObjectWriter objectWriter = mock(ObjectWriter.class);

        when(objectMapper.writerWithDefaultPrettyPrinter()).thenReturn(objectWriter);
        when(objectWriter.writeValueAsString(any(Object.class)))
            .thenThrow(new JsonProcessingException("Erro!") {});

        EmailIntegrationException exception = assertThrows(EmailIntegrationException.class, 
            () -> jsonSerializer.serialize(obj));
        
        assertTrue(exception.getMessage().contains("Erro na serialização JSON"));
    }

    @Test
    @DisplayName("Deve lançar EmailIntegrationException ao falhar na serialização compacta")
    void serializeCompact_QuandoJsonProcessingException_DeveLancarEmailIntegrationException() throws Exception {
        Object obj = new Object();
        when(objectMapper.writeValueAsString(obj)).thenThrow(new JsonProcessingException("Erro!") {});

        EmailIntegrationException exception = assertThrows(EmailIntegrationException.class, 
            () -> jsonSerializer.serializeCompact(obj));
        
        assertTrue(exception.getMessage().contains("Erro na serialização JSON compacto"));
    }
}