package com.example.emailservice.integration.dto;

import com.example.emailservice.shared.constants.EmailConstants;

/**
 * DTO para integração com AWS
 * Representa o formato específico exigido pela AWS
 */
public class EmailAwsDTO {

    private String recipient;
    private String recipientName;
    private String sender;
    private String subject;
    private String content;
    
    public EmailAwsDTO() { }

    public EmailAwsDTO(String recipient, String recipientName, String sender,
                       String subject, String content) {
        this.recipient = truncate(recipient, EmailConstants.AWS_RECIPIENT_MAX_LENGTH);
        this.recipientName = truncate(recipientName, EmailConstants.AWS_RECIPIENT_NAME_MAX_LENGTH);
        this.sender = truncate(sender, EmailConstants.AWS_SENDER_MAX_LENGTH);
        this.subject = truncate(subject, EmailConstants.AWS_SUBJECT_MAX_LENGTH);
        this.content = truncate(content, EmailConstants.AWS_CONTENT_MAX_LENGTH);
    }

    /**
     * Trunca string respeitando limite de caracteres da AWS
     */
    private String truncate(String value, int maxLength) {
        if (value == null) return null;
        return value.length() > maxLength ? value.substring(0, maxLength) : value;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = truncate(recipient, EmailConstants.AWS_RECIPIENT_MAX_LENGTH);
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = truncate(recipientName, EmailConstants.AWS_RECIPIENT_NAME_MAX_LENGTH);
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = truncate(sender, EmailConstants.AWS_SENDER_MAX_LENGTH);
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = truncate(subject, EmailConstants.AWS_SUBJECT_MAX_LENGTH);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = truncate(content, EmailConstants.AWS_CONTENT_MAX_LENGTH);
    }

    @Override
    public String toString() {
        return "EmailAwsDTO{" +
            "recipient='" + recipient + '\'' +
            ", recipientName='" + recipientName + '\'' +
            ", sender='" + sender + '\'' + 
            ", subject='" + subject + '\'' +
            ", content='" + content + '\'' +
            '}';
    }
}