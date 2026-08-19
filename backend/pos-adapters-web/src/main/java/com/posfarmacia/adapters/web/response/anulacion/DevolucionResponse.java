package com.posfarmacia.adapters.web.response.anulacion;

import com.posfarmacia.usecases.dto.anulacion.DevolucionResult;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

/** Nombres de campo en camelCase, tal como los espera `frontend/src/pages/DevolucionesPage.jsx`. */
public record DevolucionResponse(UUID id, UUID ventaId, UUID usuarioId, String motivo, Instant fecha,
        BigDecimal total, List<DetalleDevolucionResponse> detalles) {

    public static DevolucionResponse desde(DevolucionResult result) {
        return new DevolucionResponse(result.id(), result.ventaId(), result.usuarioId(), result.motivo(),
                result.fecha(), result.total(), result.detalles().stream().map(DetalleDevolucionResponse::desde).toList());
    }
}
