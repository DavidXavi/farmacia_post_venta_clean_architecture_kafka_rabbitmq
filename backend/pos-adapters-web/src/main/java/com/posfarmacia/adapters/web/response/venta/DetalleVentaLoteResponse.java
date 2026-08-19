package com.posfarmacia.adapters.web.response.venta;

import com.posfarmacia.usecases.dto.venta.DetalleVentaLoteResult;
import java.util.UUID;

public record DetalleVentaLoteResponse(UUID id, UUID loteId, int cantidadTomada) {

    public static DetalleVentaLoteResponse desde(DetalleVentaLoteResult result) {
        return new DetalleVentaLoteResponse(result.id(), result.loteId(), result.cantidadTomada());
    }
}
