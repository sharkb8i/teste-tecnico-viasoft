package com.example.emailservice.integration.adapter.impl;

import org.springframework.stereotype.Component;

import com.example.emailservice.business.domain.model.Email;
import com.example.emailservice.integration.adapter.IEmailProviderAdapter;
import com.example.emailservice.integration.dto.EmailAwsDTO;
import com.example.emailservice.shared.enums.IntegrationType;

/**
 * Adapter para integração com AWS SES
 * Converte entidade de domínio para formato AWS
 */
@Component("awsEmailAdapter")
public class AwsEmailAdapter implements IEmailProviderAdapter {
    
    @Override
    public EmailAwsDTO adaptEmail(Email email) {
        if (email == null) {
            throw new IllegalArgumentException("Email de domínio não pode ser nulo.");
        }

        return new EmailAwsDTO(
            email.getRecipientEmail(),
            email.getRecipientName(),
            email.getSenderEmail(),
            email.getSubject(),
            email.getContent()
        );
    }

    @Override
    public String getProviderType() {
        return IntegrationType.AWS.name();
    }
}