package com.example.emailservice.business.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.emailservice.business.domain.dto.EmailRequestDTO;
import com.example.emailservice.business.domain.model.Email;
import com.example.emailservice.business.domain.service.EmailDomainService;
import com.example.emailservice.business.exception.EmailBusinessException;
import com.example.emailservice.integration.service.EmailIntegrationService;

@ExtendWith(MockitoExtension.class)
@DisplayName("EmailProcessingService - Testes Unitários")
public class EmailProcessingServiceTest {
    
    @Mock
    private EmailDomainService emailDomainService;

    @Mock
    private EmailIntegrationService emailIntegrationService;

    @InjectMocks
    private EmailProcessingService emailProcessingService;

    private EmailRequestDTO validEmailRequestDTO;
    private Email emailMock;

    @BeforeEach
    void setup() {
        validEmailRequestDTO = new EmailRequestDTO();
        validEmailRequestDTO.setRecipientEmail("destinatario@email.com");
        validEmailRequestDTO.setRecipientName("João Silva");
        validEmailRequestDTO.setSenderEmail("remetente@email.com");
        validEmailRequestDTO.setSubject("Assunto do Email");
        validEmailRequestDTO.setContent("Conteúdo do email de teste");
        validEmailRequestDTO.validate();

        emailMock = new Email(
            "destinatario@email.com",
            "João Silva",
            "remetente@email.com",
            "Assunto do Email",
            "Conteúdo do email de teste"
        );
    }

    @Test
    @DisplayName("Deve processar email com sucesso quando todos os dados são válidos")
    void processarEmail_ComDadosValidos_DeveProcessarComSucesso() {
        when(emailDomainService.criarEmail(validEmailRequestDTO)).thenReturn(emailMock);
        when(emailDomainService.validarEmail(emailMock)).thenReturn(true);
        when(emailDomainService.verificarConteudoApropriado(emailMock)).thenReturn(true);
        doNothing().when(emailIntegrationService).enviarEmail(emailMock);

        assertDoesNotThrow(() -> emailProcessingService.processarEmail(validEmailRequestDTO));

        verify(emailDomainService).criarEmail(validEmailRequestDTO);
        verify(emailDomainService).validarEmail(emailMock);
        verify(emailDomainService).verificarConteudoApropriado(emailMock);
        verify(emailIntegrationService).enviarEmail(emailMock);
    }

    @Test
    @DisplayName("Deve lançar EmailBusinessException quando domainService.criarEmail lança IllegalArgumentException")
    void processarEmail_ComIllegalArgumentException_DeveLancarEmailBusinessException() {
        String errorMessage = "Dados inválidos para a criação do email";
        when(emailDomainService.criarEmail(validEmailRequestDTO))
            .thenThrow(new IllegalArgumentException(errorMessage));
        
        EmailBusinessException exception = assertThrows(EmailBusinessException.class, 
            () -> emailProcessingService.processarEmail(validEmailRequestDTO));
        
        assertEquals("Dados inválidos => " + errorMessage, exception.getMessage());
        assertTrue(exception.getCause() instanceof IllegalArgumentException);

        verify(emailDomainService).criarEmail(validEmailRequestDTO);
        verifyNoMoreInteractions(emailDomainService);
        verifyNoInteractions(emailIntegrationService);
    }

    @Test
    @DisplayName("Deve lançar EmailBusinessException quando ocorre Exception genérica")
    void processEmail_WithGenericException_MustThrowEmailBusinessException() {
        String errorMessage = "Erro inesperado no processamento";
        when(emailDomainService.criarEmail(validEmailRequestDTO)).thenReturn(emailMock);
        when(emailDomainService.validarEmail(emailMock)).thenReturn(true);
        when(emailDomainService.verificarConteudoApropriado(emailMock)).thenReturn(true);
        doThrow(new RuntimeException(errorMessage)).when(emailIntegrationService).enviarEmail(emailMock);
        
        EmailBusinessException exception = assertThrows(EmailBusinessException.class, 
            () -> emailProcessingService.processarEmail(validEmailRequestDTO));
        
        assertEquals("Erro no processamento do email: " + errorMessage, exception.getMessage());
        assertTrue(exception.getCause() instanceof RuntimeException);

        verify(emailDomainService).criarEmail(validEmailRequestDTO);
        verify(emailDomainService).validarEmail(emailMock);
        verify(emailDomainService).verificarConteudoApropriado(emailMock);
        verify(emailIntegrationService).enviarEmail(emailMock);
    }

