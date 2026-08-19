package com.posfarmacia.usecases.port.in.inventario;

import com.posfarmacia.usecases.dto.inventario.LoteResult;
import java.util.List;
import java.util.UUID;

/** Puerto de entrada: lista lotes, opcionalmente filtrados por producto (RF04). */
public interface ConsultarLotesUseCase {

    List<LoteResult> consultar(UUID productoId);
}
