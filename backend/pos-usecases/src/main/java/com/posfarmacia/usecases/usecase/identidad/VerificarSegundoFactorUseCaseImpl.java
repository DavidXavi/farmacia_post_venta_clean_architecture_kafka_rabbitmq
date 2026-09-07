package com.posfarmacia.usecases.usecase.identidad;

import com.posfarmacia.usecases.dto.identidad.UsuarioAutenticado;
import com.posfarmacia.usecases.port.in.identidad.VerificarSegundoFactorUseCase;
import com.posfarmacia.usecases.port.out.identidad.RolRepositoryPort;
import com.posfarmacia.usecases.port.out.identidad.TotpPort;
import com.posfarmacia.usecases.port.out.identidad.UsuarioRepositoryPort;
import com.posfarmacia.domain.exception.CredencialesInvalidasException;
import com.posfarmacia.domain.exception.MfaRequeridaException;
import com.posfarmacia.domain.model.identidad.Usuario;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

/** RF01: valida el codigo TOTP y devuelve la identidad completa para emitir el JWT de sesion. */
public class VerificarSegundoFactorUseCaseImpl implements VerificarSegundoFactorUseCase {

    private final UsuarioRepositoryPort usuarios;
    private final RolRepositoryPort roles;
    private final TotpPort totp;

    public VerificarSegundoFactorUseCaseImpl(UsuarioRepositoryPort usuarios, RolRepositoryPort roles, TotpPort totp) {
        this.usuarios = usuarios;
        this.roles = roles;
        this.totp = totp;
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioAutenticado verificar(UUID usuarioId, String codigo) {
        Usuario usuario = usuarios.buscarPorId(usuarioId)
                .orElseThrow(CredencialesInvalidasException::new);

        if (!usuario.estaActivo()) {
            throw new CredencialesInvalidasException();
        }
        if (!usuario.tieneMfaHabilitado() || !totp.validar(usuario.getMfaSecret(), codigo)) {
            throw new MfaRequeridaException();
        }

        return UsuarioAutenticadoFactory.de(usuario, roles);
    }
}
