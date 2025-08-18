package com.example.emailservice.integration.adapter.impl;

import org.springframework.stereotype.Component;

import com.example.emailservice.business.domain.model.Email;
import com.example.emailservice.integration.adapter.IEmailProviderAdapter;
import com.example.emailservice.integration.dto.EmailOciDTO;
import com.example.emailservice.shared.enums.IntegrationType;

/**
 * Adapter para integração com OCI
 * Converte entidade de domínio para formato OCI
 */
@Component("ociEmailAdapter")
public class OciEmailAdapter implements IEmailProviderAdapter {
    
    @Override
    public EmailOciDTO adaptEmail(Email email) {
        if (email == null) {
            throw new IllegalArgumentException("Email de domínio não pode ser nulo.");
        }

        return new EmailOciDTO(
            email.getRecipientEmail(),
            email.getRecipientName(),
            email.getSenderEmail(),
            email.getSubject(),
            email.getContent()
        );
    }

    @Override
    public String getProviderType() {
        return IntegrationType.OCI.name();
    }
}