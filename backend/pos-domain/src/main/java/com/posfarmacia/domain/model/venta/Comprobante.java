package com.posfarmacia.domain.model.venta;

import com.posfarmacia.domain.enums.TipoComprobante;
import com.posfarmacia.domain.model.Entidad;
import com.posfarmacia.domain.valueobject.NumeroComprobante;
import java.time.Instant;
import java.util.UUID;

/**
 * Comprobante de pago emitido al confirmar una venta (RF05). Equivalente a
 * PosFarmacia.Domain.Entities.Comprobante (.NET). La fecha de emision se recibe como
 * parametro (via ClockPort en la capa de aplicacion), nunca Instant.now() en el dominio.
 */
public final class Comprobante extends Entidad {

    private final UUID ventaId;
    private final TipoComprobante tipo;
    private final NumeroComprobante numero;
    private final Instant fechaEmision;

    public Comprobante(UUID ventaId, TipoComprobante tipo, NumeroComprobante numero, Instant fechaEmision) {
        super();
        this.ventaId = ventaId;
        this.tipo = tipo;
        this.numero = numero;
        this.fechaEmision = fechaEmision;
    }

    /** Constructor de reconstruccion usado por los mappers de persistencia (preserva el id existente). */
    public Comprobante(UUID id, UUID ventaId, TipoComprobante tipo, NumeroComprobante numero, Instant fechaEmision) {
        super(id);
        this.ventaId = ventaId;
        this.tipo = tipo;
        this.numero = numero;
        this.fechaEmision = fechaEmision;
    }

    public UUID getVentaId() {
        return ventaId;
    }

    public TipoComprobante getTipo() {
        return tipo;
    }

    public NumeroComprobante getNumero() {
        return numero;
    }

    public Instant getFechaEmision() {
        return fechaEmision;
    }
}
