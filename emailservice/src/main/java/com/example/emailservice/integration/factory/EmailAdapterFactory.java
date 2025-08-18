package com.example.emailservice.integration.factory;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.emailservice.integration.adapter.IEmailProviderAdapter;
import com.example.emailservice.integration.exception.EmailIntegrationException;

/**
 * Factory de camada de integração
 * Responsável por criar adapters baseado no tipo de provedor
 */
@Component("emailAdapterFactory")
public class EmailAdapterFactory {
    
    private final Map<String, IEmailProviderAdapter> adapters;

    public EmailAdapterFactory(List<IEmailProviderAdapter> adapterList) {
        this.adapters = adapterList.stream()
            .collect(Collectors.toMap(
                IEmailProviderAdapter::getProviderType,
                Function.identity(),
                (first, second) -> second
            ));
    }

    /**
     * Cria um adapter baseado no tipo de provedor
     * @param providerType tipo do provedor (AWS, OCI)
     * @return adapter correspondente
     * @throws EmailIntegrationException se o tipo não for suportado
     */
    public IEmailProviderAdapter createAdapter(String providerType) {
        if (providerType == null || providerType.trim().isEmpty()) {
            throw new EmailIntegrationException("Tipo de provedor não pode ser nulo ou vazio");
        }

        IEmailProviderAdapter adapter = adapters.get(providerType.toUpperCase());
        if (adapter == null) {
            throw new EmailIntegrationException(
                String.format("Tipo de provedor '%s' não suportado. Tipos disponíveis: %s",
                    providerType, adapters.keySet()
                )
            );
        }

        return adapter;
    }

    /**
     * Retorna todos os tipos de provedor disponíveis
     * @return conjunto com os tipos disponíveis
     */
    public Set<String> getAvailableProviders() {
        return adapters.keySet();
    }
}