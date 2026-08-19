package com.posfarmacia.domain.service.inventario;

import com.posfarmacia.domain.valueobject.Cantidad;
import java.util.UUID;

/** Resultado de la asignacion FEFO: cuanta cantidad se despacha desde cada lote. */
public record AsignacionLote(UUID loteId, Cantidad cantidad) {
}
