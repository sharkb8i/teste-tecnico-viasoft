package com.example.emailservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe principal da aplicação Spring Boot
 * Implementa arquitetura em camadas para integração de email
 * 
 * Camadas implementadas:
 * - Presentation: Controllers e DTOs de entrada
 * - Business: Lógica de negócio e entidades de domínio
 * - Integration: Adapters para provedores externos (AWS/OCI)
 * - Infrastructure: Configurações e utilitários técnicos
 * - Share: Componentes compartilhados (constantes, enums)
 */
@SpringBootApplication
public class EmailServiceRestApplication {

	private static final Logger logger = LoggerFactory.getLogger(EmailServiceRestApplication.class);

	public static void main(String[] args) {
		logger.info("Iniciando Email Service ...");
		
		SpringApplication.run(EmailServiceRestApplication.class, args);
		
		logger.info("Aplicação iniciada com sucesso!");
		logger.info("Acesse: http://localhost:8080/api/email/health");
	}
}