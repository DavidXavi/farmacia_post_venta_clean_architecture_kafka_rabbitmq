package com.posfarmacia.domain.model;

import java.util.Objects;
import java.util.UUID;

/**
 * Base de los agregados/entidades de dominio. Las relaciones entre agregados
 * distintos se expresan por UUID (nunca por referencia de objeto), igual que
 * en arquitectura_2_t2, para mantener los limites de cada agregado.
 */
public abstract class Entidad {

    private final UUID id;

    protected Entidad() {
        this.id = UUID.randomUUID();
    }

    protected Entidad(UUID id) {
        this.id = Objects.requireNonNull(id, "id no puede ser nulo");
    }

    public UUID getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Entidad otra)) return false;
        return id.equals(otra.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
