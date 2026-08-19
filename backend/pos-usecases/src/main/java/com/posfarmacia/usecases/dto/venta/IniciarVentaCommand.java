package com.posfarmacia.usecases.dto.venta;

import java.util.UUID;

/** Entrada de {@code IniciarVentaUseCase} (RF05). {@code clienteDni} es opcional. */
public record IniciarVentaCommand(UUID cajaId, UUID sesionCajaId, UUID usuarioId, String clienteDni) {
}
