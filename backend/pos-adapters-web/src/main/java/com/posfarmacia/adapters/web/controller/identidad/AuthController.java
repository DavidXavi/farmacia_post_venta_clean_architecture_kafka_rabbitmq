package com.posfarmacia.adapters.web.controller.identidad;

import com.posfarmacia.adapters.web.request.identidad.LoginRequest;
import com.posfarmacia.adapters.web.request.identidad.VerificarMfaRequest;
import com.posfarmacia.adapters.web.response.identidad.LoginResponse;
import com.posfarmacia.adapters.web.security.JwtTokenIssuer;
import com.posfarmacia.usecases.dto.identidad.UsuarioAutenticado;
import com.posfarmacia.usecases.port.in.identidad.AutenticarUsuarioUseCase;
import com.posfarmacia.usecases.port.in.identidad.VerificarSegundoFactorUseCase;
import com.posfarmacia.domain.enums.PermisoEspecial;
import com.posfarmacia.domain.enums.RolNombre;
import com.posfarmacia.domain.exception.CredencialesInvalidasException;
import io.jsonwebtoken.JwtException;
import jakarta.validation.Valid;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** RF01: autenticacion y control de acceso (contrasena, proveedores sociales y segundo factor). */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AutenticarUsuarioUseCase autenticarUsuario;
    private final VerificarSegundoFactorUseCase verificarSegundoFactor;
    private final JwtTokenIssuer jwtTokenIssuer;

    public AuthController(AutenticarUsuarioUseCase autenticarUsuario,
                           VerificarSegundoFactorUseCase verificarSegundoFactor,
                           JwtTokenIssuer jwtTokenIssuer) {
        this.autenticarUsuario = autenticarUsuario;
        this.verificarSegundoFactor = verificarSegundoFactor;
        this.jwtTokenIssuer = jwtTokenIssuer;
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        UsuarioAutenticado usuario = autenticarUsuario.autenticar(request.nombreUsuario(), request.password());

        // Con MFA activo el primer factor no abre sesion: solo habilita el paso de verificacion.
        return usuario.mfaHabilitado()
                ? LoginResponse.segundoFactorPendiente(jwtTokenIssuer.emitirPendienteMfa(usuario.usuarioId()))
                : sesion(usuario);
    }

    /** Segundo paso del login: canjea el token intermedio + el codigo TOTP por una sesion real. */
    @PostMapping("/mfa/verificar")
    public LoginResponse verificarMfa(@Valid @RequestBody VerificarMfaRequest request) {
        UUID usuarioId;
        try {
            usuarioId = jwtTokenIssuer.usuarioDePendienteMfa(request.mfaToken());
        } catch (JwtException | IllegalArgumentException ex) {
            throw new CredencialesInvalidasException("La verificacion expiro. Vuelve a iniciar sesion.");
        }

        return sesion(verificarSegundoFactor.verificar(usuarioId, request.codigo()));
    }

    private LoginResponse sesion(UsuarioAutenticado usuario) {
        Set<String> roles = usuario.roles().stream().map(RolNombre::name).collect(Collectors.toSet());
        Set<String> permisos = usuario.permisos().stream().map(PermisoEspecial::name).collect(Collectors.toSet());

        return LoginResponse.sesion(jwtTokenIssuer.emitir(usuario), usuario.usuarioId(), usuario.nombreUsuario(),
                roles, permisos, usuario.localId());
    }
}
