package com.posfarmacia.usecases.port.in.anulacion;

import com.posfarmacia.usecases.dto.anulacion.DevolucionResult;
import com.posfarmacia.usecases.dto.anulacion.RegistrarDevolucionCommand;

/**
 * Puerto de entrada RF16/RN42/RN43: registra una devolucion parcial de una venta confirmada,
 * validando cada linea (producto no controlado, dentro del plazo, cantidad disponible) y
 * revirtiendo el stock a los lotes originales cuando siguen en condiciones de venderse.
 */
public interface RegistrarDevolucionUseCase {

    DevolucionResult registrar(RegistrarDevolucionCommand command);
}
