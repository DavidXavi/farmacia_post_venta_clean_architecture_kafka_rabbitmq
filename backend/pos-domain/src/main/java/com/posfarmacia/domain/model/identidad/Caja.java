package com.posfarmacia.domain.model.identidad;

import com.posfarmacia.domain.model.Entidad;
import java.util.UUID;

/** Caja registradora fisica de un local (RF02). Equivalente a PosFarmacia.Domain.Entities.Caja. */
public final class Caja extends Entidad {

    private final String nombre;
    private final UUID localId;
    private boolean activa;

    public Caja(String nombre, UUID localId) {
        super();
        this.nombre = nombre;
        this.localId = localId;
        this.activa = true;
    }

    /** Constructor de reconstruccion usado por los mappers de persistencia (preserva el id existente). */
    public Caja(UUID id, String nombre, UUID localId, boolean activa) {
        super(id);
        this.nombre = nombre;
        this.localId = localId;
        this.activa = activa;
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

    public void desactivar() {
        this.activa = false;
    }
}
