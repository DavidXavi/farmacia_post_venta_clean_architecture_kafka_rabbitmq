package com.posfarmacia.adapters.persistence.entity.inventario;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "existencias_lote")
public class ExistenciaLoteJpaEntity {

    @Id
    private UUID id;

    @Column(name = "producto_id", nullable = false)
    private UUID productoId;

    @Column(name = "local_id", nullable = false)
    private UUID localId;

    @Column(name = "cantidad_actual", nullable = false)
    private int cantidadActual;

    @Column(name = "actualizado_en", nullable = false)
    private Instant actualizadoEn;

    protected ExistenciaLoteJpaEntity() {
    }

    public ExistenciaLoteJpaEntity(UUID id, UUID productoId, UUID localId, int cantidadActual, Instant actualizadoEn) {
        this.id = id;
        this.productoId = productoId;
        this.localId = localId;
        this.cantidadActual = cantidadActual;
        this.actualizadoEn = actualizadoEn;
    }

    public UUID getId() {
        return id;
    }

    public UUID getProductoId() {
        return productoId;
    }

    public UUID getLocalId() {
        return localId;
    }

    public int getCantidadActual() {
        return cantidadActual;
    }

    public Instant getActualizadoEn() {
        return actualizadoEn;
    }
}
