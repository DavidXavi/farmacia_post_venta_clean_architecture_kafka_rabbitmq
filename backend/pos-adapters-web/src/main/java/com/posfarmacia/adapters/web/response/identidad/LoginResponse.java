package com.posfarmacia.adapters.web.response.identidad;

import java.util.Set;
import java.util.UUID;

/**
 * Resultado del login. Si la cuenta tiene MFA activo, la respuesta llega con
 * {@code mfaRequerido = true} y solo {@code mfaToken}: la sesion aun no existe hasta que el
 * cliente presente el codigo de Google Authenticator en /api/auth/mfa/verificar.
 */
public record LoginResponse(
        String token,
        UUID usuarioId,
        String nombreUsuario,
        Set<String> roles,
        Set<String> permisos,
        UUID localId,
        boolean mfaRequerido,
        String mfaToken) {

    public static LoginResponse sesion(String token, UUID usuarioId, String nombreUsuario, Set<String> roles,
                                        Set<String> permisos, UUID localId) {
        return new LoginResponse(token, usuarioId, nombreUsuario, roles, permisos, localId, false, null);
    }

    public static LoginResponse segundoFactorPendiente(String mfaToken) {
        return new LoginResponse(null, null, null, null, null, null, true, mfaToken);
    }
}
