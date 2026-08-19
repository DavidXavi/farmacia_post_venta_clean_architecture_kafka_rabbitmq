package com.posfarmacia.usecases.port.in.venta;

import com.posfarmacia.usecases.dto.venta.ConfirmarVentaCommand;
import com.posfarmacia.usecases.dto.venta.VentaResult;

/**
 * Puerto de entrada RN01-RN06: confirma la venta dentro de una unica transaccion. Asigna lotes por
 * FEFO, confirma el uso de recetas, recalcula copago y linea de credito, y descuenta el stock.
 */
public interface ConfirmarVentaUseCase {

    VentaResult confirmar(ConfirmarVentaCommand command);
}
