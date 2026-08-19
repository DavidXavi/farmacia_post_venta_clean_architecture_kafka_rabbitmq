package com.posfarmacia.domain.service.incentivo;

import com.posfarmacia.domain.model.incentivo.ReglaIncentivo;
import com.posfarmacia.domain.valueobject.Cantidad;
import com.posfarmacia.domain.valueobject.Dinero;
import java.math.BigDecimal;

/**
 * Servicio de dominio puro (RF18): calcula el monto de incentivo ganado por una linea de venta
 * como {@code montoPorUnidad * cantidadVendida}. Equivalente a
 * PosFarmacia.Domain.Services.CalculadorIncentivos (.NET).
 */
public final class CalculadorIncentivos {

    public Dinero calcular(ReglaIncentivo regla, Cantidad cantidadVendida) {
        return regla.getMontoPorUnidad().multiplicar(BigDecimal.valueOf(cantidadVendida.valor()));
    }
}
