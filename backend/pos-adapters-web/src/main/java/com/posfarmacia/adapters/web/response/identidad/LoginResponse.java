package com.posfarmacia.adapters.web.response.identidad;

import java.util.Set;
import java.util.UUID;

public record LoginResponse(
        String token,
        UUID usuarioId,
        String nombreUsuario,
        Set<String> roles,
        Set<String> permisos,
        UUID localId) {
}
