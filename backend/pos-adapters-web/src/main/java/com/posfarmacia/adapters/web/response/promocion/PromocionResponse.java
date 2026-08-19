package com.posfarmacia.adapters.web.response.promocion;

import com.posfarmacia.adapters.web.controller.promocion.TipoBeneficioPromocionTexto;
import com.posfarmacia.domain.model.promocion.CondicionPromocion;
import com.posfarmacia.domain.model.promocion.Promocion;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Respuesta del CRUD de administracion de promociones. Ruta EXACTA (campos) que consume
 * {@code frontend/src/pages/PromocionesPage.jsx}.
 */
public record PromocionResponse(
        UUID id,
        String nombre,
        String descripcion,
        String tipoBeneficio,
        BigDecimal valorBeneficio,
        boolean requiereCliente,
        int cantidadMinima,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        boolean activa,
        List<UUID> productosParticipantes) {

    public static PromocionResponse desde(Promocion promocion) {
        return new PromocionResponse(
                promocion.getId(),
                promocion.getNombre(),
                promocion.getDescripcion(),
                TipoBeneficioPromocionTexto.desdeEnum(promocion.getTipoBeneficio()),
                promocion.getValorBeneficio(),
                promocion.isRequiereCliente(),
                promocion.getCantidadMinima().valor(),
                promocion.getVigencia().inicio(),
                promocion.getVigencia().fin(),
                promocion.isActiva(),
                promocion.getCondiciones().stream().map(CondicionPromocion::productoId).toList());
    }
}
