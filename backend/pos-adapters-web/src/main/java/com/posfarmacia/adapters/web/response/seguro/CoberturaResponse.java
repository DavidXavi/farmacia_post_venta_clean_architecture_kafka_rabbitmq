package com.posfarmacia.adapters.web.response.seguro;

import com.posfarmacia.usecases.dto.seguro.ConsultarCoberturaResult;
import java.math.BigDecimal;
import java.util.UUID;

public record CoberturaResponse(
        UUID clienteId,
        UUID convenioId,
        UUID productoId,
        BigDecimal montoCubierto,
        BigDecimal copago,
        String codigoAutorizacion) {

    public static CoberturaResponse de(ConsultarCoberturaResult resultado) {
        return new CoberturaResponse(
                resultado.clienteId(),
                resultado.convenioId(),
                resultado.productoId(),
                resultado.montoCubierto(),
                resultado.copago(),
                resultado.codigoAutorizacion());
    }
}
