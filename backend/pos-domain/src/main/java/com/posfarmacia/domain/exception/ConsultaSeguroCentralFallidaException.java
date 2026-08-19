package com.posfarmacia.domain.exception;

/**
 * RN27: si la central o el servicio del seguro no puede confirmar la cobertura
 * (falla tecnica de la consulta externa), la venta no debe asumir cobertura de
 * forma automatica. Distinta de {@link ConvenioNoDisponibleException}, que
 * representa una regla de negocio incumplida (convenio inactivo o afiliacion no
 * vigente) una vez que la consulta si pudo resolverse.
 */
public final class ConsultaSeguroCentralFallidaException extends DomainException {
    public ConsultaSeguroCentralFallidaException(String mensaje) {
        super(mensaje);
    }
}
