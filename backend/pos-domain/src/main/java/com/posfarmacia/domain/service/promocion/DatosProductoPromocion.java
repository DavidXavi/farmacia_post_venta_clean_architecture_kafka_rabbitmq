package com.posfarmacia.domain.service.promocion;

import com.posfarmacia.domain.exception.ValorInvalidoException;
import com.posfarmacia.domain.valueobject.Cantidad;
import java.util.UUID;

/**
 * Datos minimos de una linea de venta necesarios para evaluar promociones, sin depender de la
 * entidad completa de Venta/DetalleVenta (que pertenece a otro contexto). Se pasa explicitamente
 * si el cliente esta identificado (RN10) y la cantidad de la linea (RN12).
 */
public record DatosProductoPromocion(UUID productoId, Cantidad cantidad, boolean clienteIdentificado) {

    public DatosProductoPromocion {
        if (productoId == null) {
            throw new ValorInvalidoException("El productoId es obligatorio para evaluar promociones.");
        }
        if (cantidad == null) {
            throw new ValorInvalidoException("La cantidad es obligatoria para evaluar promociones.");
        }
    }
}
