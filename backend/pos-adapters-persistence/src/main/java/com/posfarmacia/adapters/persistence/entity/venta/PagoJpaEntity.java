package com.posfarmacia.adapters.persistence.entity.venta;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "pagos")
public class PagoJpaEntity {

    @Id
    private UUID id;

    @Column(name = "venta_id", nullable = false)
    private UUID ventaId;

    @Column(name = "forma_pago_id", nullable = false)
    private UUID formaPagoId;

    @Column(name = "monto", nullable = false)
    private BigDecimal monto;

    @Column(name = "codigo_autorizacion")
    private String codigoAutorizacion;

    @Column(name = "fecha", nullable = false)
    private Instant fecha;

    protected PagoJpaEntity() {
    }

    public PagoJpaEntity(UUID id, UUID ventaId, UUID formaPagoId, BigDecimal monto, String codigoAutorizacion,
            Instant fecha) {
        this.id = id;
        this.ventaId = ventaId;
        this.formaPagoId = formaPagoId;
        this.monto = monto;
        this.codigoAutorizacion = codigoAutorizacion;
        this.fecha = fecha;
    }

    public UUID getId() {
        return id;
    }

    public UUID getVentaId() {
        return ventaId;
    }

    public UUID getFormaPagoId() {
        return formaPagoId;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public String getCodigoAutorizacion() {
        return codigoAutorizacion;
    }

    public Instant getFecha() {
        return fecha;
    }
}
