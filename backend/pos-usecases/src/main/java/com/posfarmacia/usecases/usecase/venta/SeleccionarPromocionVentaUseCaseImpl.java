package com.posfarmacia.usecases.usecase.venta;

import com.posfarmacia.usecases.dto.promocion.SeleccionarPromocionCommand;
import com.posfarmacia.usecases.dto.venta.SeleccionarPromocionVentaCommand;
import com.posfarmacia.usecases.dto.venta.VentaResult;
import com.posfarmacia.usecases.port.in.promocion.SeleccionarPromocionUseCase;
import com.posfarmacia.usecases.port.in.venta.SeleccionarPromocionVentaUseCase;
import com.posfarmacia.usecases.port.out.inventario.ProductoRepositoryPort;
import com.posfarmacia.usecases.port.out.venta.VentaRepositoryPort;
import com.posfarmacia.domain.exception.EntidadNoEncontradaException;
import com.posfarmacia.domain.model.promocion.AplicacionPromocion;
import com.posfarmacia.domain.model.venta.DetalleVenta;
import com.posfarmacia.domain.model.venta.Venta;
import org.springframework.transaction.annotation.Transactional;

/**
 * Caso de uso RN07-RN12: delega en {@code SeleccionarPromocionUseCase} (contexto de Promociones)
 * la decision de si la promocion es aplicable, y en el agregado Venta la invariante de que no se
 * repita en el comprobante (RN09, defensa en profundidad, ver {@code Venta.aplicarPromocionALinea}).
 */
public class SeleccionarPromocionVentaUseCaseImpl implements SeleccionarPromocionVentaUseCase {

    private final VentaRepositoryPort ventas;
    private final SeleccionarPromocionUseCase seleccionarPromocion;
    private final ProductoRepositoryPort productos;

    public SeleccionarPromocionVentaUseCaseImpl(VentaRepositoryPort ventas,
            SeleccionarPromocionUseCase seleccionarPromocion, ProductoRepositoryPort productos) {
        this.ventas = ventas;
        this.seleccionarPromocion = seleccionarPromocion;
        this.productos = productos;
    }

    @Override
    @Transactional
    public VentaResult seleccionar(SeleccionarPromocionVentaCommand command) {
        Venta venta = VentaResultMapper.buscarVentaOLanzar(ventas, command.ventaId());
        DetalleVenta detalle = venta.buscarDetalle(command.detalleVentaId())
                .orElseThrow(() -> new EntidadNoEncontradaException("La linea de venta indicada no existe."));

        SeleccionarPromocionCommand comandoPromocion = new SeleccionarPromocionCommand(
                command.promocionId(),
                venta.getId(),
                detalle.getId(),
                detalle.getProductoId(),
                detalle.getCantidad().valor(),
                detalle.getPrecioUnitario().monto(),
                venta.getClienteId() != null,
                venta.getPromocionesAplicadasIds());

        AplicacionPromocion aplicacion = seleccionarPromocion.seleccionar(comandoPromocion);
        venta.aplicarPromocionALinea(detalle.getId(), aplicacion.getPromocionId(), aplicacion.getMontoDescuento());

        Venta guardada = ventas.guardar(venta);
        return VentaResultMapper.aResultado(guardada, productos);
    }
}
