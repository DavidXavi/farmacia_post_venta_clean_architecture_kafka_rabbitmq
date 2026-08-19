package com.posfarmacia.usecases.usecase.anulacion;

import com.posfarmacia.usecases.dto.anulacion.DevolucionResult;
import com.posfarmacia.usecases.dto.anulacion.LineaDevolucionCommand;
import com.posfarmacia.usecases.dto.anulacion.RegistrarDevolucionCommand;
import com.posfarmacia.usecases.port.in.anulacion.RegistrarDevolucionUseCase;
import com.posfarmacia.usecases.port.out.ClockPort;
import com.posfarmacia.usecases.port.out.anulacion.DevolucionRepositoryPort;
import com.posfarmacia.usecases.port.out.inventario.ExistenciaLoteRepositoryPort;
import com.posfarmacia.usecases.port.out.inventario.LoteRepositoryPort;
import com.posfarmacia.usecases.port.out.inventario.MovimientoInventarioRepositoryPort;
import com.posfarmacia.usecases.port.out.inventario.ProductoRepositoryPort;
import com.posfarmacia.usecases.port.out.venta.VentaRepositoryPort;
import com.posfarmacia.usecases.usecase.inventario.SincronizadorExistencias;
import com.posfarmacia.domain.enums.TipoMovimientoStock;
import com.posfarmacia.domain.exception.EntidadNoEncontradaException;
import com.posfarmacia.domain.model.anulacion.Devolucion;
import com.posfarmacia.domain.model.catalogo.Producto;
import com.posfarmacia.domain.model.inventario.Lote;
import com.posfarmacia.domain.model.inventario.MovimientoInventario;
import com.posfarmacia.domain.model.venta.DetalleVenta;
import com.posfarmacia.domain.model.venta.Venta;
import com.posfarmacia.domain.service.anulacion.AsignadorReversionesDevolucion;
import com.posfarmacia.domain.service.anulacion.ValidadorDevolucion;
import com.posfarmacia.domain.service.venta.ReversionStock;
import com.posfarmacia.domain.valueobject.Cantidad;
import com.posfarmacia.domain.valueobject.Dinero;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;

/**
 * Caso de uso RF16/RN42/RN43: registra una devolucion parcial de una o mas lineas de una venta
 * confirmada. {@link ValidadorDevolucion} decide si cada linea puede devolverse (venta confirmada,
 * producto no controlado, dentro del plazo, cantidad disponible); {@link AsignadorReversionesDevolucion}
 * distribuye la cantidad devuelta entre los lotes que surtieron esa linea, y el stock solo regresa
 * al lote si {@code Lote.devolver} confirma que sigue en condiciones de venderse (RN43) -- si el
 * lote ya no es vendible, la linea igual se descuenta de la venta (queda registrada la devolucion)
 * pero esa porcion de stock no vuelve a aparecer como vendible, dejando constancia del motivo en el
 * propio movimiento de inventario que sí se aplica para el resto de lotes.
 *
 * <p>RN21: una receta especial retenida asociada a la venta NO se libera aqui; ese contexto no se
 * toca desde esta implementacion.
 */
public class RegistrarDevolucionUseCaseImpl implements RegistrarDevolucionUseCase {

    private final VentaRepositoryPort ventas;
    private final ProductoRepositoryPort productos;
    private final DevolucionRepositoryPort devoluciones;
    private final LoteRepositoryPort lotes;
    private final ExistenciaLoteRepositoryPort existencias;
    private final MovimientoInventarioRepositoryPort movimientosInventario;
    private final ClockPort clock;
    private final ValidadorDevolucion validador = new ValidadorDevolucion();
    private final AsignadorReversionesDevolucion asignadorReversiones = new AsignadorReversionesDevolucion();

