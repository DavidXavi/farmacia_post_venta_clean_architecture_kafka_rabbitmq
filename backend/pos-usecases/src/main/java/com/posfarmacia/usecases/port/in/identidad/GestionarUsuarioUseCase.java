package com.posfarmacia.usecases.port.in.identidad;

import com.posfarmacia.domain.model.identidad.Usuario;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * RF01: alta y consulta de usuarios del sistema, reservado al rol Administrador.
 * Equivalente a RegistrarUsuarioUseCase/ConsultarUsuariosUseCase (.NET).
 */
public interface GestionarUsuarioUseCase {

    /**
     * Registra un nuevo usuario con los roles indicados (nombres de {@code RolNombre}).
     * Falla con {@code ValorInvalidoException} si ya existe un usuario con ese nombre de
     * usuario y con {@code EntidadNoEncontradaException} si algun rol indicado no existe.
     */
    Usuario registrar(String nombreUsuario, String password, UUID localId, Set<String> nombresRoles);

    List<Usuario> listar();

    Usuario obtenerPorId(UUID id);
}
