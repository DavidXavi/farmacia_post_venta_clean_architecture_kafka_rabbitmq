package com.posfarmacia.usecases.usecase.venta;

import com.posfarmacia.usecases.dto.venta.ConsultarVentasDiariasQuery;
import com.posfarmacia.usecases.dto.venta.VentaResult;
import com.posfarmacia.usecases.port.in.venta.ConsultarVentasDiariasUseCase;
import com.posfarmacia.usecases.port.out.inventario.ProductoRepositoryPort;
import com.posfarmacia.usecases.port.out.venta.VentaRepositoryPort;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

/** Caso de uso RF17: lista ventas filtradas por fecha, caja, usuario y/o cliente. */
public class ConsultarVentasDiariasUseCaseImpl implements ConsultarVentasDiariasUseCase {

    private final VentaRepositoryPort ventas;
    private final ProductoRepositoryPort productos;

    public ConsultarVentasDiariasUseCaseImpl(VentaRepositoryPort ventas, ProductoRepositoryPort productos) {
        this.ventas = ventas;
        this.productos = productos;
    }

    @Override
    @Transactional(readOnly = true)
    public List<VentaResult> consultar(ConsultarVentasDiariasQuery query) {
        return ventas.buscar(query.fecha(), query.cajaId(), query.usuarioId(), query.clienteId()).stream()
                .map(venta -> VentaResultMapper.aResultado(venta, productos))
                .toList();
    }
}
