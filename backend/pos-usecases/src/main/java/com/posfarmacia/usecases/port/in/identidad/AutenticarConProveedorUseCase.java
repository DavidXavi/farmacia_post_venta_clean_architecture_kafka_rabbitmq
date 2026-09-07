package com.posfarmacia.usecases.port.in.identidad;

import com.posfarmacia.usecases.dto.identidad.UsuarioAutenticado;

/**
 * RF01: autentica a un usuario que ya se identifico ante un proveedor social (Google, Facebook).
 * Si el correo no corresponde a ninguna cuenta, se crea una cuenta local vinculada al proveedor.
 * No genera JWT: eso es responsabilidad del adaptador de entrada REST.
 */
public interface AutenticarConProveedorUseCase {

    UsuarioAutenticado autenticar(String proveedor, String email, String nombreMostrado);
}
