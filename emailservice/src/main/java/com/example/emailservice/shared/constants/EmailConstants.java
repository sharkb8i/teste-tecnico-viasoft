package com.example.emailservice.shared.constants;

/**
 * Constantes compartilhadas relacionadas a email
 */
public final class EmailConstants {
    
    // Limites AWS
    public static final int AWS_RECIPIENT_MAX_LENGTH = 45;
    public static final int AWS_RECIPIENT_NAME_MAX_LENGTH = 60;
    public static final int AWS_SENDER_MAX_LENGTH = 45;
    public static final int AWS_SUBJECT_MAX_LENGTH = 120;
    public static final int AWS_CONTENT_MAX_LENGTH = 256;

    // Limites OCI
    public static final int OCI_RECIPIENT_EMAIL_MAX_LENGTH = 40;
    public static final int OCI_RECIPIENT_NAME_MAX_LENGTH = 50;
    public static final int OCI_SENDER_EMAIL_MAX_LENGTH = 40;
    public static final int OCI_SUBJECT_MAX_LENGTH = 100;
    public static final int OCI_BODY_MAX_LENGTH = 250;

    // Validações gerais
    public static final int GENERAL_EMAIL_MIN_LENGTH = 5;
    public static final int GENERAL_NAME_MAX_LENGTH = 100;
    public static final int GENERAL_SUBJECT_MAX_LENGTH = 250;
    public static final int GENERAL_CONTENT_MAX_LENGTH = 500;

    // Mensagens de erro
    public static final String ERROR_NULL_EMAIL = "Email não pode ser nulo";
    public static final String ERROR_EMPTY_FIELD = "Campo não pode estar vazio";
    public static final String ERROR_INVALID_EMAIL_FORMAT = "Formato de email inválido";
    public static final String ERROR_UNSUPPORTED_INTEGRATION = "Tipo de integração não suportada";

    private EmailConstants() {
        throw new IllegalStateException("Classe de constantes não deve ser inicializada.");
    }
}