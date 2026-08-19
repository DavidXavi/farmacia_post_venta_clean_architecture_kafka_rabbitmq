package com.posfarmacia.adapters.web.response.receta;

import com.posfarmacia.domain.model.receta.Receta;
import java.time.LocalDate;
import java.util.UUID;

public record RecetaResponse(
        UUID id,
        String numero,
        String tipo,
        LocalDate fechaEmision,
        LocalDate fechaVencimiento,
        UUID productoId,
        UUID clienteId,
        String estado,
        boolean retenidaEnBotica) {

    public static RecetaResponse de(Receta receta) {
        return new RecetaResponse(
                receta.getId(),
                receta.getNumero().valor(),
                receta.getTipo().name(),
                receta.getFechaEmision(),
                receta.getFechaVencimiento(),
                receta.getProductoId(),
                receta.getClienteId(),
                receta.getEstado().name(),
                receta.isRetenidaEnBotica());
    }
}
