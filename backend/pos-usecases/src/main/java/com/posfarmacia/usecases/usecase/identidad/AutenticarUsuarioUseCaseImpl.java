package com.posfarmacia.usecases.usecase.identidad;

import com.posfarmacia.usecases.dto.identidad.UsuarioAutenticado;
import com.posfarmacia.usecases.port.in.identidad.AutenticarUsuarioUseCase;
import com.posfarmacia.usecases.port.out.identidad.PasswordHasherPort;
import com.posfarmacia.usecases.port.out.identidad.RolRepositoryPort;
import com.posfarmacia.usecases.port.out.identidad.UsuarioRepositoryPort;
import com.posfarmacia.domain.exception.CredencialesInvalidasException;
import com.posfarmacia.domain.model.identidad.Usuario;
import org.springframework.transaction.annotation.Transactional;

/** RF01: autentica un usuario y devuelve su identidad, sin generar el JWT (responsabilidad del adaptador REST). */
public class AutenticarUsuarioUseCaseImpl implements AutenticarUsuarioUseCase {

    private final UsuarioRepositoryPort usuarios;
    private final RolRepositoryPort roles;
    private final PasswordHasherPort passwordHasher;

    public AutenticarUsuarioUseCaseImpl(UsuarioRepositoryPort usuarios, RolRepositoryPort roles,
                                         PasswordHasherPort passwordHasher) {
        this.usuarios = usuarios;
        this.roles = roles;
        this.passwordHasher = passwordHasher;
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioAutenticado autenticar(String nombreUsuario, String password) {
        Usuario usuario = usuarios.buscarPorNombreUsuario(nombreUsuario)
                .orElseThrow(CredencialesInvalidasException::new);

        if (!usuario.estaActivo()
                || usuario.getPasswordHash() == null
                || !passwordHasher.verificar(password, usuario.getPasswordHash())) {
            throw new CredencialesInvalidasException();
        }

        return UsuarioAutenticadoFactory.de(usuario, roles);
    }
}
