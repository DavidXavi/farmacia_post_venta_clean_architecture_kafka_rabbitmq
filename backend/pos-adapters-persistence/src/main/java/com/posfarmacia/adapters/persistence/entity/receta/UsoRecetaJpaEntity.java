package com.posfarmacia.adapters.persistence.entity.receta;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "usos_receta")
public class UsoRecetaJpaEntity {

    @Id
    private UUID id;

    @Column(name = "receta_id", nullable = false)
    private UUID recetaId;

    @Column(name = "venta_id", nullable = false)
    private UUID ventaId;

    @Column(name = "fecha", nullable = false)
    private Instant fecha;

    public UsoRecetaJpaEntity() {
        // JPA
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getRecetaId() {
        return recetaId;
    }

    public void setRecetaId(UUID recetaId) {
        this.recetaId = recetaId;
    }

    public UUID getVentaId() {
        return ventaId;
    }

    public void setVentaId(UUID ventaId) {
        this.ventaId = ventaId;
    }

    public Instant getFecha() {
        return fecha;
    }

    public void setFecha(Instant fecha) {
        this.fecha = fecha;
    }
}
