package com.posfarmacia.domain.service.promocion;

import com.posfarmacia.domain.exception.PromocionInvalidaException;
import com.posfarmacia.domain.model.promocion.Promocion;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Motor de promociones (servicio de dominio puro, sin I/O): evalua cuales promociones
 * candidatas son aplicables a una linea de venta y valida la eleccion del cajero antes de
 * registrarla. No conoce Venta ni ningun otro agregado de otro contexto, solo UUIDs y los
 * datos minimos que necesita (ver {@link DatosProductoPromocion}).
 */
public final class EvaluadorPromociones {

    /**
     * RF06 / RN08 / RN10 / RN11 / RN12: de las promociones candidatas (ya cargadas por el
     * caller para el producto de la linea), devuelve las que realmente son aplicables hoy.
     * Puede devolver 0, 1 o varias: la eleccion final la hace el cajero (RN08).
     */
    public List<Promocion> obtenerAplicables(List<Promocion> promocionesCandidatas, DatosProductoPromocion datos, LocalDate hoy) {
        return promocionesCandidatas.stream()
                .filter(p -> p.aplicaAProducto(datos.productoId()))
                .filter(p -> p.estaVigente(hoy))
                .filter(p -> datos.cantidad().esMayorOIgualQue(p.getCantidadMinima()))
                .filter(p -> !p.isRequiereCliente() || datos.clienteIdentificado())
                .toList();
    }

    /**
     * Valida la promocion elegida por el cajero para una linea antes de registrarla:
     * RN09 (no repetir la misma promocion en el comprobante), RN11 (vigencia), RN12
     * (producto participante y cantidad minima) y RN10 (cliente identificado si se exige).
     * Lanza {@link PromocionInvalidaException} si alguna regla no se cumple.
     */
    public void validarSeleccion(
            Promocion promocionElegida,
            DatosProductoPromocion datos,
            LocalDate hoy,
            Set<UUID> promocionesYaAplicadasEnComprobante) {
        if (promocionesYaAplicadasEnComprobante.contains(promocionElegida.getId())) {
            throw new PromocionInvalidaException("Esta promocion ya fue aplicada en otra linea de este comprobante.");
        }
        if (!promocionElegida.estaVigente(hoy) || !promocionElegida.aplicaAProducto(datos.productoId())) {
            throw new PromocionInvalidaException("La promocion no es aplicable a esta linea de venta.");
        }
        if (promocionElegida.isRequiereCliente() && !datos.clienteIdentificado()) {
            throw new PromocionInvalidaException("Esta promocion exige que el cliente se identifique con su DNI.");
        }
        if (datos.cantidad().esMenorQue(promocionElegida.getCantidadMinima())) {
            throw new PromocionInvalidaException("La cantidad de la linea no cumple el minimo requerido por la promocion.");
        }
    }
}
