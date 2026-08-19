package com.posfarmacia.usecases.port.out.seguro;

import com.posfarmacia.domain.valueobject.CodigoAutorizacionSeguro;
import com.posfarmacia.domain.valueobject.Porcentaje;

/**
 * Respuesta de la central/servicio del seguro a una consulta de cobertura (RF10, RN22-RN26).
 *
 * @param convenioActivo           si el convenio esta activo en la central (RN23).
 * @param afiliacionActivaYVigente si la afiliacion del cliente esta activa y vigente (RN23).
 * @param porcentajeCubierto       porcentaje cubierto para el producto consultado, o {@code null} si no esta cubierto (RN24).
 * @param codigoAutorizacion       codigo de autorizacion entregado por el seguro, cuando corresponda (RN26).
 */
public record CoberturaCentralResult(
        boolean convenioActivo,
        boolean afiliacionActivaYVigente,
        Porcentaje porcentajeCubierto,
        CodigoAutorizacionSeguro codigoAutorizacion) {
}
