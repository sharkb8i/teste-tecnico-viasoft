package com.example.emailservice.business.domain.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.example.emailservice.business.domain.dto.EmailRequestDTO;
import com.example.emailservice.business.domain.model.Email;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;

@DisplayName("EmailDomainService - Testes Unitários")
public class EmailDomainServiceTest {
    
    private EmailDomainService emailDomainService;
    private EmailRequestDTO validEmailRequestDTO;

    @BeforeEach
    void setup() {
        emailDomainService = new EmailDomainService();

        validEmailRequestDTO = new EmailRequestDTO();
        validEmailRequestDTO.setRecipientEmail("destinatario@email.com");
        validEmailRequestDTO.setRecipientName("João Silva");
        validEmailRequestDTO.setSenderEmail("remetente@email.com");
        validEmailRequestDTO.setSubject("Assunto do Email");
        validEmailRequestDTO.setContent("Conteúdo do email de teste");
    }

    // ========================
    // TESTES PARA criarEmail()
    // ========================

    @Test
    @DisplayName("Deve criar email corretamente com dados válidos")
    void criarEmail_ComDadosValidos_DeveCriarEmailCorretamente() {
        Email email = emailDomainService.criarEmail(validEmailRequestDTO);

        assertNotNull(email);
        assertEquals("destinatario@email.com", email.getRecipientEmail());
        assertEquals("João Silva", email.getRecipientName());
        assertEquals("remetente@email.com", email.getSenderEmail());
        assertEquals("Assunto do Email", email.getSubject());
        assertEquals("Conteúdo do email de teste", email.getContent());
    }

    @DisplayName("Deve lançar exceção quando DTO tem recipientEmail nulo")
    void criarEmail_ComRecipientEmailNulo_DeveLancarExcecao() {
        EmailRequestDTO dtoComRecipientEmailNulo = new EmailRequestDTO();
        dtoComRecipientEmailNulo.setRecipientEmail(null);
        dtoComRecipientEmailNulo.setRecipientName("João Silva");
        dtoComRecipientEmailNulo.setSenderEmail("remetente@email.com");
        dtoComRecipientEmailNulo.setSubject("Assunto do Email");
        dtoComRecipientEmailNulo.setContent("Conteúdo do email de teste");

        assertThrows(NullPointerException.class,
            () -> emailDomainService.criarEmail(dtoComRecipientEmailNulo));
    }

    @DisplayName("Deve lançar exceção quando DTO tem recipientEmail vazio")
    void criarEmail_ComRecipientEmailVazio_DeveLancarExcecao() {
        EmailRequestDTO dtoComRecipientEmailVazio = new EmailRequestDTO();
        dtoComRecipientEmailVazio.setRecipientEmail("");
        dtoComRecipientEmailVazio.setRecipientName("João Silva");
        dtoComRecipientEmailVazio.setSenderEmail("remetente@email.com");
        dtoComRecipientEmailVazio.setSubject("Assunto do Email");
        dtoComRecipientEmailVazio.setContent("Conteúdo do email de teste");

        assertThrows(IllegalArgumentException.class,
            () -> emailDomainService.criarEmail(dtoComRecipientEmailVazio));
    }
    
    @DisplayName("Deve lançar exceção quando DTO tem senderEmail nulo")
    void criarEmail_ComSenderEmailNulo_DeveLancarExcecao() {
        EmailRequestDTO dtoComSenderEmailNulo = new EmailRequestDTO();
        dtoComSenderEmailNulo.setRecipientEmail("destinatario@email.com");
        dtoComSenderEmailNulo.setRecipientName("João Silva");
        dtoComSenderEmailNulo.setSenderEmail(null);
        dtoComSenderEmailNulo.setSubject("Assunto do Email");
        dtoComSenderEmailNulo.setContent("Conteúdo do email de teste");

        assertThrows(NullPointerException.class,
            () -> emailDomainService.criarEmail(dtoComSenderEmailNulo));
    }

