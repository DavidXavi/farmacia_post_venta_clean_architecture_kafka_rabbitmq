package com.posfarmacia.adapters.persistence.entity.inventario;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "movimientos_inventario")
public class MovimientoInventarioJpaEntity {

    @Id
    private UUID id;

    @Column(name = "lote_id", nullable = false)
    private UUID loteId;

    @Column(nullable = false)
    private String tipo;

    @Column(nullable = false)
    private int cantidad;

    @Column(name = "usuario_id", nullable = false)
    private UUID usuarioId;

    private String referencia;

    @Column(nullable = false)
    private Instant fecha;

    protected MovimientoInventarioJpaEntity() {
    }

    public MovimientoInventarioJpaEntity(UUID id, UUID loteId, String tipo, int cantidad, UUID usuarioId,
            String referencia, Instant fecha) {
        this.id = id;
        this.loteId = loteId;
        this.tipo = tipo;
        this.cantidad = cantidad;
        this.usuarioId = usuarioId;
        this.referencia = referencia;
        this.fecha = fecha;
    }

    public UUID getId() {
        return id;
    }

    public UUID getLoteId() {
        return loteId;
    }

    public String getTipo() {
        return tipo;
    }

    public int getCantidad() {
        return cantidad;
    }

    public UUID getUsuarioId() {
        return usuarioId;
    }

    public String getReferencia() {
        return referencia;
    }

    public Instant getFecha() {
        return fecha;
    }
}
