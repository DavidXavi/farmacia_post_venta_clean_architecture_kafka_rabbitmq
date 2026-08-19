package com.posfarmacia.usecases.port.in.receta;

import java.util.UUID;

/**
 * Entrada de {@link RevisarRecetaUseCase}: decision del quimico farmaceutico sobre una
 * receta ya registrada (aprobarla o rechazarla).
 *
 * <p>{@code usuarioValidadorId} y {@code observaciones} se preservan del contrato de
 * {@code arquitectura_2_t2} (auditoria); este contexto aun no persiste un registro de
 * auditoria propio de la revision (no hay tabla `usos_receta`-equivalente para esto en
 * la migracion V4 de este contexto), asi que por ahora solo se usan para la traza de
 * auditoria general si el llamador la registra.
 */
public record RevisarRecetaCommand(UUID recetaId, UUID usuarioValidadorId, boolean aprobar, String observaciones) {
}
