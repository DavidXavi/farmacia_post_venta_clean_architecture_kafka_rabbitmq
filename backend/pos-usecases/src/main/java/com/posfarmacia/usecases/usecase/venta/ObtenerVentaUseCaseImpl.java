package com.posfarmacia.usecases.usecase.venta;

import com.posfarmacia.usecases.dto.venta.VentaResult;
import com.posfarmacia.usecases.port.in.venta.ObtenerVentaUseCase;
import com.posfarmacia.usecases.port.out.inventario.ProductoRepositoryPort;
import com.posfarmacia.usecases.port.out.venta.VentaRepositoryPort;
import com.posfarmacia.domain.model.venta.Venta;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

/** Caso de uso RF05: consulta una venta por id. */
public class ObtenerVentaUseCaseImpl implements ObtenerVentaUseCase {

    private final VentaRepositoryPort ventas;
    private final ProductoRepositoryPort productos;

    public ObtenerVentaUseCaseImpl(VentaRepositoryPort ventas, ProductoRepositoryPort productos) {
        this.ventas = ventas;
        this.productos = productos;
    }

    @Override
    @Transactional(readOnly = true)
    public VentaResult obtener(UUID ventaId) {
        Venta venta = VentaResultMapper.buscarVentaOLanzar(ventas, ventaId);
        return VentaResultMapper.aResultado(venta, productos);
    }
}
