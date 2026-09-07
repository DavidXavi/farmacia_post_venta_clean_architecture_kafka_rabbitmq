package com.posfarmacia.usecases.usecase.identidad;

import com.posfarmacia.usecases.dto.identidad.UsuarioAutenticado;
import com.posfarmacia.usecases.port.in.identidad.AutenticarConProveedorUseCase;
import com.posfarmacia.usecases.port.out.identidad.LocalRepositoryPort;
import com.posfarmacia.usecases.port.out.identidad.RolRepositoryPort;
import com.posfarmacia.usecases.port.out.identidad.UsuarioRepositoryPort;
import com.posfarmacia.domain.enums.RolNombre;
import com.posfarmacia.domain.exception.CredencialesInvalidasException;
import com.posfarmacia.domain.model.identidad.Local;
import com.posfarmacia.domain.model.identidad.Usuario;
import org.springframework.transaction.annotation.Transactional;

/**
 * RF01: login social. El proveedor (Google/Facebook) ya verifico la identidad; aqui solo se
 * resuelve a que cuenta del POS corresponde ese correo. Si es la primera vez, se da de alta
 * una cuenta sin contrasena con el rol CAJERO (el minimo) para que un administrador la ajuste.
 */
public class AutenticarConProveedorUseCaseImpl implements AutenticarConProveedorUseCase {

    private final UsuarioRepositoryPort usuarios;
    private final RolRepositoryPort roles;
    private final LocalRepositoryPort locales;

    public AutenticarConProveedorUseCaseImpl(UsuarioRepositoryPort usuarios, RolRepositoryPort roles,
                                              LocalRepositoryPort locales) {
        this.usuarios = usuarios;
        this.roles = roles;
        this.locales = locales;
    }

    @Override
    @Transactional
    public UsuarioAutenticado autenticar(String proveedor, String email, String nombreMostrado) {
        if (email == null || email.isBlank()) {
            throw new CredencialesInvalidasException("El proveedor " + proveedor + " no entrego un correo verificado.");
        }

        Usuario usuario = usuarios.buscarPorEmail(email)
                .orElseGet(() -> registrar(proveedor, email, nombreMostrado));

        if (!usuario.estaActivo()) {
            throw new CredencialesInvalidasException("La cuenta esta suspendida.");
        }

        // Vuelve a marcar el proveedor por si la cuenta ya existia como cuenta local con ese correo.
        usuario.vincularCuentaSocial(proveedor, email);
        usuarios.guardar(usuario);

        return UsuarioAutenticadoFactory.de(usuario, roles);
    }

    private Usuario registrar(String proveedor, String email, String nombreMostrado) {
        Local local = locales.listarTodos().stream()
                .filter(Local::isActivo)
                .findFirst()
                .orElseThrow(() -> new CredencialesInvalidasException("No hay un local activo al cual asignar la cuenta."));

        String nombreUsuario = nombreUsuarioLibre(email);
        Usuario nuevo = Usuario.deProveedorSocial(nombreUsuario, email, proveedor, local.getId());
        roles.buscarPorNombre(RolNombre.CAJERO).ifPresent(rol -> nuevo.asignarRol(rol.getId()));
        return usuarios.guardar(nuevo);
    }

    /** El correo es unico, pero el nombre de usuario tambien: se desambigua con un sufijo. */
    private String nombreUsuarioLibre(String email) {
        String base = email.split("@")[0];
        String candidato = base;
        for (int sufijo = 2; usuarios.buscarPorNombreUsuario(candidato).isPresent(); sufijo++) {
            candidato = base + sufijo;
        }
        return candidato;
    }
}
