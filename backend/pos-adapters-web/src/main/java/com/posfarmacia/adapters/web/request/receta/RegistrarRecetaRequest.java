package com.posfarmacia.adapters.web.request.receta;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Cuerpo de {@code POST /api/recetas}. El frontend (no reescrito, ver
 * {@code RecetasPage.jsx}) mantiene el contrato de {@code arquitectura_2_t2}: un unico
 * campo de texto libre {@code dosisYCantidadAutorizada}, mientras que el dominio Java
 * separa {@code dosis} (texto) de {@code cantidadAutorizada} (numerico, para RN15). El
 * controller hace esa traduccion (ver {@code RecetasController}), no es una regla de
 * negocio.
 */
public record RegistrarRecetaRequest(
        @NotBlank String numero,
        @NotBlank String tipo,
        @NotNull LocalDate fechaEmision,
        LocalDate fechaVencimiento,
        @NotNull UUID productoId,
        UUID clienteId,
        @NotBlank String datosPaciente,
        @NotBlank String datosProfesional,
        @NotBlank String dosisYCantidadAutorizada,
        String archivoRespaldoUrl) {
}
