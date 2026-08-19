package com.posfarmacia.adapters.web.response.identidad;

import com.posfarmacia.domain.model.identidad.Usuario;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record UsuarioResponse(UUID id, String nombreUsuario, UUID localId, String estado, Set<String> roles) {

    public static UsuarioResponse desde(Usuario usuario, Map<UUID, String> nombresRolPorId) {
        Set<String> nombresRoles = usuario.getRolesIds().stream()
                .map(nombresRolPorId::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        return new UsuarioResponse(usuario.getId(), usuario.getNombreUsuario(), usuario.getLocalId(),
                usuario.getEstado().name(), nombresRoles);
    }
}
