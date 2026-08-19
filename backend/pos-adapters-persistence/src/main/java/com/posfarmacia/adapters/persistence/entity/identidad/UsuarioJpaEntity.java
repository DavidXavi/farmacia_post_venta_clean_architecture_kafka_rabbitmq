package com.posfarmacia.adapters.persistence.entity.identidad;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "usuarios")
public class UsuarioJpaEntity {

    @Id
    private UUID id;

    @Column(name = "nombre_usuario", nullable = false, unique = true)
    private String nombreUsuario;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "estado", nullable = false)
    private String estado;

    @Column(name = "local_id", nullable = false)
    private UUID localId;

    /** Nombres de PermisoEspecial separados por coma; ver UsuarioMapper. */
    @Column(name = "permisos", nullable = false)
    private String permisos;

    protected UsuarioJpaEntity() {
    }

    public UsuarioJpaEntity(UUID id, String nombreUsuario, String passwordHash, String estado, UUID localId,
                             String permisos) {
        this.id = id;
        this.nombreUsuario = nombreUsuario;
        this.passwordHash = passwordHash;
        this.estado = estado;
        this.localId = localId;
        this.permisos = permisos;
    }

    public UUID getId() {
        return id;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getEstado() {
        return estado;
    }

    public UUID getLocalId() {
        return localId;
    }

    public String getPermisos() {
        return permisos;
    }
}
