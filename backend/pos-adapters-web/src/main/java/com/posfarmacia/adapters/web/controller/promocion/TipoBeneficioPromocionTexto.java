package com.posfarmacia.adapters.web.controller.promocion;

import com.posfarmacia.domain.enums.TipoBeneficioPromocion;
import com.posfarmacia.domain.exception.ValorInvalidoException;

/**
 * Traduce entre el texto que usa el CRUD de promociones del frontend (PascalCase, igual que el
 * enum {@code TipoBeneficioPromocion} de PosFarmacia.Domain.Enums en el .NET de referencia:
 * "DescuentoPorcentaje"/"DescuentoMonto"/"LlevaNPagaM") y el enum de dominio Java (kernel
 * compartido, {@code SCREAMING_SNAKE_CASE}). Puramente de formato HTTP, no calcula ninguna
 * regla de negocio.
 */
public final class TipoBeneficioPromocionTexto {

    private TipoBeneficioPromocionTexto() {
    }

    public static TipoBeneficioPromocion aEnum(String texto) {
        if (texto == null) {
            throw new ValorInvalidoException("El tipo de beneficio de la promocion es obligatorio.");
        }
        return switch (texto) {
            case "DescuentoPorcentaje" -> TipoBeneficioPromocion.DESCUENTO_PORCENTAJE;
            case "DescuentoMonto" -> TipoBeneficioPromocion.DESCUENTO_MONTO;
            case "LlevaNPagaM" -> TipoBeneficioPromocion.LLEVA_N_PAGA_M;
            default -> throw new ValorInvalidoException("El tipo de beneficio de promocion '" + texto + "' no es valido.");
        };
    }

    public static String desdeEnum(TipoBeneficioPromocion tipo) {
        return switch (tipo) {
            case DESCUENTO_PORCENTAJE -> "DescuentoPorcentaje";
            case DESCUENTO_MONTO -> "DescuentoMonto";
            case LLEVA_N_PAGA_M -> "LlevaNPagaM";
        };
    }
}
