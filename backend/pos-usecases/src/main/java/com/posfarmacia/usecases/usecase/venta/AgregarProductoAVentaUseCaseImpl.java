package com.posfarmacia.usecases.usecase.venta;

import com.posfarmacia.usecases.dto.venta.AgregarProductoCommand;
import com.posfarmacia.usecases.dto.venta.VentaResult;
import com.posfarmacia.usecases.port.in.venta.AgregarProductoAVentaUseCase;
import com.posfarmacia.usecases.port.out.inventario.ProductoRepositoryPort;
import com.posfarmacia.usecases.port.out.venta.VentaRepositoryPort;
import com.posfarmacia.domain.exception.EntidadNoEncontradaException;
import com.posfarmacia.domain.model.catalogo.Producto;
import com.posfarmacia.domain.model.venta.Venta;
import com.posfarmacia.domain.valueobject.Cantidad;
import com.posfarmacia.domain.valueobject.Porcentaje;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

/** Caso de uso RF05: agrega una linea de producto a una venta en proceso. */
public class AgregarProductoAVentaUseCaseImpl implements AgregarProductoAVentaUseCase {

    /** IGV: unica tasa de impuesto vigente en el alcance de este proyecto (ver comentario de ayuda en VentaPage.jsx). */
    private static final Porcentaje IGV = Porcentaje.de(18);

    private final VentaRepositoryPort ventas;
    private final ProductoRepositoryPort productos;

    public AgregarProductoAVentaUseCaseImpl(VentaRepositoryPort ventas, ProductoRepositoryPort productos) {
        this.ventas = ventas;
        this.productos = productos;
    }

    @Override
    @Transactional
    public VentaResult agregar(UUID ventaId, AgregarProductoCommand command) {
        Venta venta = VentaResultMapper.buscarVentaOLanzar(ventas, ventaId);
        Producto producto = productos.buscarPorId(command.productoId())
                .orElseThrow(() -> new EntidadNoEncontradaException("El producto indicado no existe."));

        venta.agregarDetalle(producto.getId(), new Cantidad(command.cantidad()), producto.getPrecioVenta(), IGV,
                command.recetaId());

        Venta guardada = ventas.guardar(venta);
        return VentaResultMapper.aResultado(guardada, productos);
    }
}
