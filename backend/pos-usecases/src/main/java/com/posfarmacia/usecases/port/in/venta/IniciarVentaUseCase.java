package com.posfarmacia.usecases.port.in.venta;

import com.posfarmacia.usecases.dto.venta.IniciarVentaCommand;
import com.posfarmacia.usecases.dto.venta.VentaResult;

/** Puerto de entrada RF05/RN01: inicia una venta, exigiendo que la caja este abierta. */
public interface IniciarVentaUseCase {

    VentaResult iniciar(IniciarVentaCommand command);
}
