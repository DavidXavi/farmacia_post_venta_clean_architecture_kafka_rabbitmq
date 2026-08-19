package com.posfarmacia.adapters.web.request.venta;

import jakarta.validation.constraints.NotBlank;

/** RN01-RN06: confirma la venta. {@code tipoComprobante} debe ser Boleta, Factura o Ticket. */
public record ConfirmarVentaRequest(@NotBlank String tipoComprobante, @NotBlank String serieComprobante) {
}
