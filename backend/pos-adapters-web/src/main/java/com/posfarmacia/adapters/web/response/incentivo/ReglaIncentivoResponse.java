package com.posfarmacia.adapters.web.response.incentivo;

import com.posfarmacia.domain.model.incentivo.ReglaIncentivo;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record ReglaIncentivoResponse(
        UUID id,
        String nombre,
        UUID productoId,
        UUID categoriaId,
        BigDecimal montoPorUnidad,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        boolean activa) {

    public static ReglaIncentivoResponse desde(ReglaIncentivo regla) {
        return new ReglaIncentivoResponse(
                regla.getId(),
                regla.getNombre(),
                regla.getProductoId(),
                regla.getCategoriaId(),
                regla.getMontoPorUnidad().monto(),
                regla.getVigencia().inicio(),
                regla.getVigencia().fin(),
                regla.isActiva());
    }
}
