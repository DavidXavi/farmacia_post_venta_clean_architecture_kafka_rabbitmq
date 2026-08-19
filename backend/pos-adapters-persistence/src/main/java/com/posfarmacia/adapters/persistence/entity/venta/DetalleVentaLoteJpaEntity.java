package com.posfarmacia.adapters.persistence.entity.venta;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "detalle_venta_lotes")
public class DetalleVentaLoteJpaEntity {

    @Id
    private UUID id;

    @Column(name = "detalle_venta_id", nullable = false)
    private UUID detalleVentaId;

    @Column(name = "lote_id", nullable = false)
    private UUID loteId;

    @Column(name = "cantidad_tomada", nullable = false)
    private int cantidadTomada;

    protected DetalleVentaLoteJpaEntity() {
    }

    public DetalleVentaLoteJpaEntity(UUID id, UUID detalleVentaId, UUID loteId, int cantidadTomada) {
        this.id = id;
        this.detalleVentaId = detalleVentaId;
        this.loteId = loteId;
        this.cantidadTomada = cantidadTomada;
    }

    public UUID getId() {
        return id;
    }

    public UUID getDetalleVentaId() {
        return detalleVentaId;
    }

    public UUID getLoteId() {
        return loteId;
    }

    public int getCantidadTomada() {
        return cantidadTomada;
    }
}
