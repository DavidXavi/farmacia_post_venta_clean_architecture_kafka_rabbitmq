package com.posfarmacia.usecases.usecase.inventario;

import com.posfarmacia.usecases.dto.inventario.LoteResult;
import com.posfarmacia.domain.model.inventario.Lote;

/** Mapeo de Lote (dominio) a su proyeccion de lectura. Sin estado, compartido entre los casos de uso del contexto. */
final class LoteResultMapper {

    private LoteResultMapper() {
    }

    static LoteResult aResult(Lote lote) {
        return new LoteResult(
                lote.getId(),
                lote.getCodigo().valor(),
                lote.getProductoId(),
                lote.getFechaVencimiento().valor(),
                lote.getCantidadRecibida().valor(),
                lote.getCantidadDisponible().valor(),
                lote.getCosto() == null ? null : lote.getCosto().monto(),
                lote.getLocalId(),
                lote.getEstado().name());
    }
}
