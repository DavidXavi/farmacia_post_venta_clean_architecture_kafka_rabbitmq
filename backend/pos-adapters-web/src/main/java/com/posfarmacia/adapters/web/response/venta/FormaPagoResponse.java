package com.posfarmacia.adapters.web.response.venta;

import com.posfarmacia.domain.model.venta.FormaPago;
import java.util.UUID;

public record FormaPagoResponse(UUID id, String nombre, String tipo, boolean activo) {

    public static FormaPagoResponse desde(FormaPago formaPago) {
        return new FormaPagoResponse(formaPago.getId(), formaPago.getNombre(), formaPago.getTipo().name(),
                formaPago.isActivo());
    }
}
