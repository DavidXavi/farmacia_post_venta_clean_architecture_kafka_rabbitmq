package com.posfarmacia.usecases.port.in.identidad;

import com.posfarmacia.usecases.dto.identidad.UsuarioAutenticado;

/**
 * RF01: valida usuario/contrasena. Lanza {@code CredencialesInvalidasException} si el
 * usuario no existe, la contrasena no coincide o la cuenta no esta activa. No genera JWT.
 */
public interface AutenticarUsuarioUseCase {

    UsuarioAutenticado autenticar(String nombreUsuario, String password);
}
