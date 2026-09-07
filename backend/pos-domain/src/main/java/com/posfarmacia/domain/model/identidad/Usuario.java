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
    /** Correo con el que el usuario inicia sesion via un proveedor social (RF01); null si solo usa contrasena. */
    private String email;
    /** Proveedor social vinculado ("google", "facebook"); null si la cuenta es local. */
    private String proveedorOauth;
    /** Secreto TOTP compartido con Google Authenticator; null si el usuario nunca inicio el registro de MFA. */
    private String mfaSecret;
    private boolean mfaHabilitado;

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

    /** Crea la cuenta de un usuario que se autentica con un proveedor social, sin contrasena local. */
    public static Usuario deProveedorSocial(String nombreUsuario, String email, String proveedor, UUID localId) {
        Usuario usuario = new Usuario(nombreUsuario, null, localId);
        usuario.vincularCuentaSocial(proveedor, email);
        return usuario;
    }

    public void vincularCuentaSocial(String proveedor, String email) {
        this.proveedorOauth = proveedor;
        this.email = email;
    }

    /**
     * Guarda el secreto TOTP recien generado. El MFA queda pendiente hasta que el usuario
     * demuestre, con un codigo valido, que su app de autenticacion quedo bien configurada.
     */
    public void prepararMfa(String secreto) {
        this.mfaSecret = Objects.requireNonNull(secreto, "el secreto MFA no puede ser nulo");
        this.mfaHabilitado = false;
    }

    public void confirmarMfa() {
        if (mfaSecret == null) {
            throw new IllegalStateException("no hay un secreto MFA pendiente de confirmar");
        }
        this.mfaHabilitado = true;
    }

    public void deshabilitarMfa() {
        this.mfaSecret = null;
        this.mfaHabilitado = false;
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

    public String getEmail() {
        return email;
    }

    public String getProveedorOauth() {
        return proveedorOauth;
    }

    public String getMfaSecret() {
        return mfaSecret;
    }

    public boolean tieneMfaHabilitado() {
        return mfaHabilitado;
    }

    public Set<UUID> getRolesIds() {
        return Set.copyOf(rolesIds);
    }
}
