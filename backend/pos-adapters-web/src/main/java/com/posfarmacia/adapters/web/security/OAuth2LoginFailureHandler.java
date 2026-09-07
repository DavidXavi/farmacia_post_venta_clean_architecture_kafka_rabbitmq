package com.posfarmacia.adapters.web.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

/** Devuelve al frontend con el motivo del fallo en vez de mostrar la pagina de error de Spring. */
@Component
public class OAuth2LoginFailureHandler implements AuthenticationFailureHandler {

    private final String urlFrontend;

    public OAuth2LoginFailureHandler(
            @Value("${pos-farmacia.oauth2.url-frontend:http://localhost:5173}") String urlFrontend) {
        this.urlFrontend = urlFrontend;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                         AuthenticationException exception) throws IOException {
        String mensaje = exception.getMessage() == null
                ? "No se pudo completar el login social."
                : exception.getMessage();
        response.sendRedirect(urlFrontend + "/login?error=" + URLEncoder.encode(mensaje, StandardCharsets.UTF_8));
    }
}
