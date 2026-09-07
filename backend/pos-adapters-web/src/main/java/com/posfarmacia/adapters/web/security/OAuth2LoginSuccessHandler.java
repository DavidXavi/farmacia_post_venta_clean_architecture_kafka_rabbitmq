package com.posfarmacia.adapters.web.security;

import com.posfarmacia.usecases.dto.identidad.UsuarioAutenticado;
import com.posfarmacia.usecases.port.in.identidad.AutenticarConProveedorUseCase;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

/**
 * Cierra el login social (Google/Facebook): traduce la identidad que devolvio el proveedor a la
 * cuenta del POS y devuelve al frontend con el JWT en la URL. Si la cuenta tiene MFA, lo que
 * viaja es el token intermedio y el frontend pedira el codigo de Google Authenticator.
 */
@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final AutenticarConProveedorUseCase autenticarConProveedor;
    private final JwtTokenIssuer jwtTokenIssuer;
    private final String urlFrontend;

    public OAuth2LoginSuccessHandler(AutenticarConProveedorUseCase autenticarConProveedor,
                                      JwtTokenIssuer jwtTokenIssuer,
                                      @Value("${pos-farmacia.oauth2.url-frontend:http://localhost:5173}") String urlFrontend) {
        this.autenticarConProveedor = autenticarConProveedor;
        this.jwtTokenIssuer = jwtTokenIssuer;
        this.urlFrontend = urlFrontend;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                         Authentication authentication) throws IOException, ServletException {
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        String proveedor = token.getAuthorizedClientRegistrationId();
        OAuth2User usuarioProveedor = token.getPrincipal();

        UsuarioAutenticado usuario = autenticarConProveedor.autenticar(
                proveedor,
                usuarioProveedor.getAttribute("email"),
                usuarioProveedor.getAttribute("name"));

        String parametro = usuario.mfaHabilitado()
                ? "mfaToken=" + codificar(jwtTokenIssuer.emitirPendienteMfa(usuario.usuarioId()))
                : "token=" + codificar(jwtTokenIssuer.emitir(usuario));

        response.sendRedirect(urlFrontend + "/login?" + parametro + "&proveedor=" + codificar(proveedor));
    }

    private static String codificar(String valor) {
        return URLEncoder.encode(valor, StandardCharsets.UTF_8);
    }
}
