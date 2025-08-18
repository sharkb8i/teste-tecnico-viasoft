package com.example.emailservice.integration.adapter;

import com.example.emailservice.business.domain.model.Email;

/**
 * Interface para adaptadores de provedores de email
 * Define o contrato para integração com diferentes provedores
 */
public interface IEmailProviderAdapter {
    
    /**
     * Adapta o email de domínio para o formato específico do provedor
     * @param email entidade de domínio
     * @return objeto adaptado para o provedor
     */
    Object adaptEmail(Email email);

    /**
     * Retorna o tipo de provedor que este adapter suporta
     * @return identificação do provedor
     */
    String getProviderType();
}