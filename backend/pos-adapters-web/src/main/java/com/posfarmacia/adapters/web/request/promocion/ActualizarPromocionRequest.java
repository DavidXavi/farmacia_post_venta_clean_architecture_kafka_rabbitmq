package com.posfarmacia.adapters.web.request.promocion;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Cuerpo de {@code PUT /api/promociones/{id}}: edicion de una promocion existente (CRUD de
 * administracion, RF06). Ruta EXACTA que consume {@code frontend/src/pages/PromocionesPage.jsx}.
 */
public record ActualizarPromocionRequest(
        @NotBlank String nombre,
        String descripcion,
        @NotBlank String tipoBeneficio,
        @NotNull @PositiveOrZero BigDecimal valorBeneficio,
        boolean requiereCliente,
        @Min(1) int cantidadMinima,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        List<UUID> productosParticipantes) {
}
