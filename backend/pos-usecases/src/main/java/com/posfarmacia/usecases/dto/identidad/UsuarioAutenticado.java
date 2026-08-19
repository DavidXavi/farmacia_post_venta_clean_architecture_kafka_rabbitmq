package com.posfarmacia.usecases.dto.identidad;

import com.posfarmacia.domain.enums.PermisoEspecial;
import com.posfarmacia.domain.enums.RolNombre;
import java.util.Set;
import java.util.UUID;

/**
 * Resultado de una autenticacion exitosa (RF01). No incluye el JWT: eso lo emite
 * el adaptador de entrada REST (JwtTokenIssuer), la aplicacion solo certifica identidad.
 */
public record UsuarioAutenticado(
        UUID usuarioId,
        String nombreUsuario,
        Set<RolNombre> roles,
        Set<PermisoEspecial> permisos,
        UUID localId) {
}
