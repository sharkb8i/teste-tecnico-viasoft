package com.example.emailservice.integration.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.emailservice.business.domain.model.Email;
import com.example.emailservice.infrastructure.util.JsonSerializer;
import com.example.emailservice.integration.adapter.IEmailProviderAdapter;
import com.example.emailservice.integration.exception.EmailIntegrationException;
import com.example.emailservice.integration.factory.EmailAdapterFactory;

/**
 * Serviço da camada de integração
 * Responsável por coordenar a comunicação com provedores externos
 */
@Service
public class EmailIntegrationService {
    
    private static final Logger logger = LoggerFactory.getLogger(EmailIntegrationService.class);

    private final EmailAdapterFactory adapterFactory;
    private final JsonSerializer jsonSerializer;
    private final String mailIntegracao;

    public EmailIntegrationService(EmailAdapterFactory adapterFactory,
                                   JsonSerializer jsonSerializer,
                                   @Value("${mail.integracao}") String mailIntegracao) {
        this.adapterFactory = adapterFactory;
        this.jsonSerializer = jsonSerializer;
        this.mailIntegracao = mailIntegracao;
    }

    /**
     * Envia o email através do provedor configurado
     * @param email entidade de domínio
     * @return EmailIntegrationException em caso de erro na integração
     */
    public void enviarEmail(Email email) {
        try {
            logger.info("Iniciando integração com provedor: {}.", mailIntegracao);

            IEmailProviderAdapter adapter = adapterFactory.createAdapter(mailIntegracao);

            Object emailAdaptado = adapter.adaptEmail(email);

            String jsonEmail = jsonSerializer.serialize(emailAdaptado);
            imprimirEmailSerializado(jsonEmail, mailIntegracao);

            logger.info("Email integrado com sucesso através do provedor: {}.", mailIntegracao);
        } catch (Exception e) {
            logger.error("Erro na integração com o provedor {}: {}.", mailIntegracao, e.getMessage());
            throw new EmailIntegrationException("Erro na integração: " + e.getMessage(), e);
        }
    }

    /**
     * Imprime o email serializado no console
     * @param jsonEmail JSON do email
     * @param tipoIntegracao tipo de integração utilizada
     */
    public void imprimirEmailSerializado(String jsonEmail, String tipoIntegracao) {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("EMAIL SERIALIZADO PARA INTEGRAÇÃO: " + tipoIntegracao.toUpperCase());
        System.out.println("=".repeat(80));
        System.out.println(jsonEmail);
        System.out.println("=".repeat(80) + "\n");
    }
}