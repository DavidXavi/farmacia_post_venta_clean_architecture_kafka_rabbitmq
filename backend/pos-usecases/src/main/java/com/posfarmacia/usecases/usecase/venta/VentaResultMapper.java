package com.posfarmacia.usecases.usecase.venta;

import com.posfarmacia.usecases.dto.venta.DetalleVentaLoteResult;
import com.posfarmacia.usecases.dto.venta.DetalleVentaResult;
import com.posfarmacia.usecases.dto.venta.PagoResult;
import com.posfarmacia.usecases.dto.venta.VentaResult;
import com.posfarmacia.usecases.port.out.inventario.ProductoRepositoryPort;
import com.posfarmacia.usecases.port.out.venta.VentaRepositoryPort;
import com.posfarmacia.domain.exception.EntidadNoEncontradaException;
import com.posfarmacia.domain.model.catalogo.Producto;
import com.posfarmacia.domain.model.venta.DetalleVenta;
import com.posfarmacia.domain.model.venta.DetalleVentaLote;
import com.posfarmacia.domain.model.venta.Pago;
import com.posfarmacia.domain.model.venta.Venta;
import java.util.List;
import java.util.UUID;

/** Traduce el agregado Venta a su DTO de salida, resolviendo el nombre de producto via el contexto de Inventario. */
final class VentaResultMapper {

    private VentaResultMapper() {
    }

    static VentaResult aResultado(Venta venta, ProductoRepositoryPort productos) {
        List<DetalleVentaResult> detalles = venta.getDetalles().stream()
                .map(detalle -> aDetalleResultado(detalle, productos))
                .toList();
        List<PagoResult> pagos = venta.getPagos().stream().map(VentaResultMapper::aPagoResultado).toList();

        return new VentaResult(
                venta.getId(),
                venta.getCajaId(),
                venta.getSesionCajaId(),
                venta.getUsuarioId(),
                venta.getClienteId(),
                venta.getConvenioSeguroId(),
                venta.getLineaCreditoId(),
                venta.getFecha(),
                venta.getEstado(),
                venta.getNumeroCorrelativo(),
                venta.getComprobante() == null ? null : venta.getComprobante().getNumero().toString(),
                venta.getTotal().monto(),
                venta.getTotalPagado().monto(),
                detalles,
                pagos);
    }

    private static DetalleVentaResult aDetalleResultado(DetalleVenta detalle, ProductoRepositoryPort productos) {
        String nombreProducto = productos.buscarPorId(detalle.getProductoId())
                .map(Producto::getNombreComercial)
                .orElse("");
        List<DetalleVentaLoteResult> lotes = detalle.getLotes().stream()
                .map(VentaResultMapper::aLoteResultado)
                .toList();

        return new DetalleVentaResult(
                detalle.getId(),
                detalle.getProductoId(),
                nombreProducto,
                detalle.getCantidad().valor(),
                detalle.getPrecioUnitario().monto(),
                detalle.getPromocionAplicadaId(),
                detalle.getRecetaId(),
                detalle.getDescuentoMonto().monto(),
                detalle.getImpuestoMonto().monto(),
                detalle.getSubtotal().monto(),
                lotes);
    }

    private static DetalleVentaLoteResult aLoteResultado(DetalleVentaLote lote) {
        return new DetalleVentaLoteResult(lote.getId(), lote.getLoteId(), lote.getCantidadTomada().valor());
    }

    private static PagoResult aPagoResultado(Pago pago) {
        return new PagoResult(pago.getId(), pago.getFormaPagoId(), pago.getMonto().monto(),
                pago.getCodigoAutorizacion(), pago.getFecha());
    }

    static Venta buscarVentaOLanzar(VentaRepositoryPort ventas, UUID ventaId) {
        return ventas.buscarPorId(ventaId)
                .orElseThrow(() -> new EntidadNoEncontradaException("La venta indicada no existe."));
    }
}
