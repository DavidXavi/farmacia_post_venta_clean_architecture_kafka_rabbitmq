package com.posfarmacia.usecases.dto.anulacion;

import java.util.UUID;

/** RF16/RN39-RN41: emite una nota de credito sobre una venta confirmada (tipicamente de un dia anterior). */
public record EmitirNotaCreditoCommand(UUID ventaId, UUID usuarioId, String motivo) {
}