    public RegistrarDevolucionUseCaseImpl(VentaRepositoryPort ventas, ProductoRepositoryPort productos,
            DevolucionRepositoryPort devoluciones, LoteRepositoryPort lotes, ExistenciaLoteRepositoryPort existencias,
            MovimientoInventarioRepositoryPort movimientosInventario, ClockPort clock) {
        this.ventas = ventas;
        this.productos = productos;
        this.devoluciones = devoluciones;
        this.lotes = lotes;
        this.existencias = existencias;
        this.movimientosInventario = movimientosInventario;
        this.clock = clock;
    }

    @Override
    @Transactional
    public DevolucionResult registrar(RegistrarDevolucionCommand command) {
        Venta venta = ventas.buscarPorId(command.ventaId())
                .orElseThrow(() -> new EntidadNoEncontradaException("La venta indicada no existe."));

        LocalDate hoy = clock.hoy();
        Instant ahora = clock.ahora();

        var yaDevueltoPorDetalle = devoluciones.buscarPorVenta(command.ventaId()).stream()
                .flatMap(devolucionPrevia -> devolucionPrevia.getDetalles().stream())
                .collect(Collectors.groupingBy(detalle -> detalle.getDetalleVentaId(),
                        Collectors.summingInt(detalle -> detalle.getCantidad().valor())));

        Devolucion devolucion = new Devolucion(venta.getId(), command.usuarioId(), command.motivo(), ahora);

        for (LineaDevolucionCommand linea : command.lineas()) {
            DetalleVenta detalle = venta.buscarDetalle(linea.detalleVentaId())
                    .orElseThrow(() -> new EntidadNoEncontradaException(
                            "La linea de venta indicada no existe en esta venta."));

            Producto producto = productos.buscarPorId(detalle.getProductoId())
                    .orElseThrow(() -> new EntidadNoEncontradaException("El producto de la linea de venta no existe."));

            Cantidad cantidadYaDevuelta = new Cantidad(yaDevueltoPorDetalle.getOrDefault(detalle.getId(), 0));
            Cantidad cantidadSolicitada = new Cantidad(linea.cantidad());

            validador.validar(venta.getEstado(), venta.getFecha(), producto.isEsControlado(), detalle.getCantidad(),
                    cantidadYaDevuelta, cantidadSolicitada, hoy);

            Dinero montoDevuelto = calcularMontoDevuelto(detalle, cantidadSolicitada);
            devolucion.agregarLinea(detalle.getId(), detalle.getProductoId(), cantidadSolicitada, montoDevuelto);

            List<ReversionStock> reversiones = asignadorReversiones.asignar(detalle.getLotes(), cantidadSolicitada);
            for (ReversionStock reversion : reversiones) {
                revertirStockDeLote(reversion, command.usuarioId(), devolucion.getId(), ahora);
            }
        }

        Devolucion guardada = devoluciones.guardar(devolucion);
        return DevolucionResultMapper.aResultado(guardada);
    }

    private static Dinero calcularMontoDevuelto(DetalleVenta detalle, Cantidad cantidadSolicitada) {
        BigDecimal montoUnitario = detalle.getSubtotal().monto()
                .divide(BigDecimal.valueOf(detalle.getCantidad().valor()), 2, RoundingMode.HALF_UP);
        return new Dinero(montoUnitario.multiply(BigDecimal.valueOf(cantidadSolicitada.valor())));
    }

    /** RN42/RN43: solo se devuelve el stock si el lote sigue en condiciones de venderse. */
    private void revertirStockDeLote(ReversionStock reversion, UUID usuarioId, UUID devolucionId, Instant ahora) {
        Lote lote = lotes.buscarPorId(reversion.loteId())
                .orElseThrow(() -> new EntidadNoEncontradaException("El lote asignado a la venta ya no existe."));

        boolean devuelto = lote.devolver(reversion.cantidad());
        if (!devuelto) {
            return;
        }
        lotes.guardar(lote);
        movimientosInventario.guardar(new MovimientoInventario(lote.getId(), TipoMovimientoStock.REVERSION_DEVOLUCION,
                reversion.cantidad(), usuarioId, "Devolucion " + devolucionId, ahora));
        SincronizadorExistencias.sincronizar(lote.getProductoId(), lote.getLocalId(), lotes, existencias, clock);
    }
}
