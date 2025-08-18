package com.example.emailservice.presentation.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.emailservice.business.domain.dto.EmailRequestDTO;
import com.example.emailservice.business.exception.EmailBusinessException;
import com.example.emailservice.business.service.EmailProcessingService;
import com.example.emailservice.integration.exception.EmailIntegrationException;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
@DisplayName("EmailController - Testes Unitários")
public class EmailControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmailProcessingService emailProcessingService;

    @Autowired
    private ObjectMapper objectMapper;

    private EmailRequestDTO validEmailRequestDTO;

    @BeforeEach
    void setup() {
        validEmailRequestDTO = new EmailRequestDTO();
        validEmailRequestDTO.setRecipientEmail("destinatario@email.com");
        validEmailRequestDTO.setRecipientName("João Silva");
        validEmailRequestDTO.setSenderEmail("remetente@email.com");
        validEmailRequestDTO.setSubject("Assunto do Email");
        validEmailRequestDTO.setContent("Conteúdo do email de teste");
        validEmailRequestDTO.validate();
    }

    @Test
    @DisplayName("Deve retornar 204 quando enviar email com dados válidos")
    void enviarEmail_ComDadosValidos_DeveRetornar204() throws Exception {
        doNothing().when(emailProcessingService).processarEmail(any(EmailRequestDTO.class));

        mockMvc.perform(post("/api/email/enviar")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validEmailRequestDTO)))
               .andExpect(status().isNoContent());
        
        verify(emailProcessingService).processarEmail(any(EmailRequestDTO.class));
    }

    @Test
    @DisplayName("Deve retornar 400 quando dados de entrada são inválidos")
    void enviarEmail_ComDadosInvalidos_DeveRetornar400() throws Exception {
        EmailRequestDTO emailInvalido = new EmailRequestDTO();
        emailInvalido.setRecipientEmail("email-invalido");
        emailInvalido.setRecipientName("");
        emailInvalido.setSenderEmail("remetente@email.com");
        emailInvalido.setSubject("Assunto do Email");
        emailInvalido.setContent("Conteúdo do email de teste");

        mockMvc.perform(post("/api/email/enviar")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(emailInvalido)))
               .andExpect(status().isBadRequest());
        
        verifyNoInteractions(emailProcessingService);
    }

    @Test
    @DisplayName("Deve retornar 400 quando corpo da requisição está vazio")
    void enviarEmail_ComCorpoVazio_DeveRetornar400() throws Exception {
        mockMvc.perform(post("/api/email/enviar")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{}"))
               .andExpect(status().isBadRequest());
        
        verifyNoInteractions(emailProcessingService);
    }

    @Test
    @DisplayName("Deve retornar 400 quando recipientEmail é nulo")
    void enviarEmail_ComRecipientEmailNulo_DeveRetornar400() throws Exception {
        validEmailRequestDTO.setRecipientEmail(null);

        mockMvc.perform(post("/api/email/enviar")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validEmailRequestDTO)))
               .andExpect(status().isBadRequest());
        
        verifyNoInteractions(emailProcessingService);
    }

    @Test
    @DisplayName("Deve retornar 400 quando senderEmail é inválido")
    void enviarEmail_ComSenderEmailInvalido_DeveRetornar400() throws Exception {
        validEmailRequestDTO.setSenderEmail("email-sem-formato-valido");

        mockMvc.perform(post("/api/email/enviar")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validEmailRequestDTO)))
               .andExpect(status().isBadRequest());
        
        verifyNoInteractions(emailProcessingService);
    }

    @Test
    @DisplayName("Deve retornar 400 quando subject está vazio")
    void enviarEmail_ComSubjectVazio_DeveRetornar400() throws Exception {
        validEmailRequestDTO.setSubject("");

        mockMvc.perform(post("/api/email/enviar")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validEmailRequestDTO)))
               .andExpect(status().isBadRequest());
        
        verifyNoInteractions(emailProcessingService);
    }

    @Test
    @DisplayName("Deve retornar 400 quando content está vazio")
    void enviarEmail_ComContentVazio_DeveRetornar400() throws Exception {
        validEmailRequestDTO.setContent("");

        mockMvc.perform(post("/api/email/enviar")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validEmailRequestDTO)))
               .andExpect(status().isBadRequest());
        
        verifyNoInteractions(emailProcessingService);
    }

    @Test
    @DisplayName("Deve retornar 400 quando lança EmailBusinessException")
    void enviarEmail_ComEmailBusinessException_DeveRetornar400() throws Exception {
        doThrow(new EmailBusinessException("Erro de negócio"))
            .when(emailProcessingService).processarEmail(any(EmailRequestDTO.class));

        mockMvc.perform(post("/api/email/enviar")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validEmailRequestDTO)))
               .andExpect(status().isBadRequest());
        
        verify(emailProcessingService).processarEmail(any(EmailRequestDTO.class));
    }

    @Test
    @DisplayName("Deve retornar 400 quando lançar EmailIntegrationException")
    void enviarEmail_ComEmailIntegrationException_DeveRetornar400() throws Exception {
        doThrow(new EmailIntegrationException("Erro de integração"))
            .when(emailProcessingService).processarEmail(any(EmailRequestDTO.class));

        mockMvc.perform(post("/api/email/enviar")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validEmailRequestDTO)))
               .andExpect(status().isBadRequest());
        
        verify(emailProcessingService).processarEmail(any(EmailRequestDTO.class));
    }

    @Test
    @DisplayName("Deve retornar 500 quando lança RuntimeException genérica")
    void enviarEmail_ComRuntimeException_DeveRetornar500() throws Exception {
        doThrow(new RuntimeException("Erro interno do serviço"))
            .when(emailProcessingService).processarEmail(any(EmailRequestDTO.class));

        mockMvc.perform(post("/api/email/enviar")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validEmailRequestDTO)))
               .andExpect(status().isInternalServerError());
        
        verify(emailProcessingService).processarEmail(any(EmailRequestDTO.class));
    }

    @Test
    @DisplayName("Deve retornar 200 para health check")
    void healthCheck_DeveRetornar200() throws Exception {
        mockMvc.perform(get("/api/email/health"))
               .andExpect(status().isOk())
               .andExpect(content().string("Email Service está funcionando!"));
    }

    @Test
    @DisplayName("Deve chamar emailProcessingService apenas uma vez com dados corretos")
    void enviarEmail_DeveInvocarServiceApenasUmaVez() throws Exception {
        doNothing().when(emailProcessingService).processarEmail(any(EmailRequestDTO.class));

        mockMvc.perform(post("/api/email/enviar")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validEmailRequestDTO)))
               .andExpect(status().isNoContent());
        
        verify(emailProcessingService, times(1)).processarEmail(any(EmailRequestDTO.class));
    }
}