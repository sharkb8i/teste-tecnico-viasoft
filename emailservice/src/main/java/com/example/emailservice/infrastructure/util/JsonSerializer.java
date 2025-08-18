package com.example.emailservice.infrastructure.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.example.emailservice.integration.exception.EmailIntegrationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Utilitário da camada de infraestrutura para serialização JSON
 * Centraliza a lógica de serialização
 */
@Component("jsonSerializer")
public class JsonSerializer {
    
    private static final Logger logger = LoggerFactory.getLogger(JsonSerializer.class);

    private final ObjectMapper objectMapper;

    public JsonSerializer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Serializa um objeto para JSON formatado
     * @param objeto objeto a ser serializado
     * @return string JSON formatada
     * @throws EmailIntegrationException em caso de erro na serialização
     */
    public String serialize(Object objeto) {
        try {
            String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(objeto);
            logger.debug("Objeto serializado com sucesso.");
            return json;
        } catch (JsonProcessingException e) {
            logger.error("Erro ao serializar objeto para JSON: {}.", e.getMessage());
            throw new EmailIntegrationException("Erro na serialização JSON: ", e.getMessage(), e);
        }
    }

    /**
     * Serializa um objeto para JSON compacto (sem formatação)
     * @param objeto objeto a ser serializado
     * @return string JSON compacta
     * @throws EmailIntegrationException em caso de erro na serialização
     */
    public String serializeCompact(Object objeto) {
        try {
            String json = objectMapper.writeValueAsString(objeto);
            logger.debug("Objeto serializado em formato compacto com sucesso.");
            return json;
        } catch (JsonProcessingException e) {
            logger.error("Erro ao serializar objeto para JSON compacto: {}.", e.getMessage());
            throw new EmailIntegrationException("Erro na serialização JSON compacto: ", e.getMessage(), e);
        }
    }
}