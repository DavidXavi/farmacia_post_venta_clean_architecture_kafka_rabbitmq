package com.posfarmacia.usecases.dto.venta;

import java.util.UUID;

public record DetalleVentaLoteResult(UUID id, UUID loteId, int cantidadTomada) {
}
