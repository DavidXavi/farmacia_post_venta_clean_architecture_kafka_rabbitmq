package com.posfarmacia.usecases.port.in.identidad;

import com.posfarmacia.domain.model.identidad.SesionCaja;
import com.posfarmacia.domain.valueobject.Dinero;
import java.util.UUID;

/**
 * RF02: cierra la sesion de caja activa. El monto esperado se calcula a partir de las ventas
 * confirmadas del turno (monto inicial + ingresos en efectivo), igual que
 * PosFarmacia.Application.UseCases.CerrarCajaUseCase (.NET); el frontend solo envia el monto
 * declarado por el cajero y una observacion opcional. Falla con
 * {@code EntidadNoEncontradaException} si la caja no tiene sesion abierta.
 */
public interface CerrarCajaUseCase {

    SesionCaja cerrar(UUID cajaId, Dinero montoDeclarado, String observacion);
}
