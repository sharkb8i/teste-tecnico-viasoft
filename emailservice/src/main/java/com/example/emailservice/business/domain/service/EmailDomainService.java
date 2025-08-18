package com.example.emailservice.business.domain.service;

import org.springframework.stereotype.Service;

import com.example.emailservice.business.domain.dto.EmailRequestDTO;
import com.example.emailservice.business.domain.model.Email;

/**
 * Serviço de domínio responsável por operações relacionadas à entidade Email
 * Contém lógicas específicas do domínio
 */
@Service
public class EmailDomainService {
    
    /**
     * Cria uma entidade de domínio Email a partir do DTO da apresentação
     * @param emailRequestDTO dados recebidos na camada de apresentação
     * @return entidade Email validada
     */
    public Email criarEmail(EmailRequestDTO emailRequestDTO) {
        return new Email(
            emailRequestDTO.getRecipientEmail(),
            emailRequestDTO.getRecipientName(),
            emailRequestDTO.getSenderEmail(),
            emailRequestDTO.getSubject(),
            emailRequestDTO.getContent()
        );
    }

    /**
     * Valida se o email atende aos critérios de negócio
     * @param email entidade de email a ser validada
     * @return true se válido
     */
    public boolean validarEmail(Email email) {
        return email != null &&
               !email.getRecipientEmail().isEmpty() &&
               !email.getSenderEmail().isEmpty();
    }

    /**
     * Verifica se o conteúdo do email é apropriado
     * @param email entidade de email
     * @return true se o conteúdo é apropriado
     */
    public boolean verificarConteudoApropriado(Email email) {
        String content = email.getContent().toLowerCase();
        String subject = email.getSubject().toLowerCase();

        String[] forbiddenWords = { "spam", "phishing", "scam" };

        for (String word : forbiddenWords) {
            if (content.contains(word) || subject.contains(word)) {
                return false;
            }
        }

        return true;
    }
}