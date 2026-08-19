package com.posfarmacia.adapters.persistence.entity.anulacion;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "devoluciones")
public class DevolucionJpaEntity {

    @Id
    private UUID id;

    @Column(name = "venta_id", nullable = false)
    private UUID ventaId;

    @Column(name = "usuario_id", nullable = false)
    private UUID usuarioId;

    @Column(name = "motivo", nullable = false)
    private String motivo;

    @Column(name = "fecha", nullable = false)
    private Instant fecha;

    protected DevolucionJpaEntity() {
    }

    public DevolucionJpaEntity(UUID id, UUID ventaId, UUID usuarioId, String motivo, Instant fecha) {
        this.id = id;
        this.ventaId = ventaId;
        this.usuarioId = usuarioId;
        this.motivo = motivo;
        this.fecha = fecha;
    }

    public UUID getId() {
        return id;
    }

    public UUID getVentaId() {
        return ventaId;
    }

    public UUID getUsuarioId() {
        return usuarioId;
    }

    public String getMotivo() {
        return motivo;
    }

    public Instant getFecha() {
        return fecha;
    }
}
