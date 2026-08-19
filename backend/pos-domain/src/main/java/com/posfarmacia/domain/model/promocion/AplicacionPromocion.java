package com.posfarmacia.domain.model.promocion;

import com.posfarmacia.domain.model.Entidad;
import com.posfarmacia.domain.valueobject.Dinero;
import java.util.Objects;
import java.util.UUID;

/**
 * Registro de que una {@link Promocion} (promocionId) fue aplicada en una linea (detalleVentaId)
 * de un comprobante (ventaId), con el monto de descuento resultante. La Venta es un agregado de
 * otro contexto (Ventas): aqui solo se referencia por UUID, nunca por objeto (regla de oro de
 * migracion, ver convenciones-migracion-java.md).
 */
public final class AplicacionPromocion extends Entidad {

    private final UUID ventaId;
    private final UUID detalleVentaId;
    private final UUID promocionId;
    private final Dinero montoDescuento;

    public AplicacionPromocion(UUID id, UUID ventaId, UUID detalleVentaId, UUID promocionId, Dinero montoDescuento) {
        super(id);
        this.ventaId = Objects.requireNonNull(ventaId, "ventaId no puede ser nulo");
        this.detalleVentaId = Objects.requireNonNull(detalleVentaId, "detalleVentaId no puede ser nulo");
        this.promocionId = Objects.requireNonNull(promocionId, "promocionId no puede ser nulo");
        this.montoDescuento = montoDescuento == null ? Dinero.CERO : montoDescuento;
    }

    /** Registra una nueva aplicacion (id generado) al momento de que el cajero elige la promocion. */
    public static AplicacionPromocion registrar(UUID ventaId, UUID detalleVentaId, UUID promocionId, Dinero montoDescuento) {
        return new AplicacionPromocion(UUID.randomUUID(), ventaId, detalleVentaId, promocionId, montoDescuento);
    }

    public UUID getVentaId() {
        return ventaId;
    }

    public UUID getDetalleVentaId() {
        return detalleVentaId;
    }

    public UUID getPromocionId() {
        return promocionId;
    }

    public Dinero getMontoDescuento() {
        return montoDescuento;
    }
}