    @Test
    @DisplayName("Deve lançar EmailBusinessException quando validação do email falha")
    void aplicarRegrasDeNegocio_ComValidacaoInvalida_DeveLancarEmailBusinessException() {
        when(emailDomainService.validarEmail(emailMock)).thenReturn(false);
        
        EmailBusinessException exception = assertThrows(EmailBusinessException.class, 
            () -> emailProcessingService.aplicarRegrasDeNegocio(emailMock));
        
        assertEquals("E-mail não atende aos critérios básicos da validação.", exception.getMessage());

        verify(emailDomainService).validarEmail(emailMock);
        verify(emailDomainService, never()).verificarConteudoApropriado(any());
    }

    @Test
    @DisplayName("Deve lançar EmailBusinessException quando conteúdo não é apropriado")
    void aplicarRegrasDeNegocio_ComConteudoInapropriado_DeveLancarEmailBusinessException() {
        when(emailDomainService.validarEmail(emailMock)).thenReturn(true);
        when(emailDomainService.verificarConteudoApropriado(emailMock)).thenReturn(false);
        
        EmailBusinessException exception = assertThrows(EmailBusinessException.class, 
            () -> emailProcessingService.aplicarRegrasDeNegocio(emailMock));
        
        assertEquals("Conteúdo do email não é apropriado.", exception.getMessage());

        verify(emailDomainService).validarEmail(emailMock);
        verify(emailDomainService).verificarConteudoApropriado(emailMock);
    }

    @Test
    @DisplayName("Deve aplicar regras de negócio com sucesso quando validações passam")
    void aplicarRegrasDeNegocio_ComValidacoesValidas_DeveAplicarComSucesso() {
        when(emailDomainService.validarEmail(emailMock)).thenReturn(true);
        when(emailDomainService.verificarConteudoApropriado(emailMock)).thenReturn(true);
        
        assertDoesNotThrow(() -> emailProcessingService.aplicarRegrasDeNegocio(emailMock));

        verify(emailDomainService).validarEmail(emailMock);
        verify(emailDomainService).verificarConteudoApropriado(emailMock);
    }

    @Test
    @DisplayName("Deve chamar os métodos na ordem correta durante o processamento")
    void processarEmail_DeveChamarMetodosNaOrdemCorreta() {
        when(emailDomainService.criarEmail(validEmailRequestDTO)).thenReturn(emailMock);
        when(emailDomainService.validarEmail(emailMock)).thenReturn(true);
        when(emailDomainService.verificarConteudoApropriado(emailMock)).thenReturn(true);
        doNothing().when(emailIntegrationService).enviarEmail(emailMock);
        
        emailProcessingService.processarEmail(validEmailRequestDTO);

        var inOrder = inOrder(emailDomainService, emailIntegrationService);
        inOrder.verify(emailDomainService).criarEmail(validEmailRequestDTO);
        inOrder.verify(emailDomainService).validarEmail(emailMock);
        inOrder.verify(emailDomainService).verificarConteudoApropriado(emailMock);
        inOrder.verify(emailIntegrationService).enviarEmail(emailMock);
    }

    @Test
    @DisplayName("Deve parar processamento na primeira falha de validação")
    void aplicarRegrasDeNegocio_DevePararNaPrimeiraFalhaDeValidacao() {
        when(emailDomainService.validarEmail(emailMock)).thenReturn(false);
        
        assertThrows(EmailBusinessException.class, 
            () -> emailProcessingService.aplicarRegrasDeNegocio(emailMock));
        
        verify(emailDomainService).validarEmail(emailMock);
        verify(emailDomainService, never()).verificarConteudoApropriado(any());
    }

    @Test
    @DisplayName("Não deve chamar integração quando regras de negócio falham")
    void processarEmail_ComFalhaRegrasNegocio_NaoDeveChamarIntegracao() {
        when(emailDomainService.criarEmail(validEmailRequestDTO)).thenReturn(emailMock);
        when(emailDomainService.validarEmail(emailMock)).thenReturn(false);
        
        assertThrows(EmailBusinessException.class, 
            () -> emailProcessingService.processarEmail(validEmailRequestDTO));
        
        verify(emailDomainService).criarEmail(validEmailRequestDTO);
        verify(emailDomainService).validarEmail(emailMock);
        verifyNoInteractions(emailIntegrationService);
    }
}