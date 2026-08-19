package com.posfarmacia.adapters.web.response.inventario;

import com.posfarmacia.usecases.dto.inventario.LoteResult;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record LoteResponse(
        UUID id,
        String codigo,
        UUID productoId,
        LocalDate fechaVencimiento,
        int cantidadRecibida,
        int cantidadDisponible,
        BigDecimal costo,
        UUID localId,
        String estado) {

    public static LoteResponse desde(LoteResult result) {
        return new LoteResponse(
                result.id(),
                result.codigo(),
                result.productoId(),
                result.fechaVencimiento(),
                result.cantidadRecibida(),
                result.cantidadDisponible(),
                result.costo(),
                result.localId(),
                result.estado());
    }
}
