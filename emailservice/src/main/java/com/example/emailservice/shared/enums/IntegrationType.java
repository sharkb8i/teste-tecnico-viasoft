package com.example.emailservice.shared.enums;

/**
 * Enum compartilhado representando os tipos de integração disponíveis
 */
public enum IntegrationType {
    AWS("Amazon Web Services"),
    OCI("Oracle Cloud Infrastructure");

    private final String description;

    IntegrationType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Converte string para enum de forma segura
     * @param type string do tipo
     * @return enum correspondente ou null se inválido
     */
    public static IntegrationType fromString(String type) {
        if (type == null) return null;

        try {
            return IntegrationType.valueOf(type.toUpperCase());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Verifica se um tipo é válido
     * @param type string do tipo
     * @return true se válido
     */
    public static boolean isValid(String type) {
        return fromString(type) != null;
    }
}