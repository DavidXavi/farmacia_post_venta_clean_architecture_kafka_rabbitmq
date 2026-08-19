package com.posfarmacia.domain.service.credito;

import com.posfarmacia.domain.exception.LineaCreditoInvalidaException;
import com.posfarmacia.domain.model.credito.LineaCredito;
import com.posfarmacia.domain.valueobject.Dinero;
import java.time.LocalDate;

/**
 * Servicio de dominio puro (sin I/O) que valida una linea de credito antes de
 * financiar una venta (RF11, RN29-RN30).
 */
public final class ValidadorLineaCredito {

    public void validarParaConsumo(LineaCredito lineaCredito, Dinero monto, LocalDate hoy) {
        if (!lineaCredito.estaActivaYVigente(hoy)) {
            throw new LineaCreditoInvalidaException("La linea de credito no esta activa o vigente.");
        }

        if (monto.esMayorQue(lineaCredito.getSaldoDisponible())) {
            throw new LineaCreditoInvalidaException("El monto solicitado supera el saldo disponible del cliente.");
        }
    }
}
