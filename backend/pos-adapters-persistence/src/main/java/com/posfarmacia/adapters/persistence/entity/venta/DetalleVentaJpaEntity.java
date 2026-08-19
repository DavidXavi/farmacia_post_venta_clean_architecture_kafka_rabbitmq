package com.posfarmacia.adapters.persistence.entity.venta;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "detalles_venta")
public class DetalleVentaJpaEntity {

    @Id
    private UUID id;

    @Column(name = "venta_id", nullable = false)
    private UUID ventaId;

    @Column(name = "producto_id", nullable = false)
    private UUID productoId;

    @Column(name = "cantidad", nullable = false)
    private int cantidad;

    @Column(name = "precio_unitario", nullable = false)
    private BigDecimal precioUnitario;

    @Column(name = "tasa_impuesto", nullable = false)
    private BigDecimal tasaImpuesto;

    @Column(name = "promocion_aplicada_id")
    private UUID promocionAplicadaId;

    @Column(name = "receta_id")
    private UUID recetaId;

    @Column(name = "descuento_monto", nullable = false)
    private BigDecimal descuentoMonto;

    protected DetalleVentaJpaEntity() {
    }

    public DetalleVentaJpaEntity(UUID id, UUID ventaId, UUID productoId, int cantidad, BigDecimal precioUnitario,
            BigDecimal tasaImpuesto, UUID promocionAplicadaId, UUID recetaId, BigDecimal descuentoMonto) {
        this.id = id;
        this.ventaId = ventaId;
        this.productoId = productoId;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.tasaImpuesto = tasaImpuesto;
        this.promocionAplicadaId = promocionAplicadaId;
        this.recetaId = recetaId;
        this.descuentoMonto = descuentoMonto;
    }

    public UUID getId() {
        return id;
    }

    public UUID getVentaId() {
        return ventaId;
    }

    public UUID getProductoId() {
        return productoId;
    }

    public int getCantidad() {
        return cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public BigDecimal getTasaImpuesto() {
        return tasaImpuesto;
    }

    public UUID getPromocionAplicadaId() {
        return promocionAplicadaId;
    }

    public UUID getRecetaId() {
        return recetaId;
    }

    public BigDecimal getDescuentoMonto() {
        return descuentoMonto;
    }
}
