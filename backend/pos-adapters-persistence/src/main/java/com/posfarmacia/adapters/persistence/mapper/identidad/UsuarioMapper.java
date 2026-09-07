package com.posfarmacia.adapters.persistence.mapper.identidad;

import com.posfarmacia.adapters.persistence.entity.identidad.UsuarioJpaEntity;
import com.posfarmacia.domain.enums.EstadoCuenta;
import com.posfarmacia.domain.enums.PermisoEspecial;
import com.posfarmacia.domain.model.identidad.Usuario;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/** Traduce entre el agregado de dominio Usuario y su fila JPA + sus filas de usuarios_roles. */
public final class UsuarioMapper {

    private UsuarioMapper() {
    }

    public static Usuario aDominio(UsuarioJpaEntity entity, Set<UUID> rolesIds) {
        EnumSet<PermisoEspecial> permisos = entity.getPermisos() == null || entity.getPermisos().isBlank()
                ? EnumSet.noneOf(PermisoEspecial.class)
                : Arrays.stream(entity.getPermisos().split(","))
                        .map(PermisoEspecial::valueOf)
                        .collect(Collectors.toCollection(() -> EnumSet.noneOf(PermisoEspecial.class)));

        Usuario usuario = new Usuario(
                entity.getId(),
                entity.getNombreUsuario(),
                entity.getPasswordHash(),
                entity.getLocalId(),
                EstadoCuenta.valueOf(entity.getEstado()),
                permisos,
                rolesIds);

        if (entity.getProveedorOauth() != null || entity.getEmail() != null) {
            usuario.vincularCuentaSocial(entity.getProveedorOauth(), entity.getEmail());
        }
        if (entity.getMfaSecret() != null) {
            usuario.prepararMfa(entity.getMfaSecret());
            if (entity.isMfaHabilitado()) {
                usuario.confirmarMfa();
            }
        }
        return usuario;
    }

    public static UsuarioJpaEntity aEntidad(Usuario usuario) {
        String permisos = usuario.getPermisos().stream()
                .map(Enum::name)
                .collect(Collectors.joining(","));

        return new UsuarioJpaEntity(
                usuario.getId(),
                usuario.getNombreUsuario(),
                usuario.getPasswordHash(),
                usuario.getEstado().name(),
                usuario.getLocalId(),
                permisos,
                usuario.getEmail(),
                usuario.getProveedorOauth(),
                usuario.getMfaSecret(),
                usuario.tieneMfaHabilitado());
    }
}
