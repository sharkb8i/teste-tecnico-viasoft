package com.example.emailservice.business.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.emailservice.business.domain.dto.EmailRequestDTO;
import com.example.emailservice.business.domain.model.Email;
import com.example.emailservice.business.domain.service.EmailDomainService;
import com.example.emailservice.business.exception.EmailBusinessException;
import com.example.emailservice.integration.service.EmailIntegrationService;

/**
 * Serviço da camada de negócio responsável pelo processamento de emails
 * Coordena as operações entre o domínio e a integração
 */
@Service
public class EmailProcessingService {
    
    private static final Logger logger = LoggerFactory.getLogger(EmailProcessingService.class);

    private final EmailDomainService emailDomainService;
    private final EmailIntegrationService emailIntegrationService;

    public EmailProcessingService(EmailDomainService emailDomainService, EmailIntegrationService emailIntegrationService) {
        this.emailDomainService = emailDomainService;
        this.emailIntegrationService = emailIntegrationService;
    }

    /**
     * Processa o email aplicando regras de negócio e delegando para integração
     * @param emailRequestDTO dados do email recebidos da apresentação
     * @return EmailBusinessException em caso de violação de regras de negócio
     */
    public void processarEmail(EmailRequestDTO emailRequestDTO) {
        try {
            logger.info("Iniciando processamento de email.");

            Email email = emailDomainService.criarEmail(emailRequestDTO);
            logger.debug("Entidade de domínio criada: {}.", email);

            aplicarRegrasDeNegocio(email);

            emailIntegrationService.enviarEmail(email);

            logger.info("Email processado com sucesso.");

        } catch (IllegalArgumentException e) {
            logger.error("Erro de validação de domínio: {}.", e.getMessage());
            throw new EmailBusinessException("Dados inválidos => " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Erro no processamento do email: {}.", e.getMessage());
            throw new EmailBusinessException("Erro no processamento do email: " + e.getMessage(), e);
        }
    }

    /**
     * Aplica todas as regras de negócio ao email
     * @param email entidade de domínio
     * @return EmailBusinessException se alguma regra for violada
     */
    public void aplicarRegrasDeNegocio(Email email) {
        if(!emailDomainService.validarEmail(email)) {
            throw new EmailBusinessException("E-mail não atende aos critérios básicos da validação.");
        }

        if (!emailDomainService.verificarConteudoApropriado(email)) {
            throw new EmailBusinessException("Conteúdo do email não é apropriado.");
        }

        logger.debug("Todas as regras de negócio foram aplicadas com sucesso.");
    }
}