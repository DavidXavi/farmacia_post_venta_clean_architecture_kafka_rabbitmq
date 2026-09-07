package com.posfarmacia.usecases.usecase.identidad;

import com.posfarmacia.usecases.dto.identidad.RegistroMfa;
import com.posfarmacia.usecases.port.in.identidad.GestionarMfaUseCase;
import com.posfarmacia.usecases.port.out.identidad.TotpPort;
import com.posfarmacia.usecases.port.out.identidad.UsuarioRepositoryPort;
import com.posfarmacia.domain.exception.EntidadNoEncontradaException;
import com.posfarmacia.domain.exception.MfaRequeridaException;
import com.posfarmacia.domain.model.identidad.Usuario;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

/** RF01: alta/baja del segundo factor TOTP de la cuenta propia. */
public class GestionarMfaUseCaseImpl implements GestionarMfaUseCase {

    private final UsuarioRepositoryPort usuarios;
    private final TotpPort totp;

    public GestionarMfaUseCaseImpl(UsuarioRepositoryPort usuarios, TotpPort totp) {
        this.usuarios = usuarios;
        this.totp = totp;
    }

    @Override
    @Transactional
    public RegistroMfa iniciarRegistro(UUID usuarioId) {
        Usuario usuario = buscar(usuarioId);
        String secreto = totp.generarSecreto();
        usuario.prepararMfa(secreto);
        usuarios.guardar(usuario);

        return new RegistroMfa(secreto, totp.uriOtpauth(secreto, usuario.getNombreUsuario()));
    }

    @Override
    @Transactional
    public void confirmarRegistro(UUID usuarioId, String codigo) {
        Usuario usuario = buscar(usuarioId);
        if (usuario.getMfaSecret() == null || !totp.validar(usuario.getMfaSecret(), codigo)) {
            throw new MfaRequeridaException();
        }
        usuario.confirmarMfa();
        usuarios.guardar(usuario);
    }

    @Override
    @Transactional
    public void deshabilitar(UUID usuarioId) {
        Usuario usuario = buscar(usuarioId);
        usuario.deshabilitarMfa();
        usuarios.guardar(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean estaHabilitado(UUID usuarioId) {
        return buscar(usuarioId).tieneMfaHabilitado();
    }

    private Usuario buscar(UUID usuarioId) {
        return usuarios.buscarPorId(usuarioId)
                .orElseThrow(() -> new EntidadNoEncontradaException("Usuario no encontrado: " + usuarioId));
    }
}
