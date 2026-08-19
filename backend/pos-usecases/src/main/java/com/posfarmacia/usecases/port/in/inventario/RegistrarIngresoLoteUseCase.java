package com.posfarmacia.usecases.port.in.inventario;

import com.posfarmacia.usecases.dto.inventario.LoteResult;
import com.posfarmacia.usecases.dto.inventario.RegistrarLoteCommand;

/** Puerto de entrada: registra el ingreso de un lote de mercaderia (RF04). */
public interface RegistrarIngresoLoteUseCase {

    LoteResult registrar(RegistrarLoteCommand command);
}
