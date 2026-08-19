package com.posfarmacia.usecases.dto.promocion;

import com.posfarmacia.domain.enums.TipoBeneficioPromocion;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/** Entrada de {@code GestionarPromocionUseCase#actualizar} (CRUD de administracion, RF06). */
public record ActualizarPromocionCommand(
        String nombre,
        String descripcion,
        TipoBeneficioPromocion tipoBeneficio,
        BigDecimal valorBeneficio,
        boolean requiereCliente,
        int cantidadMinima,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        List<UUID> productosParticipantes) {
}
