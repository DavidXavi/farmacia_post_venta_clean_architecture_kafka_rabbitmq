package com.posfarmacia.adapters.web.response.promocion;

import com.posfarmacia.adapters.web.controller.promocion.TipoBeneficioPromocionTexto;
import com.posfarmacia.domain.model.promocion.Promocion;
import java.math.BigDecimal;
import java.util.UUID;

public record PromocionAplicableResponse(
        UUID id,
        String nombre,
        String tipoBeneficio,
        BigDecimal valorBeneficio,
        boolean requiereCliente) {

    public static PromocionAplicableResponse desde(Promocion promocion) {
        return new PromocionAplicableResponse(
                promocion.getId(),
                promocion.getNombre(),
                TipoBeneficioPromocionTexto.desdeEnum(promocion.getTipoBeneficio()),
                promocion.getValorBeneficio(),
                promocion.isRequiereCliente());
    }
}
