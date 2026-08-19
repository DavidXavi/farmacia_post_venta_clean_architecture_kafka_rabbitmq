package com.posfarmacia.adapters.persistence.entity.anulacion;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "detalle_devoluciones")
public class DetalleDevolucionJpaEntity {

    @Id
    private UUID id;

    @Column(name = "devolucion_id", nullable = false)
    private UUID devolucionId;

    @Column(name = "detalle_venta_id", nullable = false)
    private UUID detalleVentaId;

    @Column(name = "producto_id", nullable = false)
    private UUID productoId;

    @Column(name = "cantidad", nullable = false)
    private int cantidad;

    @Column(name = "monto_devuelto", nullable = false)
    private BigDecimal montoDevuelto;

    protected DetalleDevolucionJpaEntity() {
    }

    public DetalleDevolucionJpaEntity(UUID id, UUID devolucionId, UUID detalleVentaId, UUID productoId, int cantidad,
            BigDecimal montoDevuelto) {
        this.id = id;
        this.devolucionId = devolucionId;
        this.detalleVentaId = detalleVentaId;
        this.productoId = productoId;
        this.cantidad = cantidad;
        this.montoDevuelto = montoDevuelto;
    }

    public UUID getId() {
        return id;
    }

    public UUID getDevolucionId() {
        return devolucionId;
    }

    public UUID getDetalleVentaId() {
        return detalleVentaId;
    }

    public UUID getProductoId() {
        return productoId;
    }

    public int getCantidad() {
        return cantidad;
    }

    public BigDecimal getMontoDevuelto() {
        return montoDevuelto;
    }
}
