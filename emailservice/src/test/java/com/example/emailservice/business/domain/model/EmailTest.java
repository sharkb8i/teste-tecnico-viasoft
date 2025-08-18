package com.example.emailservice.business.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Email - Testes Unitários")
public class EmailTest {
    
    @Test
    @DisplayName("Deve criar Email válido sem lançar exceção")
    void criarEmailValido() {
        Email email = new Email(
            "destinatario@email.com", 
            "João Silva", 
            "remetente@email.com", 
            "Assunto do Email", 
            "Conteúdo do email de teste");
        
        assertEquals("destinatario@email.com", email.getRecipientEmail());
        assertEquals("João Silva", email.getRecipientName());
        assertEquals("remetente@email.com", email.getSenderEmail());
        assertEquals("Assunto do Email", email.getSubject());
        assertEquals("Conteúdo do email de teste", email.getContent());
    }

    @Test
    @DisplayName("Deve lançar NullPointerException quando algum campo for nulo")
    void criarEmailComCampoNulo() {
        assertThrows(NullPointerException.class, () ->
            new Email(null, "João Silva", "remetente@email.com", "Assunto do Email", "Conteúdo do email de teste"));
        
        assertThrows(NullPointerException.class, () ->
            new Email("destinatario@email.com", null, "remetente@email.com", "Assunto do Email", "Conteúdo do email de teste"));
        
        assertThrows(NullPointerException.class, () ->
            new Email("destinatario@email.com", "João Silva", null, "Assunto do Email", "Conteúdo do email de teste"));
        
        assertThrows(NullPointerException.class, () ->
            new Email("destinatario@email.com", "João Silva", "remetente@email.com", null, "Conteúdo do email de teste"));
        
        assertThrows(NullPointerException.class, () ->
            new Email("destinatario@email.com", "João Silva", "remetente@email.com", "Assunto do Email", null));
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException quando email do destinatário for inválido")
    void criarEmailComEmailDestinatarioInvalido() {
        assertThrows(IllegalArgumentException.class, () ->
            new Email("destinatario_invalido", "João Silva", "remetente@email.com", "Assunto do Email", "Conteúdo do email de teste"));
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException quando email do remetente for inválido")
    void criarEmailComEmailRemetenteInvalido() {
        assertThrows(IllegalArgumentException.class, () ->
            new Email("destinatario@email.com", "João Silva", "remetente_invalido", "Assunto do Email", "Conteúdo do email de teste"));
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException quando campos obrigatórios estiverem vazios")
    void criarEmailComCamposVazios() {
        assertThrows(IllegalArgumentException.class, () ->
            new Email("destinatario@email.com", "   ", "remetente@email.com", "Assunto do Email", "Conteúdo do email de teste"));
        
        assertThrows(IllegalArgumentException.class, () ->
            new Email("destinatario@email.com", "João Silva", "remetente@email.com", "   ", "Conteúdo do email de teste"));
        
        assertThrows(IllegalArgumentException.class, () ->
            new Email("destinatario@email.com", "João Silva", "remetente@email.com", "Assunto do Email", "   "));
    }

    @Test
    @DisplayName("hashCode e toString devem retornar valores corretos")
    void testarHashCodeEToString() {
        Email email = new Email(
            "destinatario@email.com", 
            "João Silva", 
            "remetente@email.com", 
            "Assunto do Email", 
            "Conteúdo do email de teste");
        
        assertNotNull(email.hashCode());
        String str = email.toString();
        assertTrue(str.contains("destinatario@email.com"));
        assertTrue(str.contains("João Silva"));
        assertTrue(str.contains("remetente@email.com"));
        assertTrue(str.contains("Assunto do Email"));
        assertTrue(str.contains("Conteúdo do email de teste"));
    }
}