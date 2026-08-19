package com.posfarmacia.adapters.persistence.entity.venta;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "comprobantes")
public class ComprobanteJpaEntity {

    @Id
    private UUID id;

    @Column(name = "venta_id", nullable = false, unique = true)
    private UUID ventaId;

    @Column(name = "tipo", nullable = false)
    private String tipo;

    @Column(name = "serie", nullable = false)
    private String serie;

    @Column(name = "correlativo", nullable = false)
    private int correlativo;

    @Column(name = "fecha_emision", nullable = false)
    private Instant fechaEmision;

    protected ComprobanteJpaEntity() {
    }

    public ComprobanteJpaEntity(UUID id, UUID ventaId, String tipo, String serie, int correlativo,
            Instant fechaEmision) {
        this.id = id;
        this.ventaId = ventaId;
        this.tipo = tipo;
        this.serie = serie;
        this.correlativo = correlativo;
        this.fechaEmision = fechaEmision;
    }

    public UUID getId() {
        return id;
    }

    public UUID getVentaId() {
        return ventaId;
    }

    public String getTipo() {
        return tipo;
    }

    public String getSerie() {
        return serie;
    }

    public int getCorrelativo() {
        return correlativo;
    }

    public Instant getFechaEmision() {
        return fechaEmision;
    }
}
