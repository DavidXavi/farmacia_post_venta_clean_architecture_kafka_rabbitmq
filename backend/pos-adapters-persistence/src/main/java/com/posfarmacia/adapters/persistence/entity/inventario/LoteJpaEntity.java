package com.posfarmacia.adapters.persistence.entity.inventario;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "lotes")
public class LoteJpaEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String codigo;

    @Column(name = "producto_id", nullable = false)
    private UUID productoId;

    @Column(name = "fecha_vencimiento", nullable = false)
    private LocalDate fechaVencimiento;

    @Column(name = "cantidad_recibida", nullable = false)
    private int cantidadRecibida;

    @Column(name = "cantidad_disponible", nullable = false)
    private int cantidadDisponible;

    @Column(precision = 12, scale = 2)
    private BigDecimal costo;

    @Column(name = "local_id", nullable = false)
    private UUID localId;

    @Column(nullable = false)
    private String estado;

    protected LoteJpaEntity() {
    }

    public LoteJpaEntity(UUID id, String codigo, UUID productoId, LocalDate fechaVencimiento, int cantidadRecibida,
            int cantidadDisponible, BigDecimal costo, UUID localId, String estado) {
        this.id = id;
        this.codigo = codigo;
        this.productoId = productoId;
        this.fechaVencimiento = fechaVencimiento;
        this.cantidadRecibida = cantidadRecibida;
        this.cantidadDisponible = cantidadDisponible;
        this.costo = costo;
        this.localId = localId;
        this.estado = estado;
    }

    public UUID getId() {
        return id;
    }

    public String getCodigo() {
        return codigo;
    }

    public UUID getProductoId() {
        return productoId;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public int getCantidadRecibida() {
        return cantidadRecibida;
    }

    public int getCantidadDisponible() {
        return cantidadDisponible;
    }

    public BigDecimal getCosto() {
        return costo;
    }

    public UUID getLocalId() {
        return localId;
    }

    public String getEstado() {
        return estado;
    }
}
