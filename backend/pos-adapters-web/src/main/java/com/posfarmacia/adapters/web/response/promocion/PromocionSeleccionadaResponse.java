package com.posfarmacia.adapters.web.response.promocion;

import com.posfarmacia.domain.model.promocion.AplicacionPromocion;
import java.math.BigDecimal;
import java.util.UUID;

public record PromocionSeleccionadaResponse(
        UUID promocionId,
        UUID ventaId,
        UUID detalleVentaId,
        BigDecimal montoDescuento) {

    public static PromocionSeleccionadaResponse desde(AplicacionPromocion aplicacion) {
        return new PromocionSeleccionadaResponse(
                aplicacion.getPromocionId(),
                aplicacion.getVentaId(),
                aplicacion.getDetalleVentaId(),
                aplicacion.getMontoDescuento().monto());
    }
}
