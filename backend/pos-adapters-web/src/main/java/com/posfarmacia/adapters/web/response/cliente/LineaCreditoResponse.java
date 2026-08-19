package com.posfarmacia.adapters.web.response.cliente;

import com.posfarmacia.domain.model.credito.LineaCredito;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record LineaCreditoResponse(
        UUID id,
        UUID clienteId,
        BigDecimal montoAutorizado,
        BigDecimal saldoDisponible,
        String estado,
        LocalDate vigenciaInicio,
        LocalDate vigenciaFin) {

    public static LineaCreditoResponse de(LineaCredito lineaCredito) {
        return new LineaCreditoResponse(
                lineaCredito.getId(),
                lineaCredito.getClienteId(),
                lineaCredito.getMontoAutorizado().monto(),
                lineaCredito.getSaldoDisponible().monto(),
                lineaCredito.getEstado().name(),
                lineaCredito.getVigencia().inicio(),
                lineaCredito.getVigencia().fin());
    }
}
