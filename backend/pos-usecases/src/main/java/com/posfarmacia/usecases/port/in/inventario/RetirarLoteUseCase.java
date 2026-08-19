package com.posfarmacia.usecases.port.in.inventario;

import java.util.UUID;

/** Puerto de entrada: retira un lote del stock vendible en todas las sedes (RN37). */
public interface RetirarLoteUseCase {

    void retirar(UUID loteId);
}
