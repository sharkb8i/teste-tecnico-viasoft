package com.example.emailservice.integration.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.emailservice.integration.adapter.IEmailProviderAdapter;
import com.example.emailservice.integration.exception.EmailIntegrationException;

@ExtendWith(MockitoExtension.class)
@DisplayName("EmailAdapterFactory - Testes Unitários")
public class EmailAdapterFactoryTest {
    
    @Mock
    private IEmailProviderAdapter awsAdapter;
    
    @Mock
    private IEmailProviderAdapter ociAdapter;

    private EmailAdapterFactory emailAdapterFactory;
    private List<IEmailProviderAdapter> adapterList;

    @BeforeEach
    void setup() {
        when(awsAdapter.getProviderType()).thenReturn("AWS");
        when(ociAdapter.getProviderType()).thenReturn("OCI");

        adapterList = Arrays.asList(awsAdapter, ociAdapter);
        emailAdapterFactory = new EmailAdapterFactory(adapterList);
    }

    @Test
    @DisplayName("Deve criar factory com sucesso quando lista de adapters válida é fornecida")
    void criarFactory_ComDadosValidos_DeveCriarComSucesso() {
        EmailAdapterFactory factory = new EmailAdapterFactory(adapterList);

        assertNotNull(factory);
        assertEquals(2, factory.getAvailableProviders().size());
        assertTrue(factory.getAvailableProviders().contains("AWS"));
        assertTrue(factory.getAvailableProviders().contains("OCI"));
    }

    @Test
    @DisplayName("Deve retornar adapter AWS quando providerType for 'AWS'")
    void retornaAdapter_ComProviderTypeAWS_DeveRetornarAwsAdapter() {
        String providerType = "AWS";
        
        IEmailProviderAdapter resultado = emailAdapterFactory.createAdapter(providerType);

        assertNotNull(resultado);
        assertEquals(awsAdapter, resultado);
        verify(awsAdapter, times(1)).getProviderType();
    }

    @Test
    @DisplayName("Deve retornar adapter OCI quando providerType for 'OCI'")
    void retornaAdapter_ComProviderTypeOCI_DeveRetornarOcidapter() {
        String providerType = "OCI";
        
        IEmailProviderAdapter resultado = emailAdapterFactory.createAdapter(providerType);

        assertNotNull(resultado);
        assertEquals(ociAdapter, resultado);
        verify(ociAdapter, times(1)).getProviderType();
    }

    @Test
    @DisplayName("Deve retornar adapter correto independente do caso do providerType")
    void retornaAdapter_ComProviderTypeCaseInsensitive_DeveRetornarAdapterCorreto() {
        assertEquals(awsAdapter, emailAdapterFactory.createAdapter("AWS"));
        assertEquals(awsAdapter, emailAdapterFactory.createAdapter("Aws"));
        assertEquals(awsAdapter, emailAdapterFactory.createAdapter("aws"));

        assertEquals(ociAdapter, emailAdapterFactory.createAdapter("OCI"));
        assertEquals(ociAdapter, emailAdapterFactory.createAdapter("Oci"));
        assertEquals(ociAdapter, emailAdapterFactory.createAdapter("oci"));
    }

    @Test
    @DisplayName("Deve lançar EmailIntegrationException quando providerType for nulo")
    void retornaAdapter_ComProviderTypeNulo_DeveLancarExcecao() {
        String providerType = null;

        EmailIntegrationException exception = assertThrows(
            EmailIntegrationException.class, 
            () -> emailAdapterFactory.createAdapter(providerType));
        
        assertEquals("Tipo de provedor não pode ser nulo ou vazio", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar EmailIntegrationException quando providerType for string vazia")
    void retornaAdapter_ComProviderTypeVazio_DeveLancarExcecao() {
        String providerType = "";

        EmailIntegrationException exception = assertThrows(
            EmailIntegrationException.class, 
            () -> emailAdapterFactory.createAdapter(providerType));
        
        assertEquals("Tipo de provedor não pode ser nulo ou vazio", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar EmailIntegrationException quando providerType for apenas espaços")
    void retornaAdapter_ComProviderTypeEmBranco_DeveLancarExcecao() {
        String providerType = "  ";

        EmailIntegrationException exception = assertThrows(
            EmailIntegrationException.class, 
            () -> emailAdapterFactory.createAdapter(providerType));
        
        assertEquals("Tipo de provedor não pode ser nulo ou vazio", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar EmailIntegrationException quando providerType não existir")
    void retornaAdapter_ComProviderTypeNaoSuportado_DeveLancarExcecao() {
        String providerType = "INVALIDO";

        EmailIntegrationException exception = assertThrows(
            EmailIntegrationException.class, 
            () -> emailAdapterFactory.createAdapter(providerType));
        
        assertTrue(exception.getMessage().contains("Tipo de provedor 'INVALIDO' não suportado"));
        assertTrue(exception.getMessage().contains("Tipos disponíveis: "));
    }

    @Test
    @DisplayName("Deve retornar conjunto de provedores disponíveis")
    void retornaProvedoresDisponiveis_DeveRetornarTodosProviders() {
        Set<String> availableProviders = emailAdapterFactory.getAvailableProviders();

        assertNotNull(availableProviders);
        assertEquals(2, availableProviders.size());
        assertTrue(availableProviders.contains("AWS"));
        assertTrue(availableProviders.contains("OCI"));
    }

    @Test
    @DisplayName("Deve criar factory vazia quando lista de adapters estiver vazia")
    void criarFactory_ComListaAdaptersVazia_DeveCriarFactoryVazia() {
        List<IEmailProviderAdapter> emptyList = Arrays.asList();

        EmailAdapterFactory factory = new EmailAdapterFactory(emptyList);

        assertNotNull(factory);
        assertTrue(factory.getAvailableProviders().isEmpty());
    }

    @Test
    @DisplayName("Deve lançar exceção quando tentar criar adapter com factory vazia")
    void retornaAdapter_ComFactoryVazia_DeveLancarExcecao() {
        List<IEmailProviderAdapter> emptyList = Arrays.asList();
        EmailAdapterFactory emptyFactory = new EmailAdapterFactory(emptyList);

        EmailIntegrationException exception = assertThrows(
            EmailIntegrationException.class, 
            () -> emptyFactory.createAdapter("AWS"));

        assertTrue(exception.getMessage().contains("Tipo de provedor 'AWS' não suportado"));
    }

    @Test
    @DisplayName("Deve manter integridade quando adapter duplicado é adicionado")
    void adicionaAdapter_ComAdapterDuplicado_DeveManterIntegridade() {
        IEmailProviderAdapter awsAdapterDuplicado = mock(IEmailProviderAdapter.class);
        when(awsAdapterDuplicado.getProviderType()).thenReturn("AWS");

        List<IEmailProviderAdapter> listaDuplicada = Arrays.asList(
            awsAdapter,
            ociAdapter,
            awsAdapterDuplicado
        );

        EmailAdapterFactory factory = new EmailAdapterFactory(listaDuplicada);
        
        assertEquals(2, factory.getAvailableProviders().size());
        // O último adapter com a mesma chave sobrescreve o anterior
        assertEquals(awsAdapterDuplicado, factory.createAdapter("AWS"));
    }
}