    @DisplayName("Deve lançar exceção quando DTO tem subject nulo")
    void criarEmail_ComSubjectNulo_DeveLancarExcecao() {
        EmailRequestDTO dtoComSubjectNulo = new EmailRequestDTO();
        dtoComSubjectNulo.setRecipientEmail("destinatario@email.com");
        dtoComSubjectNulo.setRecipientName("João Silva");
        dtoComSubjectNulo.setSenderEmail("remetente@email.com");
        dtoComSubjectNulo.setSubject(null);
        dtoComSubjectNulo.setContent("Conteúdo do email de teste");

        assertThrows(NullPointerException.class,
            () -> emailDomainService.criarEmail(dtoComSubjectNulo));
    }

    @DisplayName("Deve lançar exceção quando DTO tem recipientName vazio")
    void criarEmail_ComRecipientNameVazio_DeveLancarExcecao() {
        EmailRequestDTO dtoComRecipientNameVazio = new EmailRequestDTO();
        dtoComRecipientNameVazio.setRecipientEmail("destinatario@email.com");
        dtoComRecipientNameVazio.setRecipientName("");
        dtoComRecipientNameVazio.setSenderEmail("remetente@email.com");
        dtoComRecipientNameVazio.setSubject("Assunto do Email");
        dtoComRecipientNameVazio.setContent("Conteúdo do email de teste");

        assertThrows(IllegalArgumentException.class,
            () -> emailDomainService.criarEmail(dtoComRecipientNameVazio));
    }

    // ==========================
    // TESTES PARA validarEmail()
    // ==========================

    @Test
    @DisplayName("Deve retornar true quando email é válido")
    void validarEmail_ComEmailValido_DeveRetornarTrue() {
        Email emailValido = new Email(
            "destinatario@email.com", 
            "João Silva", 
            "remetente@email.com", 
            "Assunto do Email", 
            "Conteúdo do email de teste");

        boolean resultado = emailDomainService.validarEmail(emailValido);
        
        assertTrue(resultado);
    }

    @Test
    @DisplayName("Deve retornar false quando email é nulo")
    void validarEmail_ComEmailNulo_DeveRetornarFalse() {
        boolean resultado = emailDomainService.validarEmail(null);
        
        assertFalse(resultado);
    }

    @Test
    @DisplayName("Deve lançar exceção quando email é criado com recipientEmail nulo")
    void validarEmail_ComRecipientEmailNulo_DeveLancarExcecao() {
        assertThrows(NullPointerException.class, 
            () -> {
                new Email(null, "João Silva", "remetente@email.com", "Assunto do Email", "Conteúdo do email de teste");
            });
    }

    @Test
    @DisplayName("Deve lançar exceção quando email é criado com recipientEmail vazio")
    void validarEmail_ComRecipientEmailVazio_DeveLancarExcecao() {
        assertThrows(IllegalArgumentException.class, 
            () -> {
                new Email("", "João Silva", "remetente@email.com", "Assunto do Email", "Conteúdo do email de teste");
            });
    }

    @Test
    @DisplayName("Deve lançar exceção quando email é criado com senderEmail nulo")
    void validarEmail_ComSenderEmailNulo_DeveLancarExcecao() {
        assertThrows(NullPointerException.class, 
            () -> {
                new Email("destinatario@email.com", "João Silva", null, "Assunto do Email", "Conteúdo do email de teste");
            });
    }

    @Test
    @DisplayName("Deve lançar exceção quando email é criado com senderEmail vazio")
    void validarEmail_ComSenderEmailVazio_DeveLancarExcecao() {
        assertThrows(IllegalArgumentException.class, 
            () -> {
                new Email("destinatario@email.com", "João Silva", "", "Assunto do Email", "Conteúdo do email de teste");
            });
    }
    
    // =========================================
    // TESTES PARA verificarConteudoApropriado()
    // =========================================

    @Test
    @DisplayName("Deve retornar true quando conteúdo não contém palavras proibidas")
    void verificarConteudoApropriado_ComConteudoLimpo_DeveRetornarTrue() {
        Email emailValido = new Email(
            "destinatario@email.com", 
            "João Silva", 
            "remetente@email.com", 
            "Assunto normal", 
            "Conteúdo limpo e apropriado");

        boolean resultado = emailDomainService.verificarConteudoApropriado(emailValido);
        
        assertTrue(resultado);
    }

