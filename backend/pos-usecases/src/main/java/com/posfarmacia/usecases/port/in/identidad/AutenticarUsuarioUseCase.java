package com.posfarmacia.usecases.port.in.identidad;

import com.posfarmacia.usecases.dto.identidad.UsuarioAutenticado;

/**
 * RF01: valida usuario/contrasena. Lanza {@code CredencialesInvalidasException} si el
 * usuario no existe, la contrasena no coincide o la cuenta no esta activa. No genera JWT.
 */
public interface AutenticarUsuarioUseCase {

    /**
     * Valida el primer factor. Si la cuenta tiene MFA, el resultado lo indica en
     * {@code mfaHabilitado()} y el adaptador REST debe exigir el segundo factor antes de
     * emitir un JWT de sesion completa.
     */
    UsuarioAutenticado autenticar(String nombreUsuario, String password);
}
