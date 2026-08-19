package com.posfarmacia.domain.service.seguro;

import com.posfarmacia.domain.exception.ConvenioNoDisponibleException;
import com.posfarmacia.domain.valueobject.Dinero;
import com.posfarmacia.domain.valueobject.Porcentaje;

/**
 * Servicio de dominio puro (sin I/O) para el calculo de copago (RF10, RN22-RN25).
 * Recibe como parametros todo lo que necesita: el estado de vigencia del convenio y
 * de la afiliacion ya resueltos por quien lo invoca (caso de uso), y el porcentaje
 * cubierto para el producto de la linea de venta (ausente si el producto no esta
 * cubierto por el convenio, RN24).
 */
public final class CalculadorCopago {

    /**
     * @param montoLinea               importe total de la linea de venta antes de aplicar la cobertura.
     * @param convenioActivo           si el convenio de seguro esta activo (RN23).
     * @param afiliacionActivaYVigente si la afiliacion del cliente al convenio esta activa y dentro de su periodo de vigencia (RN23).
     * @param porcentajeCubierto      porcentaje cubierto para el producto de la linea, o {@code null} si el producto no esta cubierto (RN24).
     * @throws ConvenioNoDisponibleException si el convenio no esta activo o la afiliacion no esta activa/vigente.
     */
    public ResultadoCopago calcular(Dinero montoLinea, boolean convenioActivo, boolean afiliacionActivaYVigente,
                                     Porcentaje porcentajeCubierto) {
        if (!convenioActivo || !afiliacionActivaYVigente) {
            throw new ConvenioNoDisponibleException(
                    "El convenio de seguro no esta activo o la afiliacion del cliente no esta vigente.");
        }

        if (porcentajeCubierto == null) {
            // RN24/RN25: el producto no esta cubierto por el convenio, el cliente paga el 100%.
            return new ResultadoCopago(Dinero.CERO, montoLinea);
        }

        Dinero montoCubierto = porcentajeCubierto.aplicarSobre(montoLinea);
        Dinero copago = montoLinea.restar(montoCubierto);
        return new ResultadoCopago(montoCubierto, copago);
    }
}
