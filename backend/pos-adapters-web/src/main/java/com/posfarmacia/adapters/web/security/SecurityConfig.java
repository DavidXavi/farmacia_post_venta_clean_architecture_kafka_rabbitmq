package com.posfarmacia.adapters.web.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Seguridad HTTP transversal (RF01): JWT sin estado, control de acceso por rol/permiso.
 * Cada contexto que agregue controllers puede sumar sus propias reglas de acceso aqui.
 */
@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final OAuth2LoginSuccessHandler oauth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler oauth2LoginFailureHandler;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
                           OAuth2LoginSuccessHandler oauth2LoginSuccessHandler,
                           OAuth2LoginFailureHandler oauth2LoginFailureHandler) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.oauth2LoginSuccessHandler = oauth2LoginSuccessHandler;
        this.oauth2LoginFailureHandler = oauth2LoginFailureHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Login con contrasena y segundo paso del MFA: aun no hay sesion que exigir.
                        .requestMatchers("/api/auth/login", "/api/auth/mfa/verificar").permitAll()
                        // Login social: el handshake con Google/Facebook lo maneja Spring Security.
                        .requestMatchers("/oauth2/**", "/login/oauth2/**").permitAll()
                        .requestMatchers("/api/v1/docs/**", "/api/v1/swagger-ui/**", "/api/v1/swagger-ui.html").permitAll()
                        .requestMatchers("/api/auditoria/**")
                        .hasAnyAuthority("ROLE_ADMINISTRADOR", "VER_AUDITORIA")
                        // RF01: gestion de usuarios/roles del sistema, reservada al rol Administrador.
                        .requestMatchers("/api/usuarios/**", "/api/roles/**")
                        .hasAuthority("ROLE_ADMINISTRADOR")
                        // RN41 (contexto Anulaciones): anular una venta o emitir una nota de credito
                        // exige un usuario con el permiso especial correspondiente.
                        .requestMatchers("/api/ventas/*/anular")
                        .hasAnyAuthority("ROLE_ADMINISTRADOR", "ANULAR_VENTAS")
                        .requestMatchers("/api/notas-credito/**")
                        .hasAnyAuthority("ROLE_ADMINISTRADOR", "EMITIR_NOTA_CREDITO")
                        // RF17/RF18 (contexto Reportes e Incentivos): reservado al rol Administrador.
                        .requestMatchers("/api/reportes/**", "/api/reglas-incentivo/**")
                        .hasAuthority("ROLE_ADMINISTRADOR")
                        // Contexto Catalogo/Inventario (RF03/RF04): lectura amplia para cualquier usuario
                        // autenticado (regla por defecto mas abajo); la escritura del catalogo (altas,
                        // ediciones, bajas de productos/categorias/laboratorios/presentaciones) se
                        // restringe a Administrador, igual que en ProductosController.cs/CatalogosController.cs.
                        .requestMatchers(HttpMethod.POST, "/api/productos", "/api/categorias",
                                "/api/laboratorios", "/api/presentaciones")
                        .hasAuthority("ROLE_ADMINISTRADOR")
                        .requestMatchers(HttpMethod.PUT, "/api/productos/**")
                        .hasAuthority("ROLE_ADMINISTRADOR")
                        .requestMatchers(HttpMethod.PATCH, "/api/productos/*/dar-de-baja")
                        .hasAuthority("ROLE_ADMINISTRADOR")
                        // Lotes (RF04): alta y bloqueo/retiro de un lote es tarea operativa de inventario,
                        // igual que en LotesController.cs (Administrador, EncargadoInventario[, OperadorCentral]).
                        .requestMatchers(HttpMethod.POST, "/api/lotes")
                        .hasAnyAuthority("ROLE_ADMINISTRADOR", "ROLE_ENCARGADO_INVENTARIO")
                        .requestMatchers(HttpMethod.PATCH, "/api/lotes/*/bloquear", "/api/lotes/*/retirar")
                        .hasAnyAuthority("ROLE_ADMINISTRADOR", "ROLE_ENCARGADO_INVENTARIO", "ROLE_OPERADOR_CENTRAL")
                        // Contexto Promociones (RF06): consulta y evaluacion/seleccion durante la venta son
                        // de cualquier usuario autenticado (regla por defecto); el CRUD de administracion de
                        // promociones se restringe a Administrador, igual que en PromocionesController.cs.
                        .requestMatchers(HttpMethod.POST, "/api/promociones")
                        .hasAuthority("ROLE_ADMINISTRADOR")
                        .requestMatchers(HttpMethod.PUT, "/api/promociones/*")
                        .hasAuthority("ROLE_ADMINISTRADOR")
                        .requestMatchers(HttpMethod.PATCH, "/api/promociones/*/desactivar")
                        .hasAuthority("ROLE_ADMINISTRADOR")
                        // Contexto Convenios/Credito (RF08/RF09): mantenimiento de convenios de seguro y
                        // apertura de lineas de credito son tarea de Administrador u OperadorCentral, igual
                        // que en ConveniosController.cs/CreditosController.cs.
                        .requestMatchers("/api/convenios/**")
                        .hasAnyAuthority("ROLE_ADMINISTRADOR", "ROLE_OPERADOR_CENTRAL")
                        .requestMatchers("/api/lineas-credito/**")
                        .hasAnyAuthority("ROLE_ADMINISTRADOR", "ROLE_OPERADOR_CENTRAL")
                        .anyRequest().authenticated())
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(oauth2LoginSuccessHandler)
                        .failureHandler(oauth2LoginFailureHandler))
                // La API responde 401 en vez de redirigir a la pagina de login del navegador.
                .exceptionHandling(manejo -> manejo.defaultAuthenticationEntryPointFor(
                        new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
                        peticion -> peticion.getRequestURI().startsWith("/api/")))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
