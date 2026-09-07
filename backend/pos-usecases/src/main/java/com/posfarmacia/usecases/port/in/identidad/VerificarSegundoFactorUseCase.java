package com.posfarmacia.usecases.port.in.identidad;

import com.posfarmacia.usecases.dto.identidad.UsuarioAutenticado;
import java.util.UUID;

/**
 * RF01: segundo paso del login cuando la cuenta tiene MFA. Recibe el usuario ya validado en
 * el primer factor (contrasena o proveedor social) y el codigo de Google Authenticator.
 * Lanza {@code MfaRequeridaException} si el codigo no es valido.
 */
public interface VerificarSegundoFactorUseCase {

    UsuarioAutenticado verificar(UUID usuarioId, String codigo);
}
