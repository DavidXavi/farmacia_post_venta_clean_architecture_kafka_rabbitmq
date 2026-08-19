package com.posfarmacia.usecases.port.in.venta;

import com.posfarmacia.usecases.dto.venta.AplicarConvenioCommand;
import com.posfarmacia.usecases.dto.venta.CopagoResult;

/** Puerto de entrada RF10: asocia un convenio de seguro a la venta y devuelve una vista previa del copago (RN04: se recalcula al confirmar). */
public interface AplicarConvenioAVentaUseCase {

    CopagoResult aplicar(AplicarConvenioCommand command);
}
