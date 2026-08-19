package com.posfarmacia.adapters.web.exception;

import java.time.Instant;

/**
 * Forma comun de las respuestas de error (Word, seccion 10): codigo funcional, mensaje
 * entendible por el usuario e identificador de correlacion para rastrear la operacion.
 */
public record ErrorResponse(String codigo, String mensaje, String correlationId, Instant timestamp) {

    public static ErrorResponse de(String codigo, String mensaje, String correlationId) {
        return new ErrorResponse(codigo, mensaje, correlationId, Instant.now());
    }
}
