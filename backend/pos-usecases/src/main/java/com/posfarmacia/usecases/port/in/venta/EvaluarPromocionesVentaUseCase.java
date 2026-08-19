package com.posfarmacia.usecases.port.in.venta;

import com.posfarmacia.usecases.dto.venta.PromocionDisponibleResult;
import java.util.List;
import java.util.UUID;

/** Puerto de entrada RF06: promociones vigentes aplicables a una linea de una venta puntual. */
public interface EvaluarPromocionesVentaUseCase {

    List<PromocionDisponibleResult> evaluar(UUID ventaId, UUID detalleVentaId);
}
