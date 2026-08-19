package com.posfarmacia.usecases.dto.anulacion;

import java.util.List;
import java.util.UUID;

/** RF16/RN41: registra una devolucion parcial sobre una venta confirmada, con motivo obligatorio. */
public record RegistrarDevolucionCommand(UUID ventaId, UUID usuarioId, String motivo,
        List<LineaDevolucionCommand> lineas) {
}
