package com.posfarmacia.domain.model.identidad;

import com.posfarmacia.domain.model.Entidad;
import java.util.UUID;

/** Sede/local de la farmacia. Equivalente a PosFarmacia.Domain.Entities.Local. */
public final class Local extends Entidad {

    private final String nombre;
    private final String direccion;
    private boolean activo;

    public Local(String nombre, String direccion) {
        super();
        this.nombre = nombre;
        this.direccion = direccion;
        this.activo = true;
    }

    /** Constructor de reconstruccion usado por los mappers de persistencia (preserva el id existente). */
    public Local(UUID id, String nombre, String direccion, boolean activo) {
        super(id);
        this.nombre = nombre;
        this.direccion = direccion;
        this.activo = activo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public boolean isActivo() {
        return activo;
    }

    public void desactivar() {
        this.activo = false;
    }
}
