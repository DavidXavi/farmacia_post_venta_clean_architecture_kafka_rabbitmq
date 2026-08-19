package com.posfarmacia.usecases.port.in.venta;

import com.posfarmacia.usecases.dto.venta.RegistrarPagoCommand;
import com.posfarmacia.usecases.dto.venta.VentaResult;

/** Puerto de entrada RF12: registra un pago sobre una venta en proceso. */
public interface RegistrarPagoUseCase {

    VentaResult registrar(RegistrarPagoCommand command);
}
