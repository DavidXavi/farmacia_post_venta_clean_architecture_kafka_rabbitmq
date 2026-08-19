package com.posfarmacia.usecases.usecase.identidad;

import com.posfarmacia.usecases.dto.identidad.UsuarioAutenticado;
import com.posfarmacia.usecases.port.in.identidad.AutenticarUsuarioUseCase;
import com.posfarmacia.usecases.port.out.identidad.PasswordHasherPort;
import com.posfarmacia.usecases.port.out.identidad.RolRepositoryPort;
import com.posfarmacia.usecases.port.out.identidad.UsuarioRepositoryPort;
import com.posfarmacia.domain.enums.RolNombre;
import com.posfarmacia.domain.exception.CredencialesInvalidasException;
import com.posfarmacia.domain.model.identidad.Rol;
import com.posfarmacia.domain.model.identidad.Usuario;
import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
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

        if (!usuario.estaActivo() || !passwordHasher.verificar(password, usuario.getPasswordHash())) {
            throw new CredencialesInvalidasException();
        }

        Set<RolNombre> nombresRoles = usuario.getRolesIds().stream()
                .map(roles::buscarPorId)
                .flatMap(Optional::stream)
                .map(Rol::getNombre)
                .collect(Collectors.toCollection(() -> EnumSet.noneOf(RolNombre.class)));

        return new UsuarioAutenticado(
                usuario.getId(),
                usuario.getNombreUsuario(),
                nombresRoles,
                usuario.getPermisos(),
                usuario.getLocalId());
    }
}
