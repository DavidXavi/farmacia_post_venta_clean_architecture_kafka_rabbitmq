package com.posfarmacia.usecases.port.out.identidad;

import com.posfarmacia.domain.model.identidad.Usuario;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepositoryPort {

    Optional<Usuario> buscarPorId(UUID id);

    Optional<Usuario> buscarPorNombreUsuario(String nombreUsuario);

    Usuario guardar(Usuario usuario);

    /** RF01: listado de usuarios registrados, reservado al rol Administrador. */
    List<Usuario> listarTodos();
}
