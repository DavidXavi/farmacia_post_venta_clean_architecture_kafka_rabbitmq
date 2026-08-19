package com.posfarmacia.usecases.usecase.anulacion;

import com.posfarmacia.usecases.dto.anulacion.EmitirNotaCreditoCommand;
import com.posfarmacia.usecases.dto.anulacion.NotaCreditoResult;
import com.posfarmacia.usecases.dto.mensajeria.EventoAuditoria;
import com.posfarmacia.usecases.port.in.anulacion.EmitirNotaCreditoUseCase;
import com.posfarmacia.usecases.port.out.ClockPort;
import com.posfarmacia.usecases.port.out.anulacion.NotaCreditoRepositoryPort;
import com.posfarmacia.usecases.port.out.mensajeria.EventoDominioPublisherPort;
import com.posfarmacia.usecases.port.out.inventario.ExistenciaLoteRepositoryPort;
import com.posfarmacia.usecases.port.out.inventario.LoteRepositoryPort;
import com.posfarmacia.usecases.port.out.inventario.MovimientoInventarioRepositoryPort;
import com.posfarmacia.usecases.port.out.venta.VentaRepositoryPort;
import com.posfarmacia.usecases.usecase.inventario.SincronizadorExistencias;
import com.posfarmacia.domain.enums.EstadoVenta;
import com.posfarmacia.domain.enums.TipoMovimientoStock;
import com.posfarmacia.domain.exception.AnulacionNoPermitidaException;
import com.posfarmacia.domain.exception.EntidadNoEncontradaException;
import com.posfarmacia.domain.model.anulacion.NotaCredito;
import com.posfarmacia.domain.model.inventario.Lote;
import com.posfarmacia.domain.model.inventario.MovimientoInventario;
import com.posfarmacia.domain.model.venta.Venta;
import com.posfarmacia.domain.service.venta.ReversionStock;
import com.posfarmacia.domain.service.venta.ServicioAnulacionVenta;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

/**
 * Caso de uso RF16/RN39/RN40/RN42/RN43: emite una nota de credito sobre una venta confirmada (la
 * via correspondiente cuando la venta ya no es del mismo dia, ver
 * {@code AnularVentaUseCaseImpl}, que rechaza la anulacion directa en ese caso) y revierte el stock
 * despachado a sus lotes originales, igual que una anulacion. Reutiliza {@link ServicioAnulacionVenta}
 * de Ventas para no duplicar el calculo de reversiones (RN42) ya existente para la anulacion total.
 *
 * <p>La reversion de pagos/linea de credito (RN44/RN32) queda fuera de este alcance, igual que en
 * {@code AnularVentaUseCaseImpl} de Ventas (decision documentada alli): ambas vias de "deshacer una
 * venta" devuelven el stock, pero la conciliacion de medios de pago/credito no forma parte de los
 * entregables de este contexto.
 */
public class EmitirNotaCreditoUseCaseImpl implements EmitirNotaCreditoUseCase {

    private final VentaRepositoryPort ventas;
    private final NotaCreditoRepositoryPort notasCredito;
    private final LoteRepositoryPort lotes;
    private final ExistenciaLoteRepositoryPort existencias;
    private final MovimientoInventarioRepositoryPort movimientosInventario;
    private final ClockPort clock;
    private final EventoDominioPublisherPort eventos;
    private final ServicioAnulacionVenta servicioAnulacion = new ServicioAnulacionVenta();

    public EmitirNotaCreditoUseCaseImpl(VentaRepositoryPort ventas, NotaCreditoRepositoryPort notasCredito,
            LoteRepositoryPort lotes, ExistenciaLoteRepositoryPort existencias,
            MovimientoInventarioRepositoryPort movimientosInventario, ClockPort clock,
            EventoDominioPublisherPort eventos) {
        this.ventas = ventas;
        this.notasCredito = notasCredito;
        this.lotes = lotes;
        this.existencias = existencias;
        this.movimientosInventario = movimientosInventario;
        this.clock = clock;
        this.eventos = eventos;
    }

    @Override
    @Transactional
    public NotaCreditoResult emitir(EmitirNotaCreditoCommand command) {
        Venta venta = ventas.buscarPorId(command.ventaId())
                .orElseThrow(() -> new EntidadNoEncontradaException("La venta indicada no existe."));

        if (venta.getEstado() != EstadoVenta.CONFIRMADA || venta.getComprobante() == null) {
            throw new AnulacionNoPermitidaException("Solo se puede emitir una nota de credito sobre una venta confirmada.");
        }

        Instant ahora = clock.ahora();

        NotaCredito notaCredito = new NotaCredito(venta.getId(), venta.getComprobante().getId(), command.usuarioId(),
                command.motivo(), venta.getTotal(), ahora);

        List<ReversionStock> reversiones = servicioAnulacion.obtenerReversionesDeStock(venta);
        for (ReversionStock reversion : reversiones) {
            revertirStockDeLote(reversion, command.usuarioId(), venta.getId(), ahora);
        }

        NotaCredito guardada = notasCredito.guardar(notaCredito);
        eventos.publicarAuditoria(new EventoAuditoria("NOTA_CREDITO_EMITIDA", "Venta", venta.getId(),
                command.usuarioId(), "Nota de credito por " + guardada.getMontoTotal().monto() + " sobre la venta "
                        + venta.getId() + ". Motivo: " + guardada.getMotivo(), ahora));
        return new NotaCreditoResult(guardada.getId(), guardada.getVentaId(), guardada.getComprobanteId(),
                guardada.getUsuarioId(), guardada.getMotivo(), guardada.getMontoTotal().monto(), guardada.getFecha());
    }

    /** RN42/RN43: solo se devuelve el stock si el lote sigue en condiciones de venderse. */
    private void revertirStockDeLote(ReversionStock reversion, UUID usuarioId, UUID ventaId, Instant ahora) {
        Lote lote = lotes.buscarPorId(reversion.loteId())
                .orElseThrow(() -> new EntidadNoEncontradaException("El lote asignado a la venta ya no existe."));

        boolean devuelto = lote.devolver(reversion.cantidad());
        if (!devuelto) {
            return;
        }
        lotes.guardar(lote);
        movimientosInventario.guardar(new MovimientoInventario(lote.getId(), TipoMovimientoStock.REVERSION_ANULACION,
                reversion.cantidad(), usuarioId, "Nota de credito venta " + ventaId, ahora));
        SincronizadorExistencias.sincronizar(lote.getProductoId(), lote.getLocalId(), lotes, existencias, clock);
    }
}
