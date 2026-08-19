package com.posfarmacia.usecases.usecase.inventario;

import com.posfarmacia.usecases.dto.inventario.InventarioResult;
import com.posfarmacia.usecases.port.in.inventario.ConsultarInventarioUseCase;
import com.posfarmacia.usecases.port.out.inventario.ExistenciaLoteRepositoryPort;
import com.posfarmacia.domain.model.inventario.ExistenciaLote;
import java.util.List;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

/**
 * Caso de uso RF15: consulta el rollup de existencias consolidado por local. Es solo lectura: el stock
 * se recalcula siempre desde los lotes (ver {@link ExistenciaLote}), nunca se edita en esta consulta.
 */
public class ConsultarInventarioUseCaseImpl implements ConsultarInventarioUseCase {

    private final ExistenciaLoteRepositoryPort existencias;

    public ConsultarInventarioUseCaseImpl(ExistenciaLoteRepositoryPort existencias) {
        this.existencias = existencias;
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventarioResult> consultarPorLocal(UUID localId) {
        return existencias.listarPorLocal(localId).stream().map(ConsultarInventarioUseCaseImpl::aResult).toList();
    }

    private static InventarioResult aResult(ExistenciaLote existencia) {
        return new InventarioResult(existencia.getProductoId(), existencia.getLocalId(),
                existencia.getCantidadActual().valor(), existencia.getActualizadoEn());
    }
}
