package com.example.emailservice.integration.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.emailservice.business.domain.model.Email;
import com.example.emailservice.infrastructure.util.JsonSerializer;
import com.example.emailservice.integration.adapter.IEmailProviderAdapter;
import com.example.emailservice.integration.dto.EmailAwsDTO;
import com.example.emailservice.integration.dto.EmailOciDTO;
import com.example.emailservice.integration.exception.EmailIntegrationException;
import com.example.emailservice.integration.factory.EmailAdapterFactory;

@ExtendWith(MockitoExtension.class)
@DisplayName("EmailIntegrationService - Testes Unitários")
public class EmailIntegrationServiceTest {
    
    @Mock
    private EmailAdapterFactory adapterFactory;

    @Mock
    private JsonSerializer jsonSerializer;

    @Mock
    private IEmailProviderAdapter emailProviderAdapter;

    private EmailIntegrationService emailIntegrationService;

    private Email emailMock;
    private EmailAwsDTO emailAwsDTO;
    private EmailOciDTO emailOciDTO;

    @BeforeEach
    void setup() {
        emailMock = new Email(
            "destinatario@email.com", 
            "João Silva", 
            "remetente@email.com", 
            "Assunto do Email",
            "Conteúdo do email de teste");
        
        emailAwsDTO = new EmailAwsDTO();
        emailAwsDTO.setRecipient("destinatario@email.com");
        emailAwsDTO.setRecipientName("João Silva");
        emailAwsDTO.setSender("remetente@email.com");
        emailAwsDTO.setSubject("Assunto do Email");
        emailAwsDTO.setContent("Conteúdo do email de teste");

        emailOciDTO = new EmailOciDTO();
        emailOciDTO.setRecipientEmail("destinatario@email.com");
        emailOciDTO.setRecipientName("João Silva");
        emailOciDTO.setSenderEmail("remetente@email.com");
        emailOciDTO.setSubject("Assunto do Email");
        emailOciDTO.setBody("Conteúdo do email de teste");
    }

    private EmailIntegrationService createService(String mailIntegracao) {
        return new EmailIntegrationService(adapterFactory, jsonSerializer, mailIntegracao);
    }

    @Test
    @DisplayName("Deve enviar email com sucesso para provedor AWS")
    void enviarEmail_ComProvedorAWS_DeveEnviarComSucesso() {
        String mailIntegracao = "AWS";
        emailIntegrationService = createService(mailIntegracao);
        String jsonEsperado = "{\"recipient\":\"destinatario@email.com\"}";

        when(adapterFactory.createAdapter(mailIntegracao)).thenReturn(emailProviderAdapter);
        when(emailProviderAdapter.adaptEmail(emailMock)).thenReturn(emailAwsDTO);
        when(jsonSerializer.serialize(emailAwsDTO)).thenReturn(jsonEsperado);

        assertDoesNotThrow(() -> emailIntegrationService.enviarEmail(emailMock));

        verify(adapterFactory).createAdapter(mailIntegracao);
        verify(emailProviderAdapter).adaptEmail(emailMock);
        verify(jsonSerializer).serialize(emailAwsDTO);
    }

    @Test
    @DisplayName("Deve enviar email com sucesso para provedor OCI")
    void enviarEmail_ComProvedorOCI_DeveEnviarComSucesso() {
        String mailIntegracao = "OCI";
        emailIntegrationService = createService(mailIntegracao);
        String jsonEsperado = "{\"recipientEmail\":\"destinatario@email.com\"}";

        when(adapterFactory.createAdapter(mailIntegracao)).thenReturn(emailProviderAdapter);
        when(emailProviderAdapter.adaptEmail(emailMock)).thenReturn(emailAwsDTO);
        when(jsonSerializer.serialize(emailAwsDTO)).thenReturn(jsonEsperado);

        assertDoesNotThrow(() -> emailIntegrationService.enviarEmail(emailMock));

        verify(adapterFactory).createAdapter(mailIntegracao);
        verify(emailProviderAdapter).adaptEmail(emailMock);
        verify(jsonSerializer).serialize(emailAwsDTO);
    }

    @Test
    @DisplayName("Deve lançar EmailIntegrationException quando adapterFactory lança exceção")
    void enviarEmail_ComErroNaFactory_DeveLancarEmailIntegrationException() {
        String mailIntegracao = "INVALID";
        emailIntegrationService = createService(mailIntegracao);
        String mensagemErro = "Provedor não suportado.";

        when(adapterFactory.createAdapter(mailIntegracao))
            .thenThrow(new RuntimeException(mensagemErro));
        
        EmailIntegrationException exception = assertThrows(EmailIntegrationException.class, 
            () -> emailIntegrationService.enviarEmail(emailMock));
        
        assertEquals("Erro na integração: " + mensagemErro, exception.getMessage());
        assertTrue(exception.getCause() instanceof RuntimeException);

        verify(adapterFactory).createAdapter(mailIntegracao);
        verifyNoInteractions(jsonSerializer);
    }

