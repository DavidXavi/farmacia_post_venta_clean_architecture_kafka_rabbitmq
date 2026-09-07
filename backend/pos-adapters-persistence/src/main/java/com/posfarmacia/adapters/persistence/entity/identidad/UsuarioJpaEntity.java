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

    /** Nulo en cuentas creadas desde un proveedor social: no tienen contrasena local. */
    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "estado", nullable = false)
    private String estado;

    @Column(name = "local_id", nullable = false)
    private UUID localId;

    /** Nombres de PermisoEspecial separados por coma; ver UsuarioMapper. */
    @Column(name = "permisos", nullable = false)
    private String permisos;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "proveedor_oauth")
    private String proveedorOauth;

    @Column(name = "mfa_secret")
    private String mfaSecret;

    @Column(name = "mfa_habilitado", nullable = false)
    private boolean mfaHabilitado;

    protected UsuarioJpaEntity() {
    }

    public UsuarioJpaEntity(UUID id, String nombreUsuario, String passwordHash, String estado, UUID localId,
                             String permisos, String email, String proveedorOauth, String mfaSecret,
                             boolean mfaHabilitado) {
        this.id = id;
        this.nombreUsuario = nombreUsuario;
        this.passwordHash = passwordHash;
        this.estado = estado;
        this.localId = localId;
        this.permisos = permisos;
        this.email = email;
        this.proveedorOauth = proveedorOauth;
        this.mfaSecret = mfaSecret;
        this.mfaHabilitado = mfaHabilitado;
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

    public String getEmail() {
        return email;
    }

    public String getProveedorOauth() {
        return proveedorOauth;
    }

    public String getMfaSecret() {
        return mfaSecret;
    }

    public boolean isMfaHabilitado() {
        return mfaHabilitado;
    }
}
