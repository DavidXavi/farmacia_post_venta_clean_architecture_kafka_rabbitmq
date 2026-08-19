package com.posfarmacia.adapters.web.response.anulacion;

import com.posfarmacia.usecases.dto.anulacion.NotaCreditoResult;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record NotaCreditoResponse(UUID id, UUID ventaId, UUID comprobanteId, UUID usuarioId, String motivo,
        BigDecimal montoTotal, Instant fecha) {

    public static NotaCreditoResponse desde(NotaCreditoResult result) {
        return new NotaCreditoResponse(result.id(), result.ventaId(), result.comprobanteId(), result.usuarioId(),
                result.motivo(), result.montoTotal(), result.fecha());
    }
}
