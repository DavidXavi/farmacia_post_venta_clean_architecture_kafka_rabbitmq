package com.posfarmacia.usecases.port.in.inventario;

import java.util.UUID;

/** Puerto de entrada: bloquea un lote no apto para la venta (RF04). */
public interface BloquearLoteUseCase {

    void bloquear(UUID loteId);
}
