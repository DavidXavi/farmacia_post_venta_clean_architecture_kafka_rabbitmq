package com.posfarmacia.adapters.web.controller.identidad;

import com.posfarmacia.adapters.web.request.identidad.CodigoMfaRequest;
import com.posfarmacia.adapters.web.response.identidad.EstadoMfaResponse;
import com.posfarmacia.adapters.web.response.identidad.RegistroMfaResponse;
import com.posfarmacia.usecases.dto.identidad.RegistroMfa;
import com.posfarmacia.usecases.port.in.identidad.GestionarMfaUseCase;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.UUID;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * RF01: el usuario administra el segundo factor de su propia cuenta. Siempre opera sobre el
 * usuario del JWT, nunca sobre un id recibido del cliente.
 */
@RestController
@RequestMapping("/api/auth/mfa")
public class MfaController {

    private final GestionarMfaUseCase gestionarMfa;

    public MfaController(GestionarMfaUseCase gestionarMfa) {
        this.gestionarMfa = gestionarMfa;
    }

    @GetMapping("/estado")
    public EstadoMfaResponse estado(Principal principal) {
        return new EstadoMfaResponse(gestionarMfa.estaHabilitado(usuarioId(principal)));
    }

    @PostMapping("/registro")
    public RegistroMfaResponse iniciarRegistro(Principal principal) {
        RegistroMfa registro = gestionarMfa.iniciarRegistro(usuarioId(principal));
        return new RegistroMfaResponse(registro.secreto(), registro.uriOtpauth());
    }

    @PostMapping("/registro/confirmar")
    public EstadoMfaResponse confirmarRegistro(Principal principal, @Valid @RequestBody CodigoMfaRequest request) {
        gestionarMfa.confirmarRegistro(usuarioId(principal), request.codigo());
        return new EstadoMfaResponse(true);
    }

    @DeleteMapping
    public EstadoMfaResponse deshabilitar(Principal principal) {
        gestionarMfa.deshabilitar(usuarioId(principal));
        return new EstadoMfaResponse(false);
    }

    private static UUID usuarioId(Principal principal) {
        return UUID.fromString(principal.getName());
    }
}
