package com.posfarmacia.usecases.dto.venta;

import com.posfarmacia.domain.enums.TipoBeneficioPromocion;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * RF06: promocion vigente y aplicable a una linea de venta, para que el cajero se la muestre al
 * cliente. {@code tipoBeneficio} viaja como el enum de dominio (no como texto ya formateado): el
 * formato de salida HTTP (PascalCase, para calzar con el frontend) es una decision del adaptador
 * de entrada, no de este DTO de aplicacion.
 */
public record PromocionDisponibleResult(UUID id, String nombre, TipoBeneficioPromocion tipoBeneficio,
        BigDecimal valorBeneficio) {
}
