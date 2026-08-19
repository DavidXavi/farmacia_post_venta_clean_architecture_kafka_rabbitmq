package com.posfarmacia.adapters.web.response.cliente;

import com.posfarmacia.domain.model.seguro.AfiliacionCliente;
import java.time.LocalDate;
import java.util.UUID;

public record AfiliacionResponse(
        UUID id,
        UUID clienteId,
        UUID convenioId,
        String estado,
        LocalDate vigenciaInicio,
        LocalDate vigenciaFin) {

    public static AfiliacionResponse de(AfiliacionCliente afiliacion) {
        return new AfiliacionResponse(
                afiliacion.getId(),
                afiliacion.getClienteId(),
                afiliacion.getConvenioId(),
                afiliacion.getEstado().name(),
                afiliacion.getVigencia().inicio(),
                afiliacion.getVigencia().fin());
    }
}
