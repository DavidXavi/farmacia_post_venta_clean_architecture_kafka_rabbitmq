package com.posfarmacia.adapters.web.response.venta;

import com.posfarmacia.adapters.web.controller.promocion.TipoBeneficioPromocionTexto;
import com.posfarmacia.usecases.dto.venta.PromocionDisponibleResult;
import java.math.BigDecimal;
import java.util.UUID;

public record PromocionAplicableResponse(UUID id, String nombre, String tipoBeneficio, BigDecimal valorBeneficio) {

    public static PromocionAplicableResponse desde(PromocionDisponibleResult result) {
        return new PromocionAplicableResponse(result.id(), result.nombre(),
                TipoBeneficioPromocionTexto.desdeEnum(result.tipoBeneficio()), result.valorBeneficio());
    }
}
