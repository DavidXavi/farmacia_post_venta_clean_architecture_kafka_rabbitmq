package com.posfarmacia.domain.service.venta;

import com.posfarmacia.domain.valueobject.Cantidad;
import com.posfarmacia.domain.valueobject.Dinero;
import com.posfarmacia.domain.valueobject.Porcentaje;
import java.math.BigDecimal;
import java.util.List;

/**
 * Servicio de dominio puro (sin I/O), equivalente a PosFarmacia.Domain.Services.CalculadorTotalVenta
 * (.NET): recalcula el subtotal de una linea (base imponible menos descuento, mas impuesto) y el
 * total de la venta como la suma de los subtotales de linea (RN04: recalculo siempre en servidor).
 */
public final class CalculadorTotalVenta {

    public Dinero calcularSubtotalLinea(Dinero precioUnitario, Cantidad cantidad, Dinero descuento,
            Porcentaje tasaImpuesto) {
        Dinero baseImponible = new Dinero(
                precioUnitario.monto().multiply(BigDecimal.valueOf(cantidad.valor())).subtract(descuento.monto()));
        Dinero impuesto = tasaImpuesto.aplicarSobre(baseImponible);
        return baseImponible.sumar(impuesto);
    }

    public Dinero calcularTotalVenta(List<Dinero> subtotalesLinea) {
        return subtotalesLinea.stream().reduce(Dinero.CERO, Dinero::sumar);
    }
}
