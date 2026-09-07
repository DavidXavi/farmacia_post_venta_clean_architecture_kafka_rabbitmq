package com.posfarmacia.adapters.web.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Valida el JWT entrante y puebla el SecurityContext (RF01). Los roles se exponen como
 * autoridades "ROLE_x" (para hasRole/@PreAuthorize) y los permisos especiales como
 * autoridades planas (para hasAuthority, p. ej. RN41 exige el permiso ANULAR_VENTAS).
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final Key clave;

    public JwtAuthenticationFilter(@Value("${pos-farmacia.jwt.secret}") String secreto) {
        this.clave = Keys.hmacShaKeyFor(secreto.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            try {
                Claims claims = Jwts.parser().verifyWith((javax.crypto.SecretKey) clave).build()
                        .parseSignedClaims(header.substring(7))
                        .getPayload();

                // Un token intermedio de MFA no autentica nada: solo sirve en /api/auth/mfa/verificar.
                if (JwtTokenIssuer.SCOPE_MFA_PENDIENTE.equals(claims.get("scope", String.class))) {
                    chain.doFilter(request, response);
                    return;
                }

                List<?> roles = claims.get("roles", List.class);
                List<?> permisos = claims.get("permisos", List.class);

                List<GrantedAuthority> authoridades = Stream.concat(
                        roles.stream().map(rol -> "ROLE_" + rol),
                        permisos.stream().map(Object::toString))
                        .map(SimpleGrantedAuthority::new)
                        .map(GrantedAuthority.class::cast)
                        .toList();

                var authentication = new UsernamePasswordAuthenticationToken(
                        claims.getSubject(), null, authoridades);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (JwtException | IllegalArgumentException ex) {
                SecurityContextHolder.clearContext();
            }
        }

        chain.doFilter(request, response);
    }
}
