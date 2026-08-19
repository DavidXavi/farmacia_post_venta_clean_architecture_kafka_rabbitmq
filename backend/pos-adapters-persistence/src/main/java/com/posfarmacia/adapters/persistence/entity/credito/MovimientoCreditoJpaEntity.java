package com.posfarmacia.adapters.persistence.entity.credito;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "movimientos_credito")
public class MovimientoCreditoJpaEntity {

    @Id
    private UUID id;

    @Column(name = "linea_credito_id", nullable = false)
    private UUID lineaCreditoId;

    @Column(name = "venta_id")
    private UUID ventaId;

    @Column(nullable = false, length = 20)
    private String tipo;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal monto;

    @Column(nullable = false)
    private Instant fecha;

    protected MovimientoCreditoJpaEntity() {
    }

    public MovimientoCreditoJpaEntity(UUID id, UUID lineaCreditoId, UUID ventaId, String tipo, BigDecimal monto, Instant fecha) {
        this.id = id;
        this.lineaCreditoId = lineaCreditoId;
        this.ventaId = ventaId;
        this.tipo = tipo;
        this.monto = monto;
        this.fecha = fecha;
    }

    public UUID getId() {
        return id;
    }

    public UUID getLineaCreditoId() {
        return lineaCreditoId;
    }

    public UUID getVentaId() {
        return ventaId;
    }

    public String getTipo() {
        return tipo;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public Instant getFecha() {
        return fecha;
    }
}
