package com.posfarmacia.adapters.web.request.venta;

/** RN39-RN41: anula la venta. {@code motivo} queda reservado para cuando se integre auditoria (RF19). */
public record AnularVentaRequest(String motivo) {
}
