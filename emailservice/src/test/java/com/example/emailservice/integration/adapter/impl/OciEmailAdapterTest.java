package com.example.emailservice.integration.adapter.impl;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.emailservice.business.domain.model.Email;
import com.example.emailservice.integration.adapter.IEmailProviderAdapter;
import com.example.emailservice.integration.dto.EmailOciDTO;
import com.example.emailservice.shared.constants.EmailConstants;
import com.example.emailservice.shared.enums.IntegrationType;

@DisplayName("OciEmailAdapter - Testes Unitários")
public class OciEmailAdapterTest {
    
    private OciEmailAdapter ociEmailAdapter;
    private Email validEmail;

    @BeforeEach
    void setup() {
        ociEmailAdapter = new OciEmailAdapter();

        validEmail = new Email(
            "destinatario@email.com", 
            "João Silva", 
            "remetente@email.com", 
            "Assunto do Email", 
            "Conteúdo do email de teste");
    }

    @Test
    @DisplayName("Deve adaptar email corretamente para EmailOciDTO")
    void adaptEmail_ComEmailValido_DeveAdaptarCorretamente() {
        EmailOciDTO resultado = ociEmailAdapter.adaptEmail(validEmail);

        assertNotNull(resultado);
        assertEquals("destinatario@email.com", resultado.getRecipientEmail());
        assertEquals("João Silva", resultado.getRecipientName());
        assertEquals("remetente@email.com", resultado.getSenderEmail());
        assertEquals("Assunto do Email", resultado.getSubject());
        assertEquals("Conteúdo do email de teste", resultado.getBody());
    }

    @Test
    @DisplayName("Deve preservar todos os dados do email original")
    void adaptEmail_DevePreservarTodosOsDados() {
        Email emailComDadosCompletos = new Email(
            "teste@exemplo.com", 
            "Maria Santos", 
            "admin@empresa.com", 
            "Teste de Adaptação", 
            "Este é um conteúdo de teste para verificar a adaptação completa");

        EmailOciDTO resultado = ociEmailAdapter.adaptEmail(emailComDadosCompletos);

        assertAll("Verificação de todos os campos",
            () -> assertEquals(emailComDadosCompletos.getRecipientEmail(), resultado.getRecipientEmail()),
            () -> assertEquals(emailComDadosCompletos.getRecipientName(), resultado.getRecipientName()),
            () -> assertEquals(emailComDadosCompletos.getSenderEmail(), resultado.getSenderEmail()),
            () -> assertEquals(emailComDadosCompletos.getSubject(), resultado.getSubject()),
            () -> assertEquals(emailComDadosCompletos.getContent(), resultado.getBody())
        );
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException quando email é nulo")
    void adaptEmail_ComEmailNulo_DeveLancarIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> ociEmailAdapter.adaptEmail(null));

        assertEquals("Email de domínio não pode ser nulo.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve retornar OCI como tipo do provedor")
    void adaptEmail_DeveRetornarOCI() {
        String providerType = ociEmailAdapter.getProviderType();

        assertEquals("OCI", providerType);
        assertEquals(IntegrationType.OCI.name(), providerType);
    }

    @Test
    @DisplayName("Deve adaptar email com caracteres especiais")
    void adaptEmail_ComCaracteresEspeciais_DeveAdaptarCorretamente() {
        Email emailComCaracteresEspeciais = new Email(
            "usuário@domínio.com", 
            "José da Silva Ção", 
            "noreply@ção.com", 
            "Assunto com acentos e çaracteres especiais!", 
            "Conteúdo com símbolos: @#$%¨&*()[]{}+=?/\\|\"';:.<>,~`^");

        EmailOciDTO resultado = ociEmailAdapter.adaptEmail(emailComCaracteresEspeciais);

        assertNotNull(resultado);
        assertEquals("usuário@domínio.com", resultado.getRecipientEmail());
        assertEquals("José da Silva Ção", resultado.getRecipientName());
        assertEquals("noreply@ção.com", resultado.getSenderEmail());
        assertEquals("Assunto com acentos e çaracteres especiais!", resultado.getSubject());
        assertEquals("Conteúdo com símbolos: @#$%¨&*()[]{}+=?/\\|\"';:.<>,~`^", resultado.getBody());

    }

    @Test
    @DisplayName("Deve adaptar email com conteúdo muito longo")
    void adaptEmail_ComConteudoLongo_DeveAdaptarCorretamente() {
        String conteudoLongo = "a".repeat(1000);
        String assuntoLongo = "b".repeat(200);
        String nomeLongo = "c".repeat(100);

        Email emailComConteudoLongo = new Email(
            "teste@email.com", 
            nomeLongo, 
            "remetente@email.com", 
            assuntoLongo, 
            conteudoLongo);

        EmailOciDTO resultado = ociEmailAdapter.adaptEmail(emailComConteudoLongo);
        
        assertNotNull(resultado);
        assertEquals("teste@email.com", resultado.getRecipientEmail());
        assertEquals(nomeLongo.substring(0, EmailConstants.OCI_RECIPIENT_NAME_MAX_LENGTH), resultado.getRecipientName());
        assertEquals("remetente@email.com", resultado.getSenderEmail());
        assertEquals(assuntoLongo.substring(0, EmailConstants.OCI_SUBJECT_MAX_LENGTH), resultado.getSubject());
        assertEquals(conteudoLongo.substring(0, EmailConstants.OCI_BODY_MAX_LENGTH), resultado.getBody());
    }

