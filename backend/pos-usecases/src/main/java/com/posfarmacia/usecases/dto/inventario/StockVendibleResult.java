package com.posfarmacia.usecases.dto.inventario;

import java.util.List;
import java.util.UUID;

/** RF14: stock vendible de un producto en un local, con el detalle de lotes vendibles ordenados FEFO. */
public record StockVendibleResult(UUID productoId, int cantidadTotalVendible, List<LoteResult> lotes) {
}
