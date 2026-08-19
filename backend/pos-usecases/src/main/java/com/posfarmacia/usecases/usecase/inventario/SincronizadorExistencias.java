package com.posfarmacia.usecases.usecase.inventario;

import com.posfarmacia.usecases.port.out.ClockPort;
import com.posfarmacia.usecases.port.out.inventario.ExistenciaLoteRepositoryPort;
import com.posfarmacia.usecases.port.out.inventario.LoteRepositoryPort;
import com.posfarmacia.domain.enums.EstadoLote;
import com.posfarmacia.domain.model.inventario.ExistenciaLote;
import com.posfarmacia.domain.model.inventario.Lote;
import com.posfarmacia.domain.valueobject.Cantidad;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Recalcula el rollup de existencias de un producto+local desde la suma de sus lotes en estado
 * DISPONIBLE (nunca es fuente de verdad, ver {@link ExistenciaLote}). Se llama despues de
 * cualquier operacion que cambie cuanto stock vendible tiene un producto en un local: alta de
 * lote, bloqueo, retiro, descuento de stock al confirmar una venta o devolucion de stock al
 * anular una venta (ver {@code usecase.venta.ConfirmarVentaUseCaseImpl}/{@code AnularVentaUseCaseImpl}).
 * Excluir los lotes BLOQUEADO/RETIRADO/VENCIDO/AGOTADO es intencional: el rollup debe reflejar
 * stock realmente vendible, no solo lo que fisicamente quedo en el lote.
 */
public final class SincronizadorExistencias {

    private SincronizadorExistencias() {
    }

    public static void sincronizar(UUID productoId, UUID localId, LoteRepositoryPort lotes,
            ExistenciaLoteRepositoryPort existencias, ClockPort clock) {
        List<Lote> todosLosLotes = lotes.listarPorProductoYLocal(productoId, localId);
        int total = todosLosLotes.stream()
                .filter(lote -> lote.getEstado() == EstadoLote.DISPONIBLE)
                .mapToInt(lote -> lote.getCantidadDisponible().valor())
                .sum();
        Instant ahora = clock.ahora();
        Optional<ExistenciaLote> existente = existencias.buscarPorProductoYLocal(productoId, localId);

        if (existente.isPresent()) {
            existente.get().actualizar(new Cantidad(total), ahora);
            existencias.guardar(existente.get());
        } else {
            existencias.guardar(new ExistenciaLote(productoId, localId, new Cantidad(total), ahora));
        }
    }
}
