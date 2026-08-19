package com.posfarmacia.adapters.web.security;

import com.posfarmacia.usecases.dto.identidad.UsuarioAutenticado;
import com.posfarmacia.domain.enums.PermisoEspecial;
import com.posfarmacia.domain.enums.RolNombre;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Firma el JWT tras una autenticacion exitosa. La aplicacion (AutenticarUsuarioUseCase) nunca
 * genera tokens; esa es responsabilidad exclusiva de este adaptador de entrada REST.
 */
@Component
public class JwtTokenIssuer {

    private final Key clave;
    private final long expiracionMinutos;

    public JwtTokenIssuer(
            @Value("${pos-farmacia.jwt.secret}") String secreto,
            @Value("${pos-farmacia.jwt.expiration-minutes:480}") long expiracionMinutos) {
        this.clave = Keys.hmacShaKeyFor(secreto.getBytes(StandardCharsets.UTF_8));
        this.expiracionMinutos = expiracionMinutos;
    }

    public String emitir(UsuarioAutenticado usuario) {
        Instant ahora = Instant.now();
        List<String> roles = usuario.roles().stream().map(RolNombre::name).collect(Collectors.toList());
        List<String> permisos = usuario.permisos().stream().map(PermisoEspecial::name).collect(Collectors.toList());

        return Jwts.builder()
                .subject(usuario.usuarioId().toString())
                .claim("nombreUsuario", usuario.nombreUsuario())
                .claim("localId", usuario.localId().toString())
                .claim("roles", roles)
                .claim("permisos", permisos)
                .issuedAt(Date.from(ahora))
                .expiration(Date.from(ahora.plus(Duration.ofMinutes(expiracionMinutos))))
                .signWith(clave)
                .compact();
    }
}