    @Test
    @DisplayName("Deve adaptar email com strings contendo apenas espaço")
    void adaptEmail_ComStringsComEspacos_DeveAdaptarCorretamente() {
        Email emailComEspacos = new Email(
            "teste@email.com", 
            "   Nome com espaços   ", 
            "remetente@email.com", 
            "   Assunto com espaços   ", 
            "   Conteúdo com espaços no início e fim   ");

        EmailOciDTO resultado = ociEmailAdapter.adaptEmail(emailComEspacos);

        assertNotNull(resultado);
        assertEquals("teste@email.com", resultado.getRecipientEmail());
        assertEquals("   Nome com espaços   ", resultado.getRecipientName());
        assertEquals("remetente@email.com", resultado.getSenderEmail());
        assertEquals("   Assunto com espaços   ", resultado.getSubject());
        assertEquals("   Conteúdo com espaços no início e fim   ", resultado.getBody());
    }

    @Test
    @DisplayName("Deve adaptar múltiplos emails mantendo consistência")
    void adaptEmail_MultiplasChamadas_DeveManterConsistencia() {
        Email email1 = new Email(
            "teste1@email.com", 
            "Nome 1", 
            "remetente1@email.com", 
            "Assunto 1", 
            "Conteúdo 1");

        Email email2 = new Email(
            "teste2@email.com", 
            "Nome 2", 
            "remetente2@email.com", 
            "Assunto 2", 
            "Conteúdo 2");
        
        Email email3 = new Email(
            "teste3@email.com", 
            "Nome 3", 
            "remetente3@email.com", 
            "Assunto 3", 
            "Conteúdo 3");
        
        EmailOciDTO resultado1 = ociEmailAdapter.adaptEmail(email1);
        EmailOciDTO resultado2 = ociEmailAdapter.adaptEmail(email2);
        EmailOciDTO resultado3 = ociEmailAdapter.adaptEmail(email3);

        assertAll("Email 1",
            () -> assertEquals("teste1@email.com", resultado1.getRecipientEmail()),
            () -> assertEquals("Nome 1", resultado1.getRecipientName()),
            () -> assertEquals("Conteúdo 1", resultado1.getBody())
        );
        assertAll("Email 2",
            () -> assertEquals("teste2@email.com", resultado2.getRecipientEmail()),
            () -> assertEquals("Nome 2", resultado2.getRecipientName()),
            () -> assertEquals("Conteúdo 2", resultado2.getBody())
        );
        assertAll("Email 3",
            () -> assertEquals("teste3@email.com", resultado3.getRecipientEmail()),
            () -> assertEquals("Nome 3", resultado3.getRecipientName()),
            () -> assertEquals("Conteúdo 3", resultado3.getBody())
        );
    }

    @Test
    @DisplayName("Deve criar nova instância de EmailOciDTO a cada chamada")
    void adaptEmail_DeveCriarNovasInstancias() {
        EmailOciDTO resultado1 = ociEmailAdapter.adaptEmail(validEmail);
        EmailOciDTO resultado2 = ociEmailAdapter.adaptEmail(validEmail);

        assertNotSame(resultado1, resultado2, "Deve criar instâncias diferentes");

        // Mas com os mesmos dados
        assertEquals(resultado1.getRecipientEmail(), resultado2.getRecipientEmail());
        assertEquals(resultado1.getRecipientName(), resultado2.getRecipientName());
        assertEquals(resultado1.getSenderEmail(), resultado2.getSenderEmail());
        assertEquals(resultado1.getSubject(), resultado2.getSubject());
        assertEquals(resultado1.getBody(), resultado2.getBody());
    }

    @Test
    @DisplayName("Deve implementar interface IEmailProviderAdapter")
    void ociEmailAdapter_DeveImplementarInterface() {
        assertTrue(ociEmailAdapter instanceof IEmailProviderAdapter,
            "ociEmailAdapter deve implementar IEmailProviderAdapter");
    }

    @Test
    @DisplayName("Deve manter imutabilidade do email original")
    void adaptEmail_DeveManterImutabilidadeDoEmailOriginal() {
        Email emailOriginal = new Email(
            "original@email.com", 
            "Nome Original", 
            "remetente@email.com", 
            "Assunto Original", 
            "Conteúdo Original");
        
        String recipientOriginal = emailOriginal.getRecipientEmail();
        String nameOriginal = emailOriginal.getRecipientName();
        String senderOriginal = emailOriginal.getSenderEmail();
        String subjectOriginal = emailOriginal.getSubject();
        String contentOriginal = emailOriginal.getContent();

        EmailOciDTO resultado = ociEmailAdapter.adaptEmail(emailOriginal);

        // Email original não deve ser modificado
        assertEquals(recipientOriginal, emailOriginal.getRecipientEmail());
        assertEquals(nameOriginal, emailOriginal.getRecipientName());
        assertEquals(senderOriginal, emailOriginal.getSenderEmail());
        assertEquals(subjectOriginal, emailOriginal.getSubject());
        assertEquals(contentOriginal, emailOriginal.getContent());

        // E o resultado deve ter os mesmos dados
        assertEquals(recipientOriginal, resultado.getRecipientEmail());
        assertEquals(nameOriginal, resultado.getRecipientName());
        assertEquals(senderOriginal, resultado.getSenderEmail());
        assertEquals(subjectOriginal, resultado.getSubject());
        assertEquals(contentOriginal, resultado.getBody());
    }
}