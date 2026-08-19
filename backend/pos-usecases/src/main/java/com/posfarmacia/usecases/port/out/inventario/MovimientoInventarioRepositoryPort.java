package com.posfarmacia.usecases.port.out.inventario;

import com.posfarmacia.domain.model.inventario.MovimientoInventario;

/** Puerto de salida: persistencia de movimientos de inventario (RF04/RF15). */
public interface MovimientoInventarioRepositoryPort {

    MovimientoInventario guardar(MovimientoInventario movimiento);
}
