package com.posfarmacia.usecases.usecase.identidad;

import com.posfarmacia.usecases.dto.identidad.UsuarioAutenticado;
import com.posfarmacia.usecases.port.out.identidad.RolRepositoryPort;
import com.posfarmacia.domain.enums.RolNombre;
import com.posfarmacia.domain.model.identidad.Rol;
import com.posfarmacia.domain.model.identidad.Usuario;
import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/** Arma el DTO de identidad resolviendo los nombres de rol; comun a los dos flujos de login. */
final class UsuarioAutenticadoFactory {

    private UsuarioAutenticadoFactory() {
    }

    static UsuarioAutenticado de(Usuario usuario, RolRepositoryPort roles) {
        Set<RolNombre> nombresRoles = usuario.getRolesIds().stream()
                .map(roles::buscarPorId)
                .flatMap(Optional::stream)
                .map(Rol::getNombre)
                .collect(Collectors.toCollection(() -> EnumSet.noneOf(RolNombre.class)));

        return new UsuarioAutenticado(
                usuario.getId(),
                usuario.getNombreUsuario(),
                nombresRoles,
                usuario.getPermisos(),
                usuario.getLocalId(),
                usuario.tieneMfaHabilitado());
    }
}
