package com.example.emailservice.business.domain.model;

import java.util.Objects;

/**
 * Entidade de domínio representando um Email
 * Contém as regras de negócio e validações de domínio
 */
public class Email {
    
    private final String recipientEmail;
    private final String recipientName;
    private final String senderEmail;
    private final String subject;
    private final String content;

    public Email(String recipientEmail, String recipientName, String senderEmail, String subject, String content) {
        this.recipientEmail = Objects.requireNonNull(recipientEmail, "E-mail do destinatário não pode ser nulo.");
        this.recipientName = Objects.requireNonNull(recipientName, "Nome do destinatário não pode ser nulo.");
        this.senderEmail = Objects.requireNonNull(senderEmail, "E-mail do remetente não pode ser nulo.");
        this.subject = Objects.requireNonNull(subject, "Assunto não pode ser nulo.");
        this.content = Objects.requireNonNull(content, "Conteúdo não pode ser nulo.");

        validateEmail();
    }

    /**
     * Valida as regras de negócio do email
     */
    public void validateEmail() {
        if (!isValidEmailFormat(recipientEmail)) {
            throw new IllegalArgumentException("E-mail do destinatário inválido: " + recipientEmail);
        }

        if (!isValidEmailFormat(senderEmail)) {
            throw new IllegalArgumentException("E-mail do remetente inválido: " + senderEmail);
        }

        if (recipientName.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do destinatário não pode estar vazio.");
        }

        if (subject.trim().isEmpty()) {
            throw new IllegalArgumentException("Assunto não pode estar vazio.");
        }

        if (content.trim().isEmpty()) {
            throw new IllegalArgumentException("Conteúdo não pode estar vazio.");
        }
    }

    /**
     * Valida formato básico do email
     * @param email email a ser validado
     * @return true se válido, false caso contrário
     */
    private boolean isValidEmailFormat(String email) {
        return email != null &&
               email.contains("@") &&
               email.indexOf("@") < email.lastIndexOf(".") &&
               email.length() > 5;
    }

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public String getSubject() {
        return subject;
    }

    public String getContent() {
        return content;
    }

    @Override
    public int hashCode() {
        return Objects.hash(recipientEmail, recipientName, senderEmail, subject, content);
    }

    @Override
    public String toString() {
        return "Email{" +
            "recipientEmail='" + recipientEmail + '\'' +
            ", recipientName='" + recipientName + '\'' +
            ", senderEmail='" + senderEmail + '\'' + 
            ", subject='" + subject + '\'' +
            ", content='" + content + '\'' +
            '}';
    }
}