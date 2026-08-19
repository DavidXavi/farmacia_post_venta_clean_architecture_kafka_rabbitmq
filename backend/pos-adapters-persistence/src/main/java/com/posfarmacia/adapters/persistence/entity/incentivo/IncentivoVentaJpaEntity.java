package com.posfarmacia.adapters.persistence.entity.incentivo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Entidad JPA de {@code incentivos_venta}. Se mapea a/desde
 * {@code com.posfarmacia.domain.model.incentivo.IncentivoVenta} mediante {@code IncentivoVentaMapper};
 * nunca se usa como modelo de negocio directamente.
 */
@Entity
@Table(name = "incentivos_venta")
public class IncentivoVentaJpaEntity {

    @Id
    private UUID id;

    @Column(name = "regla_incentivo_id", nullable = false)
    private UUID reglaIncentivoId;

    @Column(name = "usuario_id", nullable = false)
    private UUID usuarioId;

    @Column(name = "venta_id", nullable = false)
    private UUID ventaId;

    @Column(name = "detalle_venta_id", nullable = false)
    private UUID detalleVentaId;

    @Column(nullable = false)
    private int cantidad;

    @Column(name = "monto_calculado", nullable = false, precision = 12, scale = 2)
    private BigDecimal montoCalculado;

    @Column(nullable = false)
    private Instant fecha;

    protected IncentivoVentaJpaEntity() {
        // JPA
    }

    public IncentivoVentaJpaEntity(UUID id, UUID reglaIncentivoId, UUID usuarioId, UUID ventaId, UUID detalleVentaId,
            int cantidad, BigDecimal montoCalculado, Instant fecha) {
        this.id = id;
        this.reglaIncentivoId = reglaIncentivoId;
        this.usuarioId = usuarioId;
        this.ventaId = ventaId;
        this.detalleVentaId = detalleVentaId;
        this.cantidad = cantidad;
        this.montoCalculado = montoCalculado;
        this.fecha = fecha;
    }

    public UUID getId() {
        return id;
    }

    public UUID getReglaIncentivoId() {
        return reglaIncentivoId;
    }

    public UUID getUsuarioId() {
        return usuarioId;
    }

    public UUID getVentaId() {
        return ventaId;
    }

    public UUID getDetalleVentaId() {
        return detalleVentaId;
    }

    public int getCantidad() {
        return cantidad;
    }

    public BigDecimal getMontoCalculado() {
        return montoCalculado;
    }

    public Instant getFecha() {
        return fecha;
    }
}
