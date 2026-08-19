package com.posfarmacia.usecases.dto.anulacion;

import java.util.UUID;

/** Una linea a devolver dentro de {@link RegistrarDevolucionCommand} (RF16). */
public record LineaDevolucionCommand(UUID detalleVentaId, int cantidad) {
}