    @Test
    @DisplayName("Deve lançar EmailIntegrationException quando adapter lança exceção")
    void enviarEmail_ComErroNoAdapter_DeveLancarEmailIntegrationException() {
        String mailIntegracao = "AWS";
        emailIntegrationService = createService(mailIntegracao);
        String mensagemErro = "Erro na adaptação do email";

        when(adapterFactory.createAdapter(mailIntegracao)).thenReturn(emailProviderAdapter);
        when(emailProviderAdapter.adaptEmail(emailMock))
            .thenThrow(new RuntimeException(mensagemErro));
        
        EmailIntegrationException exception = assertThrows(EmailIntegrationException.class, 
            () -> emailIntegrationService.enviarEmail(emailMock));
        
        assertEquals("Erro na integração: " + mensagemErro, exception.getMessage());
        assertTrue(exception.getCause() instanceof RuntimeException);

        verify(adapterFactory).createAdapter(mailIntegracao);
        verify(emailProviderAdapter).adaptEmail(emailMock);
        verifyNoInteractions(jsonSerializer);
    }

    @Test
    @DisplayName("Deve lançar EmailIntegrationException quando jsonSerializer lança exceção")
    void enviarEmail_ComErroNaSerializacao_DeveLancarEmailIntegrationException() {
        String mailIntegracao = "AWS";
        emailIntegrationService = createService(mailIntegracao);
        String mensagemErro = "Erro na serialização JSON";

        when(adapterFactory.createAdapter(mailIntegracao)).thenReturn(emailProviderAdapter);
        when(emailProviderAdapter.adaptEmail(emailMock)).thenReturn(emailAwsDTO);
        when(jsonSerializer.serialize(emailAwsDTO))
            .thenThrow(new RuntimeException(mensagemErro));
        
        EmailIntegrationException exception = assertThrows(EmailIntegrationException.class, 
            () -> emailIntegrationService.enviarEmail(emailMock));
        
        assertEquals("Erro na integração: " + mensagemErro, exception.getMessage());
        assertTrue(exception.getCause() instanceof RuntimeException);

        verify(adapterFactory).createAdapter(mailIntegracao);
        verify(emailProviderAdapter).adaptEmail(emailMock);
        verify(jsonSerializer).serialize(emailAwsDTO);
    }

    @Test
    @DisplayName("Deve chamar os métodos na ordem correta durante o envio")
    void enviarEmail_DeveChamarMetodosNaOrdemCorreta() {
        String mailIntegracao = "AWS";
        emailIntegrationService = createService(mailIntegracao);
        String jsonEsperado = "{\"recipient\":\"destinatario@email.com\"}";

        when(adapterFactory.createAdapter(mailIntegracao)).thenReturn(emailProviderAdapter);
        when(emailProviderAdapter.adaptEmail(emailMock)).thenReturn(emailAwsDTO);
        when(jsonSerializer.serialize(emailAwsDTO)).thenReturn(jsonEsperado);
        
        emailIntegrationService.enviarEmail(emailMock);

        var inOrder = inOrder(adapterFactory, emailProviderAdapter, jsonSerializer);
        inOrder.verify(adapterFactory).createAdapter(mailIntegracao);
        inOrder.verify(emailProviderAdapter).adaptEmail(emailMock);
        inOrder.verify(jsonSerializer).serialize(emailAwsDTO);
    }

    @Test
    @DisplayName("Deve funcionar com diferentes tipos de integração case insensitive")
    void enviarEmail_ComDiferentesTiposIntegracao_DeveFuncionar() {
        String[] tiposIntegracao = {"aws", "AWS", "Aws", "oci", "OCI", "Oci"};

        for (String tipo : tiposIntegracao) {
            emailIntegrationService = createService(tipo);
            String jsonEsperado = "{\"test\":\"value\"}";

            when(adapterFactory.createAdapter(tipo)).thenReturn(emailProviderAdapter);
            when(emailProviderAdapter.adaptEmail(emailMock)).thenReturn(emailAwsDTO);
            when(jsonSerializer.serialize(emailAwsDTO)).thenReturn(jsonEsperado);
            
            assertDoesNotThrow(() -> emailIntegrationService.enviarEmail(emailMock),
                "Deve funcionar com tipo: " + tipo);

            reset(adapterFactory, emailProviderAdapter, jsonSerializer);
        }
    }
}