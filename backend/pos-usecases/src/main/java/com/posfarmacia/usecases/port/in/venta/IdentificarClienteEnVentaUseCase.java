package com.posfarmacia.usecases.port.in.venta;

import com.posfarmacia.usecases.dto.venta.IdentificarClienteCommand;
import com.posfarmacia.usecases.dto.venta.VentaResult;

/** Puerto de entrada RF09: identifica al cliente de una venta por su DNI. */
public interface IdentificarClienteEnVentaUseCase {

    VentaResult identificar(IdentificarClienteCommand command);
}
