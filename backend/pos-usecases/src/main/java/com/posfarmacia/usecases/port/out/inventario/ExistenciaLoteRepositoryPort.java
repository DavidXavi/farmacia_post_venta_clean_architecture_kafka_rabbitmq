package com.posfarmacia.usecases.port.out.inventario;

import com.posfarmacia.domain.model.inventario.ExistenciaLote;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/** Puerto de salida: persistencia del rollup de existencias por producto+local (RF04/RF15). */
public interface ExistenciaLoteRepositoryPort {

    Optional<ExistenciaLote> buscarPorProductoYLocal(UUID productoId, UUID localId);

    List<ExistenciaLote> listarPorLocal(UUID localId);

    ExistenciaLote guardar(ExistenciaLote existencia);
}
