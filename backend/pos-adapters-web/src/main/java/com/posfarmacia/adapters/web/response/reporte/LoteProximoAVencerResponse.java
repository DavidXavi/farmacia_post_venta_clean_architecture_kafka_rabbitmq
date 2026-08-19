package com.posfarmacia.adapters.web.response.reporte;

import com.posfarmacia.usecases.dto.reporte.LoteProximoAVencerResult;
import java.time.LocalDate;
import java.util.UUID;

public record LoteProximoAVencerResponse(
        UUID loteId,
        String codigo,
        UUID productoId,
        LocalDate fechaVencimiento,
        int cantidadDisponible) {

    public static LoteProximoAVencerResponse desde(LoteProximoAVencerResult result) {
        return new LoteProximoAVencerResponse(result.loteId(), result.codigo(), result.productoId(),
                result.fechaVencimiento(), result.cantidadDisponible());
    }
}
