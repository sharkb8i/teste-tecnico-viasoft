package com.example.emailservice.presentation.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.emailservice.business.domain.dto.EmailRequestDTO;
import com.example.emailservice.business.service.EmailProcessingService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/email")
public class EmailController {
    
    private static final Logger logger = LoggerFactory.getLogger(EmailController.class);

    private final EmailProcessingService emailProcessingService;

    public EmailController(EmailProcessingService emailProcessingService) {
        this.emailProcessingService = emailProcessingService;
    }

    /**
     * Endpoint para envio de email
     * @param emailRequestDTO dados do email validados
     * @return ResponseEntity com status 204 em caso de sucesso
     */
    @PostMapping("/enviar")
    public ResponseEntity<Void> enviarEmail(@RequestBody EmailRequestDTO emailRequestDTO) {
        logger.info("Recebida solicitação de envio de email.");
        logger.debug("Dados do email: {}.", emailRequestDTO);

        emailRequestDTO.validate();
        emailProcessingService.processarEmail(emailRequestDTO);

        logger.info("Email enviado com sucesso.");
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint de health check
     * @return status da aplicação
     */
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Email Service está funcionando!");
    }
}