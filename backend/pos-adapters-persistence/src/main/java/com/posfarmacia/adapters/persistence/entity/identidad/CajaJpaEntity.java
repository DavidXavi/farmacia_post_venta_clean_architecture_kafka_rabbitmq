package com.posfarmacia.adapters.persistence.entity.identidad;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "cajas")
public class CajaJpaEntity {

    @Id
    private UUID id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "local_id", nullable = false)
    private UUID localId;

    @Column(name = "activa", nullable = false)
    private boolean activa;

    protected CajaJpaEntity() {
    }

    public CajaJpaEntity(UUID id, String nombre, UUID localId, boolean activa) {
        this.id = id;
        this.nombre = nombre;
        this.localId = localId;
        this.activa = activa;
    }

    public UUID getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public UUID getLocalId() {
        return localId;
    }

    public boolean isActiva() {
        return activa;
    }
}
