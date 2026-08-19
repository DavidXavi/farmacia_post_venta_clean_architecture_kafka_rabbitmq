package com.posfarmacia.adapters.persistence.entity.identidad;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class UsuarioRolId implements Serializable {

    private UUID usuarioId;
    private UUID rolId;

    public UsuarioRolId() {
    }

    public UsuarioRolId(UUID usuarioId, UUID rolId) {
        this.usuarioId = usuarioId;
        this.rolId = rolId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UsuarioRolId that)) return false;
        return Objects.equals(usuarioId, that.usuarioId) && Objects.equals(rolId, that.rolId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(usuarioId, rolId);
    }
}
