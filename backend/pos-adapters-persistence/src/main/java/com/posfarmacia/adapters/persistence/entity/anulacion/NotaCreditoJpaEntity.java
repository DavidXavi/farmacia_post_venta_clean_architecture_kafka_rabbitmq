package com.posfarmacia.adapters.persistence.entity.anulacion;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "notas_credito")
public class NotaCreditoJpaEntity {

    @Id
    private UUID id;

    @Column(name = "venta_id", nullable = false)
    private UUID ventaId;

    @Column(name = "comprobante_id", nullable = false)
    private UUID comprobanteId;

    @Column(name = "usuario_id", nullable = false)
    private UUID usuarioId;

    @Column(name = "motivo", nullable = false)
    private String motivo;

    @Column(name = "monto_total", nullable = false)
    private BigDecimal montoTotal;

    @Column(name = "fecha", nullable = false)
    private Instant fecha;

    protected NotaCreditoJpaEntity() {
    }

    public NotaCreditoJpaEntity(UUID id, UUID ventaId, UUID comprobanteId, UUID usuarioId, String motivo,
            BigDecimal montoTotal, Instant fecha) {
        this.id = id;
        this.ventaId = ventaId;
        this.comprobanteId = comprobanteId;
        this.usuarioId = usuarioId;
        this.motivo = motivo;
        this.montoTotal = montoTotal;
        this.fecha = fecha;
    }

    public UUID getId() {
        return id;
    }

    public UUID getVentaId() {
        return ventaId;
    }

    public UUID getComprobanteId() {
        return comprobanteId;
    }

    public UUID getUsuarioId() {
        return usuarioId;
    }

    public String getMotivo() {
        return motivo;
    }

    public BigDecimal getMontoTotal() {
        return montoTotal;
    }

    public Instant getFecha() {
        return fecha;
    }
}
