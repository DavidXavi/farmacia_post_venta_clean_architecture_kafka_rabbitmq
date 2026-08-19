package com.posfarmacia.domain.model.identidad;

import com.posfarmacia.domain.enums.RolNombre;
import com.posfarmacia.domain.model.Entidad;
import java.util.Objects;
import java.util.UUID;

/** Catalogo de roles del sistema (RF01). Equivalente a PosFarmacia.Domain.Entities.Rol. */
public final class Rol extends Entidad {

    private final RolNombre nombre;
    private final String descripcion;

    public Rol(RolNombre nombre, String descripcion) {
        super();
        this.nombre = Objects.requireNonNull(nombre, "el nombre del rol no puede ser nulo");
        this.descripcion = descripcion == null ? "" : descripcion;
    }

    /** Constructor de reconstruccion usado por los mappers de persistencia (preserva el id existente). */
    public Rol(UUID id, RolNombre nombre, String descripcion) {
        super(id);
        this.nombre = Objects.requireNonNull(nombre, "el nombre del rol no puede ser nulo");
        this.descripcion = descripcion == null ? "" : descripcion;
    }

    public RolNombre getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
