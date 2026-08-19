package com.posfarmacia.usecases.port.out.inventario;

import com.posfarmacia.domain.model.inventario.Lote;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/** Puerto de salida: persistencia del agregado Lote (RF04). */
public interface LoteRepositoryPort {

    Lote guardar(Lote lote);

    Optional<Lote> buscarPorId(UUID id);

    /** Todos los lotes, opcionalmente filtrados por producto (productoId nulo = sin filtro). */
    List<Lote> listar(UUID productoId);

    /** Todos los lotes de un producto en un local, en cualquier estado (para calcular stock vendible o recalcular el rollup). */
    List<Lote> listarPorProductoYLocal(UUID productoId, UUID localId);
}
