package com.posfarmacia.usecases.port.in.inventario;

import com.posfarmacia.usecases.dto.inventario.StockVendibleResult;
import java.util.UUID;

/** Puerto de entrada: consulta el stock vendible de un producto en un local (RF14). */
public interface ConsultarStockVendibleUseCase {

    StockVendibleResult consultar(UUID productoId, UUID localId);
}
