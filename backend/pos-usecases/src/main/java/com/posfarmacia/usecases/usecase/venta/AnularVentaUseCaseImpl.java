package com.posfarmacia.usecases.usecase.venta;

import com.posfarmacia.usecases.dto.venta.VentaResult;
import com.posfarmacia.usecases.port.in.venta.AnularVentaUseCase;
import com.posfarmacia.usecases.port.out.ClockPort;
import com.posfarmacia.usecases.port.out.inventario.ExistenciaLoteRepositoryPort;
import com.posfarmacia.usecases.port.out.inventario.LoteRepositoryPort;
import com.posfarmacia.usecases.port.out.inventario.MovimientoInventarioRepositoryPort;
import com.posfarmacia.usecases.port.out.inventario.ProductoRepositoryPort;
import com.posfarmacia.usecases.port.out.venta.VentaRepositoryPort;
import com.posfarmacia.usecases.usecase.inventario.SincronizadorExistencias;
import com.posfarmacia.domain.enums.TipoMovimientoStock;
import com.posfarmacia.domain.exception.EntidadNoEncontradaException;
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
 * Caso de uso RN39/RN40/RN42/RN43: anula una venta confirmada del mismo dia (el dominio rechaza
 * cualquier otro caso, pues corresponde a una nota de credito de un contexto que se migra despues)
 * y devuelve el stock despachado a sus lotes originales. La reversion de pagos/credito (RN44/RN32)
 * queda fuera de este alcance por la misma razon.
 */
public class AnularVentaUseCaseImpl implements AnularVentaUseCase {

    private final VentaRepositoryPort ventas;
    private final LoteRepositoryPort lotes;
    private final ExistenciaLoteRepositoryPort existencias;
    private final MovimientoInventarioRepositoryPort movimientosInventario;
    private final ProductoRepositoryPort productos;
    private final ClockPort clock;
    private final ServicioAnulacionVenta servicioAnulacion = new ServicioAnulacionVenta();

    public AnularVentaUseCaseImpl(VentaRepositoryPort ventas, LoteRepositoryPort lotes,
            ExistenciaLoteRepositoryPort existencias, MovimientoInventarioRepositoryPort movimientosInventario,
            ProductoRepositoryPort productos, ClockPort clock) {
        this.ventas = ventas;
        this.lotes = lotes;
        this.existencias = existencias;
        this.movimientosInventario = movimientosInventario;
        this.productos = productos;
        this.clock = clock;
    }

    @Override
    @Transactional
    public VentaResult anular(UUID ventaId) {
        Venta venta = VentaResultMapper.buscarVentaOLanzar(ventas, ventaId);
        Instant ahora = clock.ahora();

        venta.anular(clock.hoy());

        List<ReversionStock> reversiones = servicioAnulacion.obtenerReversionesDeStock(venta);
        for (ReversionStock reversion : reversiones) {
            revertirStockDeLote(venta, reversion, ahora);
        }

        Venta guardada = ventas.guardar(venta);
        return VentaResultMapper.aResultado(guardada, productos);
    }

    /** RN42/RN43: solo se devuelve el stock si el lote sigue en condiciones de venderse. */
    private void revertirStockDeLote(Venta venta, ReversionStock reversion, Instant ahora) {
        Lote lote = lotes.buscarPorId(reversion.loteId())
                .orElseThrow(() -> new EntidadNoEncontradaException("El lote asignado a la venta ya no existe."));

        boolean devuelto = lote.devolver(reversion.cantidad());
        if (!devuelto) {
            return;
        }
        lotes.guardar(lote);
        movimientosInventario.guardar(new MovimientoInventario(lote.getId(), TipoMovimientoStock.REVERSION_ANULACION,
                reversion.cantidad(), venta.getUsuarioId(), "Anulacion venta " + venta.getId(), ahora));
        SincronizadorExistencias.sincronizar(lote.getProductoId(), lote.getLocalId(), lotes, existencias, clock);
    }
}
