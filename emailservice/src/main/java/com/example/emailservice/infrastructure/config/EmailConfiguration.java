package com.example.emailservice.infrastructure.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

/**
 * Configurações da aplicação relacionadas a email
 * Centraliza as configurações da camada de infraestrutura
 */
@Configuration
public class EmailConfiguration {
    
    private static final Logger logger = LoggerFactory.getLogger(EmailConfiguration.class);

    @Value("${mail.integracao}")
    private String mailIntegracao;

    @Value("${server.port:8080}")
    private String serverPort;

    @Value("${spring.application.name:email-service}")
    private String applicationName;

    @PostConstruct
    public void logConfiguration() {
        logger.info("=== CONFIGURAÇÕES DA APLICAÇÃO ===");
        logger.info("Aplicação: {}.", applicationName);
        logger.info("Porta: {}.", serverPort);
        logger.info("Integração de Email: {}.", mailIntegracao);
        logger.info("=".repeat(34));
    }

    public String getMailIntegracao() {
        return mailIntegracao;
    }

    public String getServerPort() {
        return serverPort;
    }

    public String getApplicationName() {
        return applicationName;
    }
}
