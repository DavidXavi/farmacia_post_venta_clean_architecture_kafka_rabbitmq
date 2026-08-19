package com.posfarmacia.adapters.web.response.venta;

import com.posfarmacia.usecases.dto.venta.CopagoResult;
import java.math.BigDecimal;

public record CopagoResponse(BigDecimal montoCubierto, BigDecimal copago) {

    public static CopagoResponse desde(CopagoResult result) {
        return new CopagoResponse(result.montoCubierto(), result.copago());
    }
}
