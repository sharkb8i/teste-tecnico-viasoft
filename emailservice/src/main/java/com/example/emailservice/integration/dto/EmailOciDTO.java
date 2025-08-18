package com.example.emailservice.integration.dto;

import com.example.emailservice.shared.constants.EmailConstants;

/**
 * DTO para integração com OCI
 * Representa o formato específico exigido pela OCI
 */
public class EmailOciDTO {

    private String recipientEmail;
    private String recipientName;
    private String senderEmail;
    private String subject;
    private String body;
    
    public EmailOciDTO() { }

    public EmailOciDTO(String recipientEmail, String recipientName, String senderEmail,
                       String subject, String body) {
        this.recipientEmail = truncate(recipientEmail, EmailConstants.OCI_RECIPIENT_EMAIL_MAX_LENGTH);
        this.recipientName = truncate(recipientName, EmailConstants.OCI_RECIPIENT_NAME_MAX_LENGTH);
        this.senderEmail = truncate(senderEmail, EmailConstants.OCI_SENDER_EMAIL_MAX_LENGTH);
        this.subject = truncate(subject, EmailConstants.OCI_SUBJECT_MAX_LENGTH);
        this.body = truncate(body, EmailConstants.OCI_BODY_MAX_LENGTH);
    }

    /**
     * Trunca string respeitando limite de caracteres da OCI
     */
    private String truncate(String value, int maxLength) {
        if (value == null) return null;
        return value.length() > maxLength ? value.substring(0, maxLength) : value;
    }

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public void setRecipientEmail(String recipientEmail) {
        this.recipientEmail = truncate(recipientEmail, EmailConstants.OCI_RECIPIENT_EMAIL_MAX_LENGTH);
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = truncate(recipientName, EmailConstants.OCI_RECIPIENT_NAME_MAX_LENGTH);
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = truncate(senderEmail, EmailConstants.OCI_SENDER_EMAIL_MAX_LENGTH);
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = truncate(subject, EmailConstants.OCI_SUBJECT_MAX_LENGTH);
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = truncate(body, EmailConstants.OCI_BODY_MAX_LENGTH);
    }

    @Override
    public String toString() {
        return "EmailOciDTO{" +
            "recipientEmail='" + recipientEmail + '\'' +
            ", recipientName='" + recipientName + '\'' +
            ", senderEmail='" + senderEmail + '\'' + 
            ", subject='" + subject + '\'' +
            ", body='" + body + '\'' +
            '}';
    }
}