package com.posfarmacia.domain.model.identidad;

import com.posfarmacia.domain.enums.EstadoCuenta;
import com.posfarmacia.domain.enums.PermisoEspecial;
import com.posfarmacia.domain.model.Entidad;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * Cuenta de un trabajador (RF01). Los roles se referencian por UUID hacia el
 * agregado {@link Rol} (equivalente a la coleccion UsuarioRol de arquitectura_2_t2),
 * nunca por objeto, para mantener el limite del agregado.
 */
public final class Usuario extends Entidad {

    private final Set<UUID> rolesIds = new HashSet<>();
    private String nombreUsuario;
    private String passwordHash;
    private UUID localId;
    private EnumSet<PermisoEspecial> permisos;
    private EstadoCuenta estado;

    public Usuario(String nombreUsuario, String passwordHash, UUID localId) {
        super();
        this.nombreUsuario = nombreUsuario;
        this.passwordHash = passwordHash;
        this.localId = localId;
        this.permisos = EnumSet.noneOf(PermisoEspecial.class);
        this.estado = EstadoCuenta.ACTIVO;
    }

    /** Constructor de reconstruccion usado por los mappers de persistencia (preserva el id existente). */
    public Usuario(UUID id, String nombreUsuario, String passwordHash, UUID localId, EstadoCuenta estado,
                   EnumSet<PermisoEspecial> permisos, Set<UUID> rolesIds) {
        super(id);
        this.nombreUsuario = nombreUsuario;
        this.passwordHash = passwordHash;
        this.localId = localId;
        this.estado = Objects.requireNonNull(estado, "el estado no puede ser nulo");
        this.permisos = permisos == null || permisos.isEmpty() ? EnumSet.noneOf(PermisoEspecial.class) : EnumSet.copyOf(permisos);
        if (rolesIds != null) {
            this.rolesIds.addAll(rolesIds);
        }
    }

    public void asignarRol(UUID rolId) {
        rolesIds.add(rolId);
    }

    public void otorgarPermiso(PermisoEspecial permiso) {
        permisos.add(permiso);
    }

    public boolean tienePermiso(PermisoEspecial permiso) {
        return permisos.contains(permiso);
    }

    public void suspender() {
        this.estado = EstadoCuenta.SUSPENDIDO;
    }

    public void activar() {
        this.estado = EstadoCuenta.ACTIVO;
    }

    public boolean estaActivo() {
        return estado == EstadoCuenta.ACTIVO;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public UUID getLocalId() {
        return localId;
    }

    public EstadoCuenta getEstado() {
        return estado;
    }

    public Set<PermisoEspecial> getPermisos() {
        return EnumSet.copyOf(permisos);
    }

    public Set<UUID> getRolesIds() {
        return Set.copyOf(rolesIds);
    }
}
