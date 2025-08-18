package com.example.emailservice.business.domain.dto;

/**
 * DTO para recebimento de dados na camada de apresentação
 */
public class EmailRequestDTO {
    private String recipientEmail;
    private String recipientName;
    private String senderEmail;
    private String subject;
    private String content;

    public EmailRequestDTO() { }

    public EmailRequestDTO(String recipientEmail, String recipientName, String senderEmail, String subject, String content) {
        this.recipientEmail = recipientEmail;
        this.recipientName = recipientName;
        this.senderEmail = senderEmail;
        this.subject = subject;
        this.content = content;
        this.validate();
    }

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public void setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void validate() {
        if (recipientEmail == null || recipientEmail.isBlank())
            throw new IllegalArgumentException("Email do destinatário é obrigatório");
        
        if (!recipientEmail.matches("^[\\w-.]+@[\\w-]+\\.[a-z]{2,}$"))
            throw new IllegalArgumentException("Email do destinatário deve ter formato válido");
        
        if (recipientName == null || recipientName.isBlank())
            throw new IllegalArgumentException("Nome do destinatário é obrigatório");
        
        if (senderEmail == null || senderEmail.isBlank())
            throw new IllegalArgumentException("Email do remetente é obrigatório");
        
        if (!senderEmail.matches("^[\\w-.]+@[\\w-]+\\.[a-z]{2,}$"))
            throw new IllegalArgumentException("Email do remetente deve ter formato válido");
        
        if (subject == null || subject.isBlank())
            throw new IllegalArgumentException("Assunto é obrigatório");
        
        if (content == null || content.isBlank())
            throw new IllegalArgumentException("Conteúdo é obrigatório");
    }

    @Override
    public String toString() {
        return "EmailRequestDTO{" +
            "recipientEmail='" + recipientEmail + '\'' +
            ", recipientName='" + recipientName + '\'' +
            ", senderEmail='" + senderEmail + '\'' + 
            ", subject='" + subject + '\'' +
            ", content='" + content + '\'' +
            '}';
    }
}