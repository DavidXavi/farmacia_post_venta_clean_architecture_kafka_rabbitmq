package com.posfarmacia.usecases.port.in.identidad;

import com.posfarmacia.domain.model.identidad.RegistroAuditoria;
import java.util.UUID;

/**
 * RF19: deja evidencia de una operacion sensible (anulaciones, notas de credito, cambios
 * de precio, ajustes de stock, validacion de recetas, cambios de promocion). Otros
 * contextos invocan este puerto de entrada para auditar sus propias operaciones.
 */
public interface RegistrarAuditoriaUseCase {

    RegistroAuditoria registrar(UUID usuarioId, String accion, String entidad, String entidadId, String detalle,
                                 String datosAnteriores, String datosNuevos);
}
