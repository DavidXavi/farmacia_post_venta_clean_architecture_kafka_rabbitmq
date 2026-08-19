package com.posfarmacia.domain.model.venta;

import com.posfarmacia.domain.model.Entidad;
import com.posfarmacia.domain.valueobject.Dinero;
import java.time.Instant;
import java.util.UUID;

/**
 * Pago registrado sobre una venta (RF12), en una de las formas de pago autorizadas.
 * Equivalente a PosFarmacia.Domain.Entities.Pago (.NET). La fecha se recibe como
 * parametro (via ClockPort en la capa de aplicacion), nunca Instant.now() en el dominio.
 */
public final class Pago extends Entidad {

    private final UUID ventaId;
    private final UUID formaPagoId;
    private final Dinero monto;
    private final String codigoAutorizacion;
    private final Instant fecha;

    public Pago(UUID ventaId, UUID formaPagoId, Dinero monto, String codigoAutorizacion, Instant fecha) {
        super();
        this.ventaId = ventaId;
        this.formaPagoId = formaPagoId;
        this.monto = monto;
        this.codigoAutorizacion = codigoAutorizacion;
        this.fecha = fecha;
    }

    /** Constructor de reconstruccion usado por los mappers de persistencia (preserva el id existente). */
    public Pago(UUID id, UUID ventaId, UUID formaPagoId, Dinero monto, String codigoAutorizacion, Instant fecha) {
        super(id);
        this.ventaId = ventaId;
        this.formaPagoId = formaPagoId;
        this.monto = monto;
        this.codigoAutorizacion = codigoAutorizacion;
        this.fecha = fecha;
    }

    public UUID getVentaId() {
        return ventaId;
    }

    public UUID getFormaPagoId() {
        return formaPagoId;
    }

    public Dinero getMonto() {
        return monto;
    }

    public String getCodigoAutorizacion() {
        return codigoAutorizacion;
    }

    public Instant getFecha() {
        return fecha;
    }
}
