package com.posfarmacia.usecases.usecase.reporte;

import com.posfarmacia.usecases.dto.reporte.LoteProximoAVencerResult;
import com.posfarmacia.usecases.port.in.reporte.ConsultarLotesProximosAVencerUseCase;
import com.posfarmacia.usecases.port.out.ClockPort;
import com.posfarmacia.usecases.port.out.inventario.LoteRepositoryPort;
import com.posfarmacia.domain.enums.EstadoLote;
import com.posfarmacia.domain.model.inventario.Lote;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

/**
 * RF17/RN36: lotes disponibles cuyo vencimiento cae dentro del horizonte pedido (por defecto 90
 * dias, aplicado por el adaptador REST). Equivalente a
 * PosFarmacia.Application.UseCases.ConsultarLotesProximosAVencerUseCase (.NET), que consulta
 * {@code ILoteRepository.ObtenerProximosAVencerAsync}; aqui se reutiliza {@link LoteRepositoryPort}
 * ya existente (contexto de Inventario) y se filtra/ordena en este caso de uso para no ampliar un
 * puerto de otro contexto.
 */
public class ConsultarLotesProximosAVencerUseCaseImpl implements ConsultarLotesProximosAVencerUseCase {

    private final LoteRepositoryPort lotes;
    private final ClockPort clock;

    public ConsultarLotesProximosAVencerUseCaseImpl(LoteRepositoryPort lotes, ClockPort clock) {
        this.lotes = lotes;
        this.clock = clock;
    }

    @Override
    public List<LoteProximoAVencerResult> consultar(int diasHorizonte) {
        LocalDate hoy = clock.hoy();
        LocalDate limite = hoy.plusDays(diasHorizonte);

        return lotes.listar(null).stream()
                .filter(lote -> lote.getEstado() == EstadoLote.DISPONIBLE)
                .filter(lote -> !lote.getFechaVencimiento().estaVencida(hoy))
                .filter(lote -> !lote.getFechaVencimiento().valor().isAfter(limite))
                .sorted(Comparator.comparing(lote -> lote.getFechaVencimiento().valor()))
                .map(this::aResultado)
                .toList();
    }

    private LoteProximoAVencerResult aResultado(Lote lote) {
        return new LoteProximoAVencerResult(lote.getId(), lote.getCodigo().valor(), lote.getProductoId(),
                lote.getFechaVencimiento().valor(), lote.getCantidadDisponible().valor());
    }
}
