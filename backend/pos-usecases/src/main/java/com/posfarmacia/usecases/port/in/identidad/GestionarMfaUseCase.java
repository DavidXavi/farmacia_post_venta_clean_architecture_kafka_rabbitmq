package com.posfarmacia.usecases.port.in.identidad;

import com.posfarmacia.usecases.dto.identidad.RegistroMfa;
import java.util.UUID;

/** RF01: alta y baja del segundo factor (Google Authenticator) de la cuenta propia. */
public interface GestionarMfaUseCase {

    /** Genera un secreto nuevo y lo deja pendiente de confirmacion. */
    RegistroMfa iniciarRegistro(UUID usuarioId);

    /** Activa el MFA solo si el codigo prueba que la app quedo bien configurada. */
    void confirmarRegistro(UUID usuarioId, String codigo);

    void deshabilitar(UUID usuarioId);

    boolean estaHabilitado(UUID usuarioId);
}
