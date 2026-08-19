package com.posfarmacia.adapters.web.response.venta;

import com.posfarmacia.usecases.dto.venta.PagoResult;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record PagoResponse(UUID id, UUID formaPagoId, BigDecimal monto, String codigoAutorizacion, Instant fecha) {

    public static PagoResponse desde(PagoResult result) {
        return new PagoResponse(result.id(), result.formaPagoId(), result.monto(), result.codigoAutorizacion(),
                result.fecha());
    }
}
