package com.posfarmacia.adapters.web.response.identidad;

import com.posfarmacia.domain.model.identidad.SesionCaja;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record SesionCajaResponse(
        UUID id,
        UUID cajaId,
        UUID usuarioId,
        Instant fechaApertura,
        BigDecimal montoInicial,
        Instant fechaCierre,
        BigDecimal montoEsperado,
        BigDecimal montoDeclarado,
        BigDecimal diferencia,
        String observacionCierre,
        String estado) {

    public static SesionCajaResponse desde(SesionCaja sesion) {
        return new SesionCajaResponse(
                sesion.getId(),
                sesion.getCajaId(),
                sesion.getUsuarioId(),
                sesion.getFechaApertura(),
                sesion.getMontoInicial().monto(),
                sesion.getFechaCierre(),
                sesion.getMontoEsperado() == null ? null : sesion.getMontoEsperado().monto(),
                sesion.getMontoDeclarado() == null ? null : sesion.getMontoDeclarado().monto(),
                sesion.getDiferencia() == null ? null : sesion.getDiferencia().monto(),
                sesion.getObservacionCierre(),
                sesion.getEstado().name());
    }
}
