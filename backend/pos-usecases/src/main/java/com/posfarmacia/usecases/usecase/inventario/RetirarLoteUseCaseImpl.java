package com.posfarmacia.usecases.usecase.inventario;

import com.posfarmacia.usecases.port.in.inventario.RetirarLoteUseCase;
import com.posfarmacia.usecases.port.out.ClockPort;
import com.posfarmacia.usecases.port.out.inventario.ExistenciaLoteRepositoryPort;
import com.posfarmacia.usecases.port.out.inventario.LoteRepositoryPort;
import com.posfarmacia.domain.exception.EntidadNoEncontradaException;
import com.posfarmacia.domain.model.inventario.Lote;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

/**
 * Caso de uso RN37: retira un lote del stock vendible en todas las sedes (orden de la central).
 * Tambien resincroniza el rollup de existencias (ver {@link SincronizadorExistencias}): un lote
 * retirado deja de contar como stock vendible, y el inventario consolidado no puede seguir
 * mostrandolo como disponible.
 */
public class RetirarLoteUseCaseImpl implements RetirarLoteUseCase {

    private final LoteRepositoryPort lotes;
    private final ExistenciaLoteRepositoryPort existencias;
    private final ClockPort clock;

    public RetirarLoteUseCaseImpl(LoteRepositoryPort lotes, ExistenciaLoteRepositoryPort existencias,
            ClockPort clock) {
        this.lotes = lotes;
        this.existencias = existencias;
        this.clock = clock;
    }

    @Override
    @Transactional
    public void retirar(UUID loteId) {
        Lote lote = lotes.buscarPorId(loteId)
                .orElseThrow(() -> new EntidadNoEncontradaException("El lote indicado no existe."));
        lote.retirar();
        lotes.guardar(lote);
        SincronizadorExistencias.sincronizar(lote.getProductoId(), lote.getLocalId(), lotes, existencias, clock);
    }
}
