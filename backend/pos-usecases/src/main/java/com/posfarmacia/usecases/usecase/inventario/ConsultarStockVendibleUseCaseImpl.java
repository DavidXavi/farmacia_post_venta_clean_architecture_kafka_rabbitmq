package com.posfarmacia.usecases.usecase.inventario;

import com.posfarmacia.usecases.dto.inventario.LoteResult;
import com.posfarmacia.usecases.dto.inventario.StockVendibleResult;
import com.posfarmacia.usecases.port.in.inventario.ConsultarStockVendibleUseCase;
import com.posfarmacia.usecases.port.out.ClockPort;
import com.posfarmacia.usecases.port.out.inventario.LoteRepositoryPort;
import com.posfarmacia.domain.model.inventario.Lote;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

/**
 * Caso de uso RF14: el stock vendible se calcula siempre desde los lotes vigentes (nunca desde el rollup
 * de existencias), excluyendo vencidos, retirados, bloqueados y los que estan en periodo preventivo.
 */
public class ConsultarStockVendibleUseCaseImpl implements ConsultarStockVendibleUseCase {

    private final LoteRepositoryPort lotes;
    private final ClockPort clock;

    public ConsultarStockVendibleUseCaseImpl(LoteRepositoryPort lotes, ClockPort clock) {
        this.lotes = lotes;
        this.clock = clock;
    }

    @Override
    @Transactional(readOnly = true)
    public StockVendibleResult consultar(UUID productoId, UUID localId) {
        LocalDate hoy = clock.hoy();

        List<Lote> vendiblesOrdenados = lotes.listarPorProductoYLocal(productoId, localId).stream()
                .filter(lote -> lote.esVendible(hoy))
                .sorted(Comparator.comparing(lote -> lote.getFechaVencimiento().valor()))
                .toList();

        int total = vendiblesOrdenados.stream().mapToInt(lote -> lote.getCantidadDisponible().valor()).sum();
        List<LoteResult> loteResults = vendiblesOrdenados.stream().map(LoteResultMapper::aResult).toList();

        return new StockVendibleResult(productoId, total, loteResults);
    }
}
