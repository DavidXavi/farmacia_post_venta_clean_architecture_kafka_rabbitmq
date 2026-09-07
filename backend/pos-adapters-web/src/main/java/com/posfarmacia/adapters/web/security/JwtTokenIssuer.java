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
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Firma el JWT tras una autenticacion exitosa. La aplicacion (AutenticarUsuarioUseCase) nunca
 * genera tokens; esa es responsabilidad exclusiva de este adaptador de entrada REST.
 */
@Component
public class JwtTokenIssuer {

    /** Marca el token intermedio que solo sirve para presentar el segundo factor. */
    public static final String SCOPE_MFA_PENDIENTE = "MFA_PENDIENTE";

    private final Key clave;
    private final long expiracionMinutos;
    private final long expiracionMfaMinutos;

    public JwtTokenIssuer(
            @Value("${pos-farmacia.jwt.secret}") String secreto,
            @Value("${pos-farmacia.jwt.expiration-minutes:480}") long expiracionMinutos,
            @Value("${pos-farmacia.jwt.mfa-expiration-minutes:5}") long expiracionMfaMinutos) {
        this.clave = Keys.hmacShaKeyFor(secreto.getBytes(StandardCharsets.UTF_8));
        this.expiracionMinutos = expiracionMinutos;
        this.expiracionMfaMinutos = expiracionMfaMinutos;
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

    /**
     * Token de corta vida emitido cuando el primer factor fue correcto pero la cuenta exige MFA.
     * No lleva roles ni permisos, asi que no abre ningun endpoint: solo identifica al usuario
     * ante /api/auth/mfa/verificar.
     */
    public String emitirPendienteMfa(UUID usuarioId) {
        Instant ahora = Instant.now();
        return Jwts.builder()
                .subject(usuarioId.toString())
                .claim("scope", SCOPE_MFA_PENDIENTE)
                .issuedAt(Date.from(ahora))
                .expiration(Date.from(ahora.plus(Duration.ofMinutes(expiracionMfaMinutos))))
                .signWith(clave)
                .compact();
    }

    /** Devuelve el id del usuario que ese token intermedio identifica, o falla si no lo es. */
    public UUID usuarioDePendienteMfa(String token) {
        var claims = Jwts.parser().verifyWith((javax.crypto.SecretKey) clave).build()
                .parseSignedClaims(token)
                .getPayload();

        if (!SCOPE_MFA_PENDIENTE.equals(claims.get("scope", String.class))) {
            throw new io.jsonwebtoken.JwtException("El token no corresponde a una verificacion de MFA.");
        }
        return UUID.fromString(claims.getSubject());
    }
}
