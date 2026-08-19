package com.posfarmacia.domain.service.venta;

import com.posfarmacia.domain.valueobject.Cantidad;
import java.util.UUID;

/** Cuanta cantidad debe devolverse a cada lote original al anular una venta (RN42). */
public record ReversionStock(UUID loteId, Cantidad cantidad) {
}
