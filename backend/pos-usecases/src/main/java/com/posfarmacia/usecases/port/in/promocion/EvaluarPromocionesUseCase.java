package com.posfarmacia.usecases.port.in.promocion;

import com.posfarmacia.usecases.dto.promocion.EvaluarPromocionesQuery;
import com.posfarmacia.domain.model.promocion.Promocion;
import java.util.List;

/** Puerto de entrada RF06: evaluar que promociones vigentes aplican a una linea de venta. */
public interface EvaluarPromocionesUseCase {

    List<Promocion> evaluar(EvaluarPromocionesQuery query);
}
