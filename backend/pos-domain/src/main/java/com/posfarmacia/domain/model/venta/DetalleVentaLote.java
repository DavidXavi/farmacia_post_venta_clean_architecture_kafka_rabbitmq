package com.posfarmacia.domain.model.venta;

import com.posfarmacia.domain.model.Entidad;
import com.posfarmacia.domain.valueobject.Cantidad;
import java.util.UUID;

/**
 * Lote asignado (por FEFO) a una linea de venta para cubrir su despacho (RN33-RN38).
 * Equivalente a PosFarmacia.Domain.Entities.DetalleVentaLote (.NET).
 */
public final class DetalleVentaLote extends Entidad {

    private final UUID detalleVentaId;
    private final UUID loteId;
    private final Cantidad cantidadTomada;

    public DetalleVentaLote(UUID detalleVentaId, UUID loteId, Cantidad cantidadTomada) {
        super();
        this.detalleVentaId = detalleVentaId;
        this.loteId = loteId;
        this.cantidadTomada = cantidadTomada;
    }

    /** Constructor de reconstruccion usado por los mappers de persistencia (preserva el id existente). */
    public DetalleVentaLote(UUID id, UUID detalleVentaId, UUID loteId, Cantidad cantidadTomada) {
        super(id);
        this.detalleVentaId = detalleVentaId;
        this.loteId = loteId;
        this.cantidadTomada = cantidadTomada;
    }

    public UUID getDetalleVentaId() {
        return detalleVentaId;
    }

    public UUID getLoteId() {
        return loteId;
    }

    public Cantidad getCantidadTomada() {
        return cantidadTomada;
    }
}
