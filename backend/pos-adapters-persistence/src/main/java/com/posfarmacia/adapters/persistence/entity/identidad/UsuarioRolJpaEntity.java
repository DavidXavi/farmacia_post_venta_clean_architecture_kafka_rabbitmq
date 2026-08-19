package com.posfarmacia.adapters.persistence.entity.identidad;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.IdClass;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "usuarios_roles")
@IdClass(UsuarioRolId.class)
public class UsuarioRolJpaEntity {

    @Id
    @Column(name = "usuario_id")
    private UUID usuarioId;

    @Id
    @Column(name = "rol_id")
    private UUID rolId;

    protected UsuarioRolJpaEntity() {
    }

    public UsuarioRolJpaEntity(UUID usuarioId, UUID rolId) {
        this.usuarioId = usuarioId;
        this.rolId = rolId;
    }

    public UUID getUsuarioId() {
        return usuarioId;
    }

    public UUID getRolId() {
        return rolId;
    }
}
