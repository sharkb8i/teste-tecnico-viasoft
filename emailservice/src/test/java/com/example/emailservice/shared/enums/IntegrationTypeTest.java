package com.example.emailservice.shared.enums;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("IntegrationType - Testes Unitários")
public class IntegrationTypeTest {
    
    @Test
    @DisplayName("Deve retornar a descrição correta das enums")
    void deveRetornarDescricaoCorreta() {
        assertEquals("Amazon Web Services", IntegrationType.AWS.getDescription());
        assertEquals("Oracle Cloud Infrastructure", IntegrationType.OCI.getDescription());
    }

    @Test
    @DisplayName("fromString deve retornar enum correta para strings válidas")
    void fromString_DeveRetornarEnumValida() {
        assertEquals(IntegrationType.AWS, IntegrationType.fromString("AWS"));
        assertEquals(IntegrationType.AWS, IntegrationType.fromString("aws"));
        assertEquals(IntegrationType.OCI, IntegrationType.fromString("OCI"));
        assertEquals(IntegrationType.OCI, IntegrationType.fromString("oci"));
    }

    @Test
    @DisplayName("fromString deve retornar nulo para strings inválidas ou nulas")
    void fromString_DeveRetornarNulo() {
        assertNull(IntegrationType.fromString("GCP"));
        assertNull(IntegrationType.fromString("google"));
        assertNull(IntegrationType.fromString(null));
        assertNull(IntegrationType.fromString(""));
    }

    @Test
    @DisplayName("isValid deve retornar true para strings válidas")
    void isValid_DeveRetornarTrue() {
        assertTrue(IntegrationType.isValid("AWS"));
        assertTrue(IntegrationType.isValid("aws"));
        assertTrue(IntegrationType.isValid("OCI"));
        assertTrue(IntegrationType.isValid("oci"));
    }

    @Test
    @DisplayName("isValid deve retornar false para strings inválidas ou nulas")
    void isValid_DeveRetornarFalse() {
        assertFalse(IntegrationType.isValid("GCP"));
        assertFalse(IntegrationType.isValid("google"));
        assertFalse(IntegrationType.isValid(null));
        assertFalse(IntegrationType.isValid(""));
    }
}