package com.posfarmacia.usecases.usecase.identidad;

import com.posfarmacia.usecases.port.in.identidad.GestionarUsuarioUseCase;
import com.posfarmacia.usecases.port.out.identidad.PasswordHasherPort;
import com.posfarmacia.usecases.port.out.identidad.RolRepositoryPort;
import com.posfarmacia.usecases.port.out.identidad.UsuarioRepositoryPort;
import com.posfarmacia.domain.enums.RolNombre;
import com.posfarmacia.domain.exception.EntidadNoEncontradaException;
import com.posfarmacia.domain.exception.ValorInvalidoException;
import com.posfarmacia.domain.model.identidad.Rol;
import com.posfarmacia.domain.model.identidad.Usuario;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

/**
 * RF01: alta y consulta de usuarios del sistema. Equivalente a
 * RegistrarUsuarioUseCase/ConsultarUsuariosUseCase (.NET).
 */
public class GestionarUsuarioUseCaseImpl implements GestionarUsuarioUseCase {

    private final UsuarioRepositoryPort usuarios;
    private final RolRepositoryPort roles;
    private final PasswordHasherPort passwordHasher;

    public GestionarUsuarioUseCaseImpl(UsuarioRepositoryPort usuarios, RolRepositoryPort roles,
            PasswordHasherPort passwordHasher) {
        this.usuarios = usuarios;
        this.roles = roles;
        this.passwordHasher = passwordHasher;
    }

    @Override
    @Transactional
    public Usuario registrar(String nombreUsuario, String password, UUID localId, Set<String> nombresRoles) {
        if (usuarios.buscarPorNombreUsuario(nombreUsuario).isPresent()) {
            throw new ValorInvalidoException("Ya existe un usuario con ese nombre de usuario.");
        }

        Usuario usuario = new Usuario(nombreUsuario, passwordHasher.hash(password), localId);
        for (String nombreRol : nombresRoles) {
            Rol rol = roles.buscarPorNombre(RolNombre.valueOf(nombreRol))
                    .orElseThrow(() -> new EntidadNoEncontradaException("El rol " + nombreRol + " no existe."));
            usuario.asignarRol(rol.getId());
        }

        return usuarios.guardar(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listar() {
        return usuarios.listarTodos();
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario obtenerPorId(UUID id) {
        return usuarios.buscarPorId(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("El usuario indicado no existe."));
    }
}