    @ParameterizedTest
    @ValueSource(strings = {"spam", "SPAM", "Spam", "SpAm", "phishing", "PHISHING", "Phishing", "PhIsHiNg", "scam", "SCAM", "Scam", "ScAm"})
    @DisplayName("Deve retornar false quando content contém palavra proibida em qualquer caso")
    void verificarConteudoApropriado_ComPalavraProibidaNoContent_DeveRetornarFalse(String palavraProibida) {
        Email email = new Email(
            "destinatario@email.com", 
            "João Silva", 
            "remetente@email.com", 
            "Assunto normal", 
            "Este é um email com " + palavraProibida + " no conteúdo");

        boolean resultado = emailDomainService.verificarConteudoApropriado(email);
        
        assertFalse(resultado);
    }

    @ParameterizedTest
    @ValueSource(strings = {"spam", "SPAM", "Spam", "SpAm", "phishing", "PHISHING", "Phishing", "PhIsHiNg", "scam", "SCAM", "Scam", "ScAm"})
    @DisplayName("Deve retornar false quando subject contém palavra proibida em qualquer caso")
    void verificarConteudoApropriado_ComPalavraProbidaNoSubject_DeveRetornarFalse(String palavraProibida) {
        Email email = new Email(
            "destinatario@email.com", 
            "João Silva", 
            "remetente@email.com", 
            "Assunto com " + palavraProibida, 
            "Conteúdo limpo");

        boolean resultado = emailDomainService.verificarConteudoApropriado(email);
        
        assertFalse(resultado);
    }

    @ParameterizedTest
    @ValueSource(strings = {"spam", "SPAM", "Spam", "SpAm", "phishing", "PHISHING", "Phishing", "PhIsHiNg", "scam", "SCAM", "Scam", "ScAm"})
    @DisplayName("Deve retornar false quando ambos subject e content contêm palavras proibidas")
    void verificarConteudoApropriado_ComPalavrasProbidasEmAmbos_DeveRetornarFalse(String palavraProibida) {
        Email email = new Email(
            "destinatario@email.com", 
            "João Silva", 
            "remetente@email.com", 
            "Spam alert", 
            "This is a phishing attempt");

        boolean resultado = emailDomainService.verificarConteudoApropriado(email);
        
        assertFalse(resultado);
    }

    @Test
    @DisplayName("Deve retornar false quando palavras proibidas são parte de outras palavras")
    void verificarConteudoApropriado_ComPalavrasProibidasComoSubstring_DeveRetornarFalse() {
        Email email = new Email(
            "destinatario@email.com", 
            "João Silva", 
            "remetente@email.com", 
            "Recipe for scampi", 
            "Stop spamming my inbox");

        boolean resultado = emailDomainService.verificarConteudoApropriado(email);
        
        assertFalse(resultado); // contains
    }

    @Test
    @DisplayName("Deve lançar exceção quando subject é vazio")
    void verificarConteudoApropriado_ComSubjectVazio_DeveLancarExcecao() {
        assertThrows(IllegalArgumentException.class, 
            () -> {
                new Email("destinatario@email.com", "João Silva", "remetente@email.com", "", "Conteúdo do email de teste");
            });
    }

    @Test
    @DisplayName("Deve lançar exceção quando subject é nulo")
    void verificarConteudoApropriado_ComSubjectNulo_DeveLancarExcecao() {
        assertThrows(NullPointerException.class, 
            () -> {
                new Email("destinatario@email.com", "João Silva", "remetente@email.com", null, "Conteúdo do email de teste");
            });
    }

    @Test
    @DisplayName("Deve ser case insensitive para todas as palavras proibidas")
    void verificarConteudoApropriado_CaseInsentitve_DeveFuncionar() {
        Email email = new Email(
            "destinatario@email.com", 
            "João Silva", 
            "remetente@email.com", 
            "SPAM and PhIsHiNg", 
            "This is a SCAM email");
        
        boolean resultado = emailDomainService.verificarConteudoApropriado(email);

        assertFalse(resultado);
    }
}