package com.posfarmacia.domain.service.seguro;

import com.posfarmacia.domain.valueobject.Dinero;

/**
 * Resultado del calculo de copago (RN25): cuanto cubre el seguro y cuanto paga el cliente.
 */
public record ResultadoCopago(Dinero montoCubierto, Dinero copago) {
}
