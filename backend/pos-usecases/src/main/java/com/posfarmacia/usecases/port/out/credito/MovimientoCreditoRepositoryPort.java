package com.posfarmacia.usecases.port.out.credito;

import com.posfarmacia.domain.model.credito.MovimientoCredito;
import java.util.List;
import java.util.UUID;

/** Puerto de salida hacia la persistencia de {@link MovimientoCredito} (consumo/reversion, RN31-RN32). */
public interface MovimientoCreditoRepositoryPort {

    MovimientoCredito guardar(MovimientoCredito movimiento);

    List<MovimientoCredito> buscarPorLineaCredito(UUID lineaCreditoId);
}
