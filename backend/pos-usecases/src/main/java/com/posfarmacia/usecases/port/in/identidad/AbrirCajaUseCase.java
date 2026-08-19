package com.posfarmacia.usecases.port.in.identidad;

import com.posfarmacia.domain.model.identidad.SesionCaja;
import com.posfarmacia.domain.valueobject.Dinero;
import java.util.UUID;

/**
 * RF02: abre una sesion de caja indicando el monto inicial. Falla con
 * {@code EntidadNoEncontradaException} si la caja no existe y con
 * {@code CajaCerradaException} si ya tiene una sesion abierta.
 */
public interface AbrirCajaUseCase {

    SesionCaja abrir(UUID cajaId, UUID usuarioId, Dinero montoInicial);
}